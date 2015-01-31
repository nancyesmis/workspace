package board;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class SendServer {

	static final boolean DISABLED = false;

	public static final int PORT = 7780;
	static ServerSocket serverSocket = null;
	static Socket socket = null;

	public static void start_server() {
		if (DISABLED)
			return;

		(new Thread() {
			public void run() {
				start_server_main();
			}
		}).start();

		(new Thread() {
			public void run() {
				send_to_clients();
			}
		}).start();
	}

	static Vector<BufferedWriter> bufwriters = new Vector<BufferedWriter>();

	static void start_server_main() {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				socket = serverSocket.accept();
				bufwriters.add(new BufferedWriter(new OutputStreamWriter(socket
						.getOutputStream())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static void send_to_clients() {
		while (true) {
			sleep(50);

			if (messages.size() == 0)
				continue;

			lock = true;
			Iterator<BufferedWriter> i = bufwriters.iterator();
			while (i.hasNext()) {
				BufferedWriter bufwriter = i.next();

				try {
					for (String message : messages)
						send_main(bufwriter, message);
				} catch (Exception e) {
					e.printStackTrace();
					i.remove();
				}
			}
			messages.clear();
			lock = false;
		}
		// socket.close();
	}

	static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static boolean lock = false;
	static ArrayList<String> messages = new ArrayList<String>(1000);

	static void send(String message) {
		while (lock) {
			sleep(10);
		}
		messages.add(message);
	}

	static void send_main(BufferedWriter bufwriter, String message)
			throws IOException {
		bufwriter.write(message, 0, message.length());
		bufwriter.newLine();
		bufwriter.flush();

		// bufwriter.close();
	}

}
