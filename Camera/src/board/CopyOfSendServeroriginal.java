package board;

import java.net.*;
import java.io.*;
import java.util.*;

public class CopyOfSendServeroriginal {
	
	static final boolean DISABLED = false;
	
	public static final int PORT = 7780;
	static ServerSocket serverSocket = null;
	static Socket socket = null;

	public static void start_server() {
		if (DISABLED)
			return;

		(new Thread(){
			public void run(){
				start_server_main();
			}
		}).start();
	}
	static BufferedWriter bufwriter;
	static void start_server_main(){
		
		try {
			serverSocket = new ServerSocket(PORT);
			socket = serverSocket.accept();
			bufwriter = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));
			
			//int index = 0;
			while(true){
				lock = true;
				for(String message : messages)
					send_main(message);
				messages.clear();
				lock = false;
//				if (index < messages.size()){
//					System.out.println(messages.get(index));
//					send_main(messages.get(index++));
//				}
//				else {
				
				sleep(100);
//				}
			}
			//socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static void sleep(int time){
		try {
			Thread.sleep(time);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	static boolean lock = false;
	static ArrayList<String> messages  = new ArrayList<String>(1000);
	static void send(String message) {
		while(lock){
			sleep(100);
		}
		messages.add(message);
	}		
	static void send_main(String message) {
		try {
			bufwriter.write(message, 0, message.length());
			bufwriter.newLine();
			bufwriter.flush();
			
			//bufwriter.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
