import java.util.Random;

public class Point implements Comparable<Point> {
	public double x;
	public double y;
	public static Random random = new Random();
	
	public Point(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	/**
	 * prints x:y
	 * useful for printing data in a text file
	 */
	public String toString() {
		return this.x+":"+this.y;
	}
	
	public int compareTo(Point __o) {
		if (this.x>__o.x) {
			return 1;
		} else if (this.x<__o.x) {
			return -1;
		} else {
			return 0; //on compare jamais y
		}
	}
	
	/**
	 * used for inverting a dataset (and computing lower hull)
	 * @return (-x,-y)
	 */
	public Point inverted() {
		return new Point(-this.x,-this.y);
	}
	
	/**
	 * useful function for dataset C
	 * @return square of the distance between the point and the origin
	 */
	public double norm() {
		return this.x*this.x+this.y*this.y;
	}
	
	/**
	 * @return a random point of coordinates x,y, with 0<x,y<1 chosen uniformly
	 */
	public static Point random() {
		return new Point(Point.random.nextDouble(),Point.random.nextDouble());
	}
	
	public static Point middle(Point a, Point b) {
		return new Point((a.x+b.x)/2,(a.y+b.y)/2);
	}
	
	
}


