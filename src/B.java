
public class B extends Dataset{
	
	public B(int size) {
		for (int i=0; i<size; i++) {
			this.add(Point.random());
		}
		this.name = "B";
	}
	
	public static void main(String args[]) throws InterruptedException {
		Dataset set = new B(100);
		set.sweep();
		set.show();
		Thread.sleep(2000);
		set.mbc();
		set.show();
	}
	
}
