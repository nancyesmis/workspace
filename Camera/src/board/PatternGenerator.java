package board;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.sun.image.codec.jpeg.*;

import java.awt.image.*;
import java.io.*;

//
//	generates printalbe pattern images (A4 size)
//		patterns0.jpg ... patterns108.png
//
public class PatternGenerator extends Panel{
	
	static int id_offset = 0;
	static boolean SHOW_FRAME = false;
	public static void main(String[] args){
		Pattern.init();
		
//		if (args.length ==1)
//			id_offset = (new Integer(args[0])).intValue();

		PatternGenerator pg = new PatternGenerator();
		
		for(int i=0; i<10; i++){
			id_offset = i*12;
			pg.save();
		}

		if (SHOW_FRAME){
			Frame f = new Frame("Main");
			f.addWindowListener(
					new WindowAdapter(){
						public void windowClosing(WindowEvent e) {System.exit(0);}}
			);
		
			f.add("Center", pg);
			f.setSize(1000, 1000);
			f.setVisible(true);
		}
	}


	
	Image buffered_image = null;
	public void update(Graphics g) {
		paint(g);
	}
	public void paint(Graphics g) {

			//double buffering
			if (buffered_image == null)
				buffered_image = createImage(getSize().width, getSize().height);
			Graphics bg = buffered_image.getGraphics();

			// clear
			bg.setColor(Color.white);
			bg.fillRect(0,0, getSize().width, getSize().height);
			
			paint_main(bg);

			//double buffering
			bg.dispose();
      
			
			g.drawImage(buffered_image,0,0,this);
			//g.drawImage(buffered_image,0,0,800,800,0,0,200,200,this);
			
			
	}
	void save(){
		try{
		File f = new File("patterns"+id_offset+".jpg");
        BufferedImage img = new BufferedImage(200*4,300*3, BufferedImage.TYPE_INT_RGB);

        
        Graphics g = img.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0,0, 200*4, 300*3);
		paint_main(g);
        
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(img);
        param.setQuality(1f, true);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(img);

		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void paint_main(Graphics g){
		
		g.setFont(new Font("Helvetica", 0, 60));
		
		int w = 200;
		int h = 200;
		int hm = h+100;
		int wm = 32;	//16
		int bm = 16;
		for(int id=0; id<12; id++){
			Pattern pattern = Pattern.get_pattern(id_offset+id);
			
			int y = id/4;
			int x = id - y*4;
			
			g.setColor(Color.lightGray);
			g.drawRect(x*w, y*hm, w-1, hm-1);

			g.setColor(Color.black);
			g.drawString(""+(id_offset+id), x*w+wm, y*hm+60+wm);
			g.fillRect(x*w+wm, y*hm+100+wm, w-2*wm, h-2*wm);
			paint_id(g, pattern.matrix, x*w+wm+bm, y*hm+100+wm+bm, w-2*wm - 2*bm, h-2*wm -2*bm);
		}
	}
	public void paint_id(Graphics g, int[][] matrix, int x0, int y0, int W, int H){
		int m = 1;
		int w = W / 3;
		int h = H / 3;
		for(int x=0; x<3; x++)
			for(int y=0; y<3; y++ ){
				int bit = matrix[x][y];
				if (bit == 1)
					g.setColor(Color.black);
				else
					g.setColor(Color.white);
				g.fillRect(x0+x*w, y0+y*h, w, h);
				m*=2;
			}
	}
}
