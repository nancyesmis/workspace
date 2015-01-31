package board;

import java.util.*;
import java.awt.*;
import java.awt.image.*;





// 
//	get_image and detect 2D barcodes (id and orientation)
// 
public class Detector {
	static String IMAGE_FILE_NAME = "images\\photo0.png";
	
	//
	//	main program  
	//		input: pixels	(format : (alpha << 24) | (red << 16) | (green << 8) | blue)
	//		output: blobs   
	//
	static int MIN_BLOB_SIZE = 8;//13;//8;
	static int MAX_BLOB_SIZE = 17;//26;//17;
	static double MIN_WH_RATIO = 0.7;
	static double MAX_WH_RATIO = 1.4;
	static double MIN_AREA = 60;//100;//60;
	static ArrayList<Blob> detect_main(int[] pixels, int w, int h){

		// remove too-large black regions
        int[] type = apply_segmentation(pixels, w, h);
        
        // identify reasonable-sized black regions
        ArrayList<Blob> blob_candidates = find_blobs(type, pixels, w, h);
        
        // 4 corners (remove bad aspect ratio blobs)
        ArrayList<Blob> blobs = new ArrayList<Blob>();
        for(Blob blob : blob_candidates){
        	
        	try{
        	    blob = compute_blob_corners(blob, pixels, w, h);
        	}catch(Exception e){
        		System.out.println("Error: "+ e.toString());
        		blob = null;
        	}
        	
        	if (blob != null)
        		blobs.add(blob);
        }
        

        //System.out.println("# of blobs = "+blobs.size());
        
        // decode
        for(Blob blob : blobs)
        	decode_blob(blob, pixels, w, h);
        
        if (SHOW_SEGMENTATION_RESULT)
        	render_segmentation_result(type, w, h);
        
        return blobs;
	}
	
	

	static final int NONE = 0;	// before processing
	static final int DEAD = 1;	// inappropriate as blob (too large region)
	static final int GOOD = 2;	// candidate for a blob
	static final int BLOB = 3;	// confirmed (for visualization only)
	
	static int[] apply_segmentation(int[] pixels, int w, int h){
        int[] type = new int[w*h];
        for(int x=0; x<w; x++){
        	type[x] = DEAD;
        	type[w*(h-1)+x] = DEAD;
        }
        for(int y=0; y<h; y++){
        	type[y*w] = DEAD;
        	type[y*w+w-1] = DEAD;
        }
        for (int y = 1; y < h-1; y++) {
        	int black_count = 0;
        	for (int x = 1; x < w-1; x++) {
        		
        		if (is_black(pixels[y*w+x])){
        			if ( type[y*w+x-1]==DEAD ){
        				// ¶—×‚ª~‚È‚ç~
        				type[y*w+x] = DEAD;
        				flood(x, y-1, w, pixels, type);
        			}
        			else if ( type[(y-1)*w+x]==DEAD){
        				// ã‚ª~‚È‚ç~B‚³‚©‚Ì‚Ú‚Á‚Ä~‚É‚·‚éB
        				type[y*w+x] = DEAD;

       					for(int k=0; k<=black_count; k++){
       						type[y*w+x-k] = DEAD;
            				flood(x-k, y-1, w, pixels, type);
       					}
        			}
        			else {
        				black_count++;
        				type[y*w+x] = GOOD;
        				
        				// ’·‚³‚ª’´‚¦‚½‚ç‚³‚©‚Ì‚Ú‚Á‚Ä~‚É‚·‚é
        				if (black_count > MAX_BLOB_SIZE){
        					for(int k=0; k<black_count; k++){
        						type[y*w+x-k] = DEAD;
                				flood(x-k, y-1, w, pixels, type);
        					}
        				}
        			}
        		}
        		else {
        			black_count = 0;
        		}
        	}
        }
        return type;
	}
	static void flood(int x, int y, int w, int[] pixels, int[] type){
		if (type[y*w+x] != GOOD)
			return;
		type[y*w+x] = DEAD;
		flood(x-1, y, w, pixels, type);
		flood(x+1, y, w, pixels, type);
		flood(x, y-1, w, pixels, type);
		flood(x, y+1, w, pixels, type);
	}
		
	
	//
	// find connected region with good size
	//
	static ArrayList<Blob> find_blobs(int[] type, int[] pixels, int w, int h){   

        blobs = new ArrayList<Blob>();
        for (int y = 1; y < h-1; y++) {
        	for (int x = 1; x < w-1; x++) {
        		if (type[y*w+x] == GOOD){
        			Blob blob = check_size_and_get_blob(x, y, w, pixels, type);
        			if (blob != null)
        				blobs.add(blob);
        		}
        	}
        }
        
        
        return blobs;
	}
	
