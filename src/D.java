
import java.lang.Math;

public class D extends Dataset{
	

	public D(int size) {
		for (int i=0; i<size; i++) {
			double teta = Point.random().x*Math.PI*2; //uniform 0 2PI
			this.add(new Point(Math.cos(teta),Math.sin(teta)));
		}
		this.name = "D";
	}
	
	public static void main(String args[]) throws InterruptedException {
		Dataset set = new D(100);
		set.sweep();
		set.show();
		Thread.sleep(2000);
		set.mbc();
		set.show();
	}
 }
