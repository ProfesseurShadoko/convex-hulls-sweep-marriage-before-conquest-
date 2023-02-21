
public class C extends Dataset {
	
	public C(int size) {
		Point point;
		double x;
		double y;
		for (int i=0; i<size; i++) {
			while (true) {
				//getting uniform distribution on [-1,1]²
				point = Point.random();
				x = 2*(point.x-0.5);
				y = 2*(point.y-0.5);
				
				//adding point only if it's norm is less than 1
				point = new Point(x,y);
				if (point.norm()<1) {
					this.add(point);
					break;
				}
			}
		}
		this.name = "C";
	}
	
	public static void main(String args[]) throws InterruptedException {
		Dataset set = new C(100);
		set.sweep();
		set.show();
		Thread.sleep(2000);
		set.mbc();
		set.show();
	}
}