	//
	// compute 4 corners
	//
	static Blob compute_blob_corners(Blob blob, int[] pixels, int w, int h){
        	
		//
		// detect boundary points
		// (edge between white and black pixel)
		//
        	blob.boundary_points = new ArrayList<Vector2D>();
        	for(Point p : blob.points){
        		int x = p.x;
        		int y = p.y;
        	
        		boolean u = is_black(pixels[(y-1)*w+x]); 
        		boolean d = is_black(pixels[(y+1)*w+x]); 
        		boolean l = is_black(pixels[y*w+x-1]); 
        		boolean r = is_black(pixels[y*w+x+1]); 
        	
        		int count = 0;
        		if (!u || !d || !r || !l){
        			Vector2D v = new Vector2D(0, 0);
        			if ( !u ){
        				v.add(new Vector2D(x, y-0.5));
        				count++;
        			}
        			if ( !l ){
        				v.add(new Vector2D(x-0.5, y));
        				count++;
        			}
        			if ( !d ){
        				v.add(new Vector2D(x, y+0.5));
        				count++;
        			}
        			if ( !r ){
        				v.add(new Vector2D(x+0.5, y));
        				count++;
        			}
        			v.multiply(1.0/count);
        			blob.boundary_points.add(v);
        		}
        	}
        	
        	//@‘ÎŠpü‚ðŒ©‚Â‚¯‚é
        	
        	//
        	// find far most pairs in boundary points
        	//
        	double max_d = -1;
        	Vector2D[] diag0 = new Vector2D[2];
        	for(Vector2D p : blob.boundary_points){
        		for(Vector2D q : blob.boundary_points){
        			double d = Vector2D.distance(p, q);
        			if (d > max_d){
        				diag0[0] = p;
        				diag0[1] = q;
        				max_d = d;
        			}
        		}
        	}
        	//
        	// find second far most pairs in boundary points
        	//
        	max_d = -1;
        	Vector2D[] diag1 = new Vector2D[2];
        	for(Vector2D p : blob.boundary_points){
        		if ( Vector2D.distance(p, diag0[0])<5 || Vector2D.distance(p, diag0[1])<5)
        			continue;
        		for(Vector2D q : blob.boundary_points){
            		if ( Vector2D.distance(q, diag0[0])<5 || Vector2D.distance(q, diag0[1])<5)
            			continue;
        			double d = (p.x-q.x)*(p.x-q.x)+(p.y-q.y)*(p.y-q.y);
        			if (d > max_d){
        				diag1[0] = p;
        				diag1[1] = q;
        				max_d = d;
        			}
        		}
        	}
        	Vector2D p0 = diag0[0];
        	Vector2D p1 = diag1[0];
        	Vector2D p2 = diag0[1];
        	Vector2D p3 = diag1[1];
        	
        	Vector2D vec0 = new Vector2D(p1.x-p0.x, p1.y-p0.y);
        	Vector2D vec1 = new Vector2D(p2.x-p0.x, p2.y-p0.y);
        	if (Vector2D.cross_product(vec0, vec1)<0){
        		p1 = diag1[1];
        		p3 = diag1[0];
        	}
        	
        	// bounds
        	blob.p0 = p0;
        	blob.p1 = p1;
        	blob.p2 = p2;
        	blob.p3 = p3;
        	
        	double h_length = (Vector2D.distance(p0,p1)+Vector2D.distance(p2,p3) )/2;
        	double v_length = (Vector2D.distance(p1,p2)+Vector2D.distance(p3,p0) )/2;
        	
        	
        	if (h_length/v_length < MIN_WH_RATIO || h_length/v_length > MAX_WH_RATIO)
        		return null;
        	
        	Vector2D h_vec = Vector2D.mid_point(new Vector2D(p0,p1), new Vector2D(p3,p2));
        	Vector2D v_vec = Vector2D.mid_point(new Vector2D(p0,p3), new Vector2D(p1,p2));
        	double area = Math.abs(Vector2D.cross_product(h_vec, v_vec));
        	if (area < MIN_AREA)
        		return null;
        	return blob;
	}
	
