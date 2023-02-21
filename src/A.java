import java.lang.Math;
import java.util.*;

public class A extends Dataset {
	
	
	public A(int size) {
		assert size>=4;
		size = size-4;
		
		//adding the corners
		this.add(new Point(0,0));
		this.add(new Point(0,1));
		this.add(new Point(1,0));
		this.add(new Point(1,1));
		
		//adding the rest of the points
		for (int i=0; i<size; i++) {
			this.add(Point.random());
		}
		
		//rotating dataset
		double teta = Point.random().x*Math.PI*2;
		for (Point point:this.points) {
			double x=point.x, y=point.y;
			point.x = Math.cos(teta)*x+Math.sin(teta)*y;
			point.y = - Math.sin(teta)*x+Math.cos(teta)*y;
		}
		this.name = "A";
		
		//shuffling dataset
		this.shuffle();
	}
	
	public static void main(String args[]) throws InterruptedException {
		Dataset set = new A(100);
		set.sweep();
		set.show();
		Thread.sleep(2000);
		set.mbc();
		set.show();
	}
}
