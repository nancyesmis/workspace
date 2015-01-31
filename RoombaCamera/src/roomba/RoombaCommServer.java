package roomba;

// 
// - IGARASHI Design Interface Project -
//
// Author : Koichi nakamura
//

import java.net.*;
import java.io.*;
import roombacomm.RoombaCommSerial;

public class RoombaCommServer {
	public static void main(String argv[]) {
		RoombaCommSerial[] comm = new RoombaCommSerial[argv.length];
		for (int i = 0; i < argv.length; i++) {
			comm[i] = setupRoombaComm(argv[i]);
		}

		try {
			ServerSocket serverSocket = new ServerSocket(7777);

			while(true) {
				try {
					Socket socket = serverSocket.accept();
					new Worker(socket, comm);
				} catch (Exception e) {}
			}
		} catch (IOException e) {}
	}

	private static RoombaCommSerial setupRoombaComm(String port)
	{
		RoombaCommSerial comm = new RoombaCommSerial();
		comm.waitForDSR = true;
		comm.flushOutput = false;

		String portList[] = comm.listPorts();
		System.out.println("Available ports:");
		for (int i = 0; i < portList.length; i++)
			System.out.println("  " + i + ": " + portList[i]);

		System.out.println("Try connect to \"" + port + '\"');
		if (!comm.connect(port)) {
			System.err.println("Couldn't connect to " + port);
			return null;
		}

		System.out.println("Roomba startup on port" + port);
		comm.startup();
		comm.control();
		comm.pause(30);

		System.out.println("Checking for Roomba... ");
		if (!comm.updateSensors()) {
			System.err.println("Roomba not found");
			return null;
		}

		System.out.println("Roomba found!");
		System.out.println(comm.capacity());
		System.out.println(comm.charge());

		comm.playNote( 72, 10 );  // C
		comm.pause( 200 );
		comm.playNote( 79, 10 );  // G
		comm.pause( 200 );
		comm.playNote( 76, 10 );  // E
		comm.pause( 200 );			
		return comm;
	}
}

class Worker extends Thread {
	final static int Stop        = 0;
	final static int GoForward   = 1;
	final static int GoBackward  = 2;
	final static int SpinLeft    = 3;
	final static int SpinRight   = 4;
	final static int Clean       = 5;
	final static int Control     = 6;
	final static int TurnLeft    = 7;
	final static int TurnRight   = 8;
	final static int Turn        = 9;
	final static int GoForwardAt = 10;
	final static int Spin        = 11;
	final static int Spot        = 12;
	final static int PlayNote    = 13;
	final static int PlaySong    = 14;

	final static int Sensor = 101;

	final static int BumpOff            = 0;
	final static int WheelDropCenterOff = 1;
	final static int WheelDropLeftOff   = 2;
	final static int WheelDropRightOff  = 3;

	private Socket socket = null;
	private PrintWriter out;
	private BufferedReader in;
	private RoombaCommSerial[] roombaComm;

	public Worker(Socket socket, RoombaCommSerial[] comm) {
		this.roombaComm = comm;
		this.socket = socket;
		this.start();
	}

	public void run() {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String cmd;
			while ((cmd = in.readLine()) != null)
				exec(cmd);

			in.close();
			out.close();
			socket.close();
		} catch(Exception e) {
			try { 
				socket.close(); 
			} catch(Exception ex) { }
		}
	}

	private void exec(String command) {
		int i = command.charAt(0);
		switch (command.charAt(1)) {
			case Stop: 
				roombaComm[i].stop();
				//System.out.println("stop");
				break;

			case GoForward:
				roombaComm[i].goForward();
				//System.out.println("go-forward");
				break;

			case GoBackward:
				roombaComm[i].goBackward();
				//System.out.println("go-backward");
				break;

			case SpinLeft:
				roombaComm[i].spinLeft();
				//System.out.println("spin-left");
				break;

			case SpinRight:
				roombaComm[i].spinRight();
				//System.out.println("spin-right");
				break;

			case Clean:
				roombaComm[i].clean();
				//System.out.println("clean");
				break;

			case Control:
				roombaComm[i].control();
				//System.out.println("clean");
				break;

			case TurnLeft:
				roombaComm[i].turnLeft();
				//System.out.println("turn-left");
				break;

			case TurnRight:
				roombaComm[i].turnRight();
				//System.out.println("turn-right");
				break;

			case Turn:
				//System.out.println("turn");
				int radius = command.charAt(2);
				roombaComm[i].turn(radius);
				break;

			case GoForwardAt:
				int speed = command.charAt(2);
				roombaComm[i].goForwardAt(speed);
				break;

			case Spin:
				int degree = command.charAt(3);
				degree = (degree << 8) | command.charAt(2);
				roombaComm[i].spin(degree);
				break;

			case Spot:
				roombaComm[i].control();
				roombaComm[i].spot();
				break;

			case Sensor:
				roombaComm[i].sensors();

				char info = 0;
				if (roombaComm[i].bump())
					info |= 1 << BumpOff;
				if (roombaComm[i].wheelDropCenter())
					info |= 1 << WheelDropCenterOff;
				if (roombaComm[i].wheelDropLeft())
					info |= 1 << WheelDropLeftOff;
				if (roombaComm[i].wheelDropRight())
					info |= 1 << WheelDropRightOff;
				out.println(info);
				break;

			case PlayNote:
				roombaComm[i].playNote( command.charAt(2), 10);
				roombaComm[i].pause( 200 );
				break;

			case PlaySong:
		        roombaComm[i].playNote( 72, 10 );  // C
		        roombaComm[i].pause( 200 );
		        roombaComm[i].playNote( 79, 10 );  // G
		        roombaComm[i].pause( 200 );
		        roombaComm[i].playNote( 76, 10 );  // E
		        roombaComm[i].pause( 200 );			
				break;

			default:
				return;
		}
	}
}