	static void decode_blob(Blob blob, int[] pixels, int w, int h){
        	//
        	// recognize code pattern
        	//
           	Vector2D[][] inner_points = new Vector2D[3][3];
           	int[][] data = new int[3][3];
        	for(int x=0; x<3; x++){
        		Vector2D p01 = Vector2D.interpolate(blob.p0, blob.p1, 1.0*(1+x)/4);
        		Vector2D p32 = Vector2D.interpolate(blob.p3, blob.p2, 1.0*(1+x)/4);
            	for(int y=0; y<3; y++){
            		Vector2D q = Vector2D.interpolate(p01, p32, 1.0*(1+y)/4);            		
            		inner_points[x][y] = q;
            		data[x][y] = is_black(q, pixels, w) ? 1 : 0;
            	}
        	}
        	blob.data = data;
        	blob.inner_points = inner_points;

        	
        	int[] id_and_orientation = Pattern.get_id_and_orientation(data);
        	blob.id = id_and_orientation[0];
        	
        	blob.orientation = new Vector2D(Vector2D.mid_point(blob.p2,blob.p3), Vector2D.mid_point(blob.p0,blob.p1));
        	blob.orientation = Vector2D.rotate(blob.orientation, 90*id_and_orientation[1]);
        	blob.orientation.normalize();
        
        	blob.position = Vector2D.mid_point( Vector2D.mid_point(blob.p0,blob.p1), Vector2D.mid_point(blob.p2,blob.p3));

	}

	
	

