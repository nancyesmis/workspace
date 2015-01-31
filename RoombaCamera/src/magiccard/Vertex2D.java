package magiccard;



import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import java.lang.Math.*;
import java.util.*;


public class Vertex2D extends Vector2D {

	boolean corner = false;

	public double depth = 0;
	
    public Vertex2D() {
    }	

    public Vertex2D(double _x, double _y) {
	x = _x;	
	y = _y;
    }	
    public Vertex2D(double _x, double _y, double _d) {
	x = _x;	
	y = _y;
	depth = _d;
    }	

    public Vertex2D(Point p) {
	x = p.x;	
	y = p.y;
    }	
    public Vertex2D(Vector2D p) {
	x = p.x;	
	y = p.y;
    }	
    public Vertex2D(double[] p) {
	x = p[0];	
	y = p[1];
    }	


    public Point Point(){
	return new Point((int)x, (int)y);
    }



    public Vertex2D copy(){
	Vertex2D v = new Vertex2D(x, y);
	return v;
    }

    public boolean same_position(Vertex2D v){
	return ((x == v.x) && ( y == v.y));
    }
    public static boolean same_position(Vertex2D a, Vertex2D b){
	return ((a.x == b.x) && ( a.y == b.y));
    }
	public static boolean touch(Vertex2D v0, Vertex2D v1){
		return ( Vertex2D.distance(v0, v1)< 0.00001 );// CONST Œë?·‘Î‰?
	}
	
	

    public static Vertex2D mid_point(Vector2D a, Vector2D b){
	return new Vertex2D((a.x+b.x)/2, (a.y+b.y)/2);
    }
    public Vertex2D translate(Vector2D v){
	return new Vertex2D(x+v.x, y+v.y);
    }
    static public Vertex2D translate(Vertex2D v, Vector2D vec){
	return new Vertex2D(v.x+vec.x, v.y+vec.y);
    }
    public void warp(Vector2D v){
	x = v.x;
	y = v.y;
    }
	public void move_relative(Vector2D vec){
		x += vec.x;
		y += vec.y;
	}

	
	
	

	public static double distance(Vertex2D n1, Vertex2D n2){
	  return Math.sqrt((n1.x-n2.x)*(n1.x-n2.x)+(n1.y-n2.y)*(n1.y-n2.y));
	}
	public static double distance(Vertex2D n1, Point n2){
	  return Math.sqrt((n1.x-n2.x)*(n1.x-n2.x)+(n1.y-n2.y)*(n1.y-n2.y));
	}
	public static Vertex2D interpolate(Vertex2D start, Vertex2D end, double t){
	  return new Vertex2D(start.x * (1-t) + end.x * t, start.y * (1-t) + end.y * t);
	}
		

}
