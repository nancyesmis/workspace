package board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class VisionClient {
	static int WIDTH = 640;
	static int HEIGHT = 480;

	static Socket socket;
	static BufferedReader br;

	static long frameIndex = 0;

	public static void main(String[] args) {

		try {
			socket = new Socket("localhost", 7780);
			br = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			while (true) { // XXX

				String line = br.readLine();
				if (line != null)
					System.out.println("" + line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void connect() {
		try {
			socket = new Socket("localhost", 7777);
			br = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			start_thread();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String read() {
		try {
			return br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static Image image = null;
	static Image image0 = null;
	static Image image1 = null;
	static ArrayList<Blob> blobs0 = null;
	static ArrayList<Blob> blobs1 = null;

	static void read_and_update() {
		blobs0 = read_and_update(0);
		image0 = image;
		blobs1 = read_and_update(1);
		image1 = image;
		Main.mainPanel.repaint();

		frame0.setImageAndBlobs(image0, blobs0);
		frame1.setImageAndBlobs(image1, blobs1);
	}

	static ViewFrame frame0 = new ViewFrame("camera0", 0, 0, WIDTH + 10,
			HEIGHT + 20);
	static ViewFrame frame1 = new ViewFrame("camera1", WIDTH / 2 + 10, 20,
			WIDTH + 10, HEIGHT + 20);

	private static final int calibration_marker_id = 59;
	private static Vector2D[] calibration_marker_positions = new Vector2D[2];
	private static Vector2D translation0 = new Vector2D(112.0, 655.25);
	private static Vector2D translation1 = new Vector2D(759.75, 623.0);
	private static double rotation0 = 0.0;
	private static double rotation1 = 0.0;

	static ArrayList<Blob> read_and_update(int camera_id) {
		int[] pixels = new int[WIDTH * HEIGHT];

		String line = read();
		if (line == null)
			return null;

		StringTokenizer st = new StringTokenizer(line, " ");
		int index = 0;
		boolean is_black = true;
		int b = from_rgb(0, 0, 0);
		int w = from_rgb(255, 255, 255);
		// System.out.println(""+line);
		while (st.hasMoreTokens()) {
			int length = new Integer(st.nextToken()).intValue();
			;
			for (int i = 0; i < length; i++)
				pixels[index++] = (is_black) ? b : w;
			is_black = !is_black;
		}
		// // flip
		// int[] reverse = new int[pixels.length];
		// for(int i=0; i<HEIGHT; i++)
		// for(int j=0; j<WIDTH; j++)
		// reverse[i*WIDTH+j] = pixels[(HEIGHT-i-1)*WIDTH+j];
		// pixels = reverse;

		ArrayList<Blob> blobs = new ArrayList<Blob>();

		try {
			blobs = Detector.detect_video(pixels);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ImageProducer ip = new MemoryImageSource(WIDTH, HEIGHT, pixels, 0,
				WIDTH);
		image = Main.f.createImage(ip);

		/*
		 * double fixangle = 0.0; Vector2D fixpos = new Vector2D(); if
		 * (camera_id == 1){ // 759.75 623.0 // 760.5 624.0 fixpos.x =759.75 ;
		 * fixpos.y = 623.0; fixangle = 0; } else {
		 * 
		 * //112.0 655.5 //112.0 655.25 fixpos.x = 112.0 ; fixpos.y = 655.25;
		 * fixangle = 0; } // fixpos.x = 0.0; // fixpos.y = 0.0;
		 */

		for (Blob blob : blobs) {
			if (blob.id == calibration_marker_id)
				calibration_marker_positions[camera_id] = blob.position;

			if (camera_id == 0) {
				SendServer.send("" + camera_id + " " + blob.id + " "
						+ Vector2D.subtract(blob.position, translation0) + " "
						+ Vector2D.rotate(blob.orientation, rotation0));
			} else if (camera_id == 1) {
				SendServer.send("" + camera_id + " " + blob.id + " "
						+ Vector2D.subtract(blob.position, translation1) + " "
						+ Vector2D.rotate(blob.orientation, rotation1));
			}
		}
		SendServer.send("frame " + (frameIndex++));

		return blobs;
	}

	static boolean calibrate() {
		if (calibration_marker_positions[0] == null
				|| calibration_marker_positions[1] == null)
			return false;

		translation0 = calibration_marker_positions[0];
		translation1 = calibration_marker_positions[1];

		return true;
	}

	static void paint(Graphics g) {
		paint(g, VisionClient.image0, blobs0, 0);
		paint(g, VisionClient.image1, blobs1, 961);
	}

	static void paint(Graphics g, Image image, ArrayList<Blob> blobs, int x0) {
		if (image == null)
			return;

		int w = image.getWidth(null);
		int h = image.getHeight(null);

		int s = 1;

		g.drawImage(image, x0, 0, x0 + w, h, 0, 0, w, h, null);

		for (Blob blob : blobs) {

			g.setColor(Color.red);

			g.drawLine(x0 + (int) (s * (0.5 + blob.p0.x)),
					(int) (s * (0.5 + blob.p0.y)), x0
							+ (int) (s * (0.5 + blob.p1.x)),
					(int) (s * (0.5 + blob.p1.y)));
			g.drawLine(x0 + (int) (s * (0.5 + blob.p1.x)),
					(int) (s * (0.5 + blob.p1.y)), x0
							+ (int) (s * (0.5 + blob.p2.x)),
					(int) (s * (0.5 + blob.p2.y)));
			g.drawLine(x0 + (int) (s * (0.5 + blob.p2.x)),
					(int) (s * (0.5 + blob.p2.y)), x0
							+ (int) (s * (0.5 + blob.p3.x)),
					(int) (s * (0.5 + blob.p3.y)));
			g.drawLine(x0 + (int) (s * (0.5 + blob.p3.x)),
					(int) (s * (0.5 + blob.p3.y)), x0
							+ (int) (s * (0.5 + blob.p0.x)),
					(int) (s * (0.5 + blob.p0.y)));

			int r = 2;

			// show id and orientation
			g.setColor(Color.green);
			g.drawString("" + blob.id,
					x0 + (int) (s * (0.5 + blob.position.x)), -20
							+ (int) (s * (0.5 + blob.position.y)));

			Vector2D v0 = blob.position;
			Vector2D v1 = Vector2D.add(v0, Vector2D.multiply(blob.orientation,
					20));
			g.setColor(Color.red);
			g.drawLine(x0 + (int) (s * (0.5 + v0.x)), (int) (s * (0.5 + v0.y)),
					x0 + (int) (s * (0.5 + v1.x)), (int) (s * (0.5 + v1.y)));

		}
	}

	static int from_rgb(int red, int green, int blue) {
		return (255 << 24) | (red << 16) | (green << 8) | blue;
	}

	static void start_thread() {
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(200);
					} catch (Exception e) {
						e.printStackTrace();
					}
					read_and_update();
				}
			}
		};
		thread.start();
	}
}
