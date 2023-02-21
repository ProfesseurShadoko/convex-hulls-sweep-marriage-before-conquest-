
import java.util.*;


public class Dataset {
	public ArrayList<Point> points = new ArrayList<Point>();
	public ArrayList<Point> sweep = new ArrayList<Point>();
	public ArrayList<Point> main =new ArrayList<Point>();
	public String name="default";
	
	/**
	 * @return empty dataset
	 */
	public Dataset() {
		
	}
	
	/**
	 * @param data (set of points)
	 * @return dataset containing the points
	 */
	public Dataset(ArrayList<Point> data) {
		this.points = data;
	}
	
	/**
	 * Sorts the list by x-index (for sweep algorithm). In place modification.
	 */
	public void sortByX() {
		Collections.sort(this.points);
	}
	
	public Point get(int index) {
		return this.points.get(index);
	}
	
	public void add(Point point) {
		this.points.add(point);
	}
	
	public int size() {
		return this.points.size();
	}
	
	public String toString() {
		String out = "";
		for (Point point: this.points) {
			out=out+point+"\n";
		}
		return out;
	}
	
	/**
	 * @return new dataset of all the inverted points (x,y)->(-x,-y)
	 */
	public Dataset inverted() {
		Dataset dataset = new Dataset();
		for (Point point : this.points) {
			dataset.add(point.inverted());
		}
		for (Point point : this.sweep) {
			dataset.sweep.add(point.inverted());
		}
		for (Point point : this.main) {
			dataset.main.add(point.inverted());
		}
		return dataset;
	}
	
	/**
	 * sweep algorithm
	 */
	public void sweep() {
		Sweep.run(this);
	}
	
	/**
	 * marriageBeforeConquest
	 */
	public void mbc(){
		MBC.run(this);
	}
	
	/**
	 * in place modification
	 */
	public void shuffle() {
		Collections.shuffle(this.points);
	}
	
	public Dataset copy() {
		return this.inverted().inverted();
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return (bool)(area(triangle(a,b,c)) < 0)
	 */
	public static boolean isClockwise(Point a, Point b, Point c) {
		double area = a.x*b.y + b.x*c.y + c.x*a.y - b.x*a.y - c.x*b.y - a.x*c.y;
		return area<0;
	}
	
	/**
	 * @param points
	 * @return median of a set of points in linear time
	 */
	public static Point xMedian(ArrayList<Point> points) {
		return Median.quickMedian(points);
	}
	
	/**
	 * @param points
	 * @return median of a set of points by sorting it
	 */
	public static Point xMedian_sort(ArrayList<Point> points) {
		Collections.sort(points);
		return points.get(points.size()/2);
	}
	
	/**
	 * for debug
	 */
	public String scatterTitle() {
		return "Dataset <"+this.name+"> : size = "+this.size();
	}
	
	/**
	 * for debug
	 */
	public void show() {
		new Scatter(this);
	}
	
	/**
	 * for debug : return [new B(datasetSize)]*length
	 */
	public static Dataset[] arrayB(int length, int datasetSize) {
		Dataset[] out = new Dataset[length];
		for (int i=0; i<length; i++) {
			out[i]=new B(datasetSize);
		}
		return out;
	}
	
}