	//
	//  compute bounding box of the blob.
	//  return Blob object when the size is acceptable
	//
	static Blob check_size_and_get_blob(int x, int y, int w, int[] pixels, int[] type){
		//
		// propagate
		//
		ArrayList<Point> ps = new ArrayList<Point>();
		grow(x, y, w, pixels, type, ps);
		//
		// check bounds
		//
		Point min_x_p = null;
		Point max_x_p = null;
		Point min_y_p = null;
		Point max_y_p = null;
		int min_x = Integer.MAX_VALUE;
		int max_x = -Integer.MAX_VALUE;
		int min_y = Integer.MAX_VALUE;
		int max_y = -Integer.MAX_VALUE;		
		for(Point p : ps){
			if (p.x < min_x){
				min_x = p.x;
				min_x_p = p;
			}
			if (p.x > max_x){
				max_x = p.x;
				max_x_p = p;
			}
			if (p.y < min_y){
				min_y = p.y;
				min_y_p = p;
			}
			if (p.y > max_y){
				max_y = p.y;
				max_y_p = p;
			}			
		}		
		
		//
		// check size
		//
		if (max_x - min_x < MIN_BLOB_SIZE || max_y-min_y < MIN_BLOB_SIZE)
			return null;
		if (max_x - min_x > MAX_BLOB_SIZE || max_y-min_y > MAX_BLOB_SIZE)
			return null;
		
		//
		// development
		//
		for(Point p : ps){
			type[p.x+p.y*w] = BLOB;
		}

		return new Blob(ps, min_x_p, max_x_p, min_y_p, max_y_p);
	}
	//
	// propagate
	//
	static void grow(int x, int y, int w, int[] pixels, int[] type, ArrayList<Point> ps){
		if (type[y*w+x] != GOOD)
			return;
		
		type[y*w+x] = DEAD;
		
		Point p = new Point(x, y);
		ps.add(p);
		
		grow(x-1, y, w, pixels, type, ps);
		grow(x+1, y, w, pixels, type, ps);
		grow(x, y-1, w, pixels, type, ps);
		grow(x, y+1, w, pixels, type, ps);
		
		grow(x-1, y-1, w, pixels, type, ps);
		grow(x+1, y-1, w, pixels, type, ps);
		grow(x-1, y+1, w, pixels, type, ps);
		grow(x+1, y+1, w, pixels, type, ps);
	}
	

	
	
	
	//
	// utilities
	//
	static int[] rgb(int pixel) {
        //int alpha = (pixel >> 24) & 0xff;
        int red   = (pixel >> 16) & 0xff;
        int green = (pixel >>  8) & 0xff;
        int blue  = (pixel      ) & 0xff;
        int[] rgb = {red, green, blue};
        return rgb;
	}
	static int from_rgb(int red, int green, int blue){
		return (255 << 24) | (red << 16) | (green << 8) | blue;
	}
	
	static boolean is_black(int pixel){
        int red   = (pixel >> 16) & 0xff;
        return (red < 128); 
	}
	// weighted examination
	static boolean is_black(Vector2D v, int[] pixels, int w){	//, ArrayList<Point> debug){
		double total_value = 0;
		double total_weight = 0;
		for(int x=0; x<2; x++){
			for(int y=0; y<2; y++){
				Point p = new Point ((int)(v.x+x), (int)(v.y+y));
				double d = Vector2D.distance(v, p);
			
				double value = (is_black(pixels[p.y*w+p.x])) ? 1 : 0;
				double weight = Math.max(0, (1-d));
			
				total_value += value*weight;
				total_weight += weight;
				
				//debug.add(p);
			}
		}
		return (total_value / total_weight > 0.5);
	}
	
	
	
	
	
