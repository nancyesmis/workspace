package magiccard;

import java.awt.*;
public class Vector2D {
	
	public double x;
	public double y;

	public Vector2D(Point start, Point end){
		x = end.x -  start.x;
		y = end.y -  start.y;
	}
	public Vector2D(Vector2D start, Vector2D end){
		x = end.x -  start.x;
		y = end.y -  start.y;
	}

	public Vector2D(Point p){
		x = p.x;
		y = p.y;
	}
	public Vector2D(){
	}
	public Vector2D(double _x, double _y){
		x = _x;
		y = _y;
	}
    	
   	public Vector2D(Vector2D v) {
	    x = v.x;	
	    y = v.y;
    }	


	public String toString(){
		//return "("+(int)x+","+(int)y+")";
		//return "("+(int)(x*100)+", "+(int)(y*100)+")";
		return "("+x+", "+y+")";
	}
	
	public Vector2D duplicate(){
		return new Vector2D(x,y);
	}



    public static Vector2D normalize(Vector2D v){
		double l = v.length();
		return new Vector2D(v.x/l, v.y/l);
    }
    public void normalize(){
		double l = length();
		x /= l;
		y /= l;
    }
	
	public void negate(){
		x*=-1;y*=-1;
	}
	public static Vector2D negate(Vector2D v){
		return new Vector2D(-v.x, -v.y);	
	}


	
	
	
    public double length(){
		return Math.sqrt(x * x + y * y);
    }
    
	
	
	
	static public Vector2D add(Vector2D u, Vector2D v){
		return new Vector2D(u.x + v.x, u.y + v.y);
    }
	static public Vector2D add(Vector2D u, Vector2D v, Vector2D w){
		return new Vector2D(u.x + v.x +w.x, u.y + v.y + w.y);
    }
    public void add(Vector2D v){
		x += v.x;	y += v.y;	
    }

	static public Vector2D subtract(Point u, Vector2D v){
		return new Vector2D(u.x - v.x, u.y - v.y);
    }
	static public Vector2D subtract(Vector2D u, Vector2D v){
		return new Vector2D(u.x - v.x, u.y - v.y);
    }
    public void subtract(Vector2D v){
		x -= v.x;	y -= v.y;
    }
	
	
    static public Vector2D multiply(Vector2D v, double m){
		return new Vector2D(v.x * m, v.y * m);
    }
    public void multiply(double m){
		x *= m;	y *= m;	
    }
	
	
	
	
	
	

    static public double dot_product(Vector2D u, Vector2D v){
		return u.x * v.x + u.y * v.y;
    }
	

	
	static public double cos(Vector2D u, Vector2D v){
	  double length = u.length() * v.length();

	  if (length >0)
	    return dot_product(u, v) / length;
	  else
	    return 0;
    }
	
    static public double cross_product(Vector2D u, Vector2D v){
		return u.x * v.y - u.y * v.x;
    }
	
	
    static public double sin(Vector2D u, Vector2D v){
	  double length = u.length() * v.length();

	  if (length >0)
	    return (cross_product(u, v)) / length;
	  else
	    return 0;
    }

	
	
	
   // Žn“_‚ðˆê’v‚³‚¹‚½‚Æ‚«‚Ì‹²Šp‚ð?‚ß‚é?B0-PI Polygon2.set_normal()
    public static double get_angle_PI(Vector2D u, Vector2D v){
		double cosine = cos(u, v);
		if (cosine <= -1) return Math.PI;
		else if (cosine >= 1) return 0;
		else return Math.acos(cosine);
    }	
    public static double get_angle_180(Vector2D u, Vector2D v){
		double cosine = cos(u, v);
		if (cosine <= -1) return 180;
		else if (cosine >= 1) return 0;
		else return Math.acos(cosine)*180/Math.PI;
    }	
	
	
	
	
	/** 0-360 Žn“_‚ðˆê’v‚³‚¹‚½??‡‚Ì‹²Šp*/
	public static double get_angle_360(Vector2D u, Vector2D v){
		if (u.x == v.x && u.y == v.y)
			return 0;
		
		double cos = cos(u, v);
		double sin = sin(u, v);
		if (cos == 0)
			if (sin > 0)	return 90;
			else		return 270;
		if (sin == 0)
			if (cos > 0)	return 0;
			else		return 180;
		
		// -PI/2   ...   +PI/2
		double angle = 180 * Math.atan(sin/cos) / Math.PI;

		if (cos < 0)
			angle = angle + 180;
		if (angle < 0)
			angle = angle + 360;
		return angle;
	}	
	
	// get angel between -180 ... 180
	public double get_angle180(Vector2D v){
		double cos = cos(this,v);
		double sin = sin(this,v);
		if (cos == 0)
			if (sin > 0)	return 90;
			else		return -90;
		if (sin == 0)
			if (cos > 0)	return 0;
			else		return 180;
		
		// -PI/2   ...   +PI/2
		double angle = 180 * Math.atan(sin/cos) / Math.PI;

		if (cos < 0)
			if (angle>0)
				angle -= 180;
			else
				angle +=180;
		return angle;
	}
	
	
	
	
	public static Vector2D rotate90(Vector2D vec){
		return new Vector2D(vec.y, -vec.x);
	}
	
	
	// rotate 0-360
	public static Vector2D rotate(Vector2D v, double degree){
	    if (degree == 90)
		return new Vector2D(-v.y, v.x);
	    else if (degree == 180)
		return new Vector2D(-v.x, -v.y);
	    else if (degree == 270)
		return new Vector2D(v.y, -v.x);

	    
	    double radian = degree * Math.PI / 180.0;
	    double cos = Math.cos(radian);
	    double sin = Math.sin(radian);

	    return  new Vector2D(v.x * cos - v.y * sin,
				v.x * sin + v.y * cos );
	}
	
	
	
	public static Vector2D flip(Vector2D vec, Vector2D axis){
		Vector2D x_vec = Vector2D.normalize(axis);
		
		Vector2D y_vec = Vector2D.rotate90(x_vec);
		double x = Vector2D.dot_product(vec, x_vec);
		double y = Vector2D.dot_product(vec, y_vec);
		y = -y;
		
		return Vector2D.add(Vector2D.multiply(x_vec, x),
							Vector2D.multiply(y_vec, y));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static Vector2D interporate(Vector2D start, Vector2D end, double t){
	    return new Vector2D( start.x * (1-t) + end.x * t, start.y * (1-t) + end.y * t);
	}



	
	
	
	
	
	
	
	
	


	public static double distance(int x1, int y1, int x2, int y2){
	  return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
	}
	public static double distance(Vector2D start, Vector2D end){
		  return distance(start.x, start.y, end.x, end.y);
		}
	public static double distance(Point start, Point end){
	  return distance(start.x, start.y, end.x, end.y);
	}
	public static double distance(double x1, double y1, double x2, double y2){
	  return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
	}
	public static Vector2D interporate(Point start, Point end, double t){
	  return new Vector2D( start.x * (1-t) + end.x * t, start.y * (1-t) + end.y * t);
	}
	public Point point(){
		return new Point((int)x, (int)y);
	}





	static public Point translatePoint(Point p, Vector2D vec){
		return new Point((int)(p.x+vec.x), (int)(p.y+vec.y));
	}
}