	//
	// for development only
	//
	static ArrayList<Blob> blobs = new ArrayList<Blob>();
	//static Image image;
	static Image segmented_image;
	static boolean DEBUG = false;
	static boolean SHOW_SEGMENTATION_RESULT = false;
	static void paint(Graphics g){
		//if (image == null)
		//	image = Main.loadImage(IMAGE_FILE_NAME);
		Image image = VisionClient.image;
		if (image == null)
			return;
		
		int w = image.getWidth(null);
		int h = image.getHeight(null);
	
		int s = DEBUG ? 8 : 1;
		
		g.drawImage(image, 0,0,w*s,h*s,0,0,w,h, null);
		
		// segmentation result
		if (SHOW_SEGMENTATION_RESULT  && segmented_image != null)
			g.drawImage(segmented_image, 0,0,w*s,h*s,0,0,w,h,  null);
		
		for(Blob blob : blobs){

			g.setColor(Color.red);

			g.drawLine((int)(s*(0.5+blob.p0.x)), (int)(s*(0.5+blob.p0.y)), (int)(s*(0.5+blob.p1.x)), (int)(s*(0.5+blob.p1.y)));
			g.drawLine((int)(s*(0.5+blob.p1.x)), (int)(s*(0.5+blob.p1.y)), (int)(s*(0.5+blob.p2.x)), (int)(s*(0.5+blob.p2.y)));
			g.drawLine((int)(s*(0.5+blob.p2.x)), (int)(s*(0.5+blob.p2.y)), (int)(s*(0.5+blob.p3.x)), (int)(s*(0.5+blob.p3.y)));
			g.drawLine((int)(s*(0.5+blob.p3.x)), (int)(s*(0.5+blob.p3.y)), (int)(s*(0.5+blob.p0.x)), (int)(s*(0.5+blob.p0.y)));
			
			int r = 2;

			if (DEBUG){
				// show 3x3 points
				for(int x=0; x<3; x++)
					for(int y=0; y<3; y++){
						Vector2D p = blob.inner_points[x][y];
						if (blob.data[x][y] == 1)
							g.fillOval((int)(s*(0.5+p.x)-r), (int)(s*(0.5+p.y)-r), 2*r, 2*r);
						else
							g.drawOval((int)(s*(0.5+p.x)-r), (int)(s*(0.5+p.y)-r), 2*r, 2*r);
					}
				
//				// show 2x2 around each point
//				g.setColor(Color.green);
//				for(Point p : blob.debug){
//					g.fillOval((int)(s*(0.5+p.x)-r), (int)(s*(0.5+p.y)-r), 2*r, 2*r);
//				}
			}
			
			// show id and orientation
			g.setColor(Color.green);
			g.drawString(""+blob.id, (int)(s*(0.5+blob.position.x)), -20+(int)(s*(0.5+blob.position.y)));
			
			Vector2D v0 = blob.position;
			Vector2D v1 = Vector2D.add(v0, Vector2D.multiply(blob.orientation, 20));
			g.setColor(Color.red);
			g.drawLine((int)(s*(0.5+v0.x)), (int)(s*(0.5+v0.y)), (int)(s*(0.5+v1.x)), (int)(s*(0.5+v1.y)));
			
			
		}
	}
	//
	// for development
	//
	static void render_segmentation_result(int[] type, int w, int h){
        
        int[] new_pixels = new int[w * h];
        for(int i=0; i<w*h; i++){
        	if (type[i] == NONE) 
        		new_pixels[i] = from_rgb(0,0,0);
        	else if (type[i] == DEAD)
        		new_pixels[i] = from_rgb(255,0,255);
        	else if (type[i] == GOOD)
        		new_pixels[i] = from_rgb(0,255,255);
        	else if (type[i] == BLOB)
        		new_pixels[i] = from_rgb(255,0,0);
        }
        
		ImageProducer ip = new MemoryImageSource(
				w , h , new_pixels , 0 , w);
		segmented_image = Main.f.createImage(ip);
	}
	
		
	
	static void detect(Image image){

		int w = image.getWidth(null);
		int h = image.getHeight(null);
		
        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(image, 0,0, w, h, pixels, 0, w);
        
        try {
        	pg.grabPixels();
        } catch (Exception e) {
        	e.printStackTrace();
        	return;
        }
        
        blobs = detect_main(pixels, w, h);
	}	
	static ArrayList<Blob> detect_video(int[] pixels){
     
        return detect_main(pixels, VisionClient.WIDTH, VisionClient.HEIGHT);
	}	
}


//
// represents a collection of black pixels with appropriate size
//
class Blob {

	// final result
	int id;
	Vector2D position;
	Vector2D orientation;

	
	// intermediate data
	Point min_x_p, min_y_p, max_x_p, max_y_p;	// bounding box
	ArrayList<Point> points;					// pixels
	ArrayList<Vector2D> boundary_points;		// points on boundary edges
	Vector2D p0,p1,p2,p3;						// corners
	Vector2D[][] inner_points;					// bit locations
	int[][] data;								// decode result

	Blob(ArrayList<Point> points,Point min_x_p, Point max_x_p, Point min_y_p, Point max_y_p){
		this.points = points;
		this.min_x_p = min_x_p;
		this.max_x_p = max_x_p;
		this.min_y_p = min_y_p;
		this.max_y_p = max_y_p;
	}
}
