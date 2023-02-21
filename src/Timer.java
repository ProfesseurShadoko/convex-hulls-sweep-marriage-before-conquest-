
//usefull for timing functions (using System.nanoTime)
public class Timer {
	
	private Long start=null;
	private Long stop=null;
	private String name="Timer";

	public Timer() {
		
	}
	
	
	public void start() {
		this.stop=null;
		this.start = System.nanoTime();
	}
	public void start(String name) {
		this.stop=null;
		this.name = name;
		this.start = System.nanoTime();
	}
	
	public void stop() {
		assert this.start!=null;
		this.stop = System.nanoTime();
	}
	
	public void reset() {
		this.start = null;
		this.stop=null;
	}
	
	public boolean isRunning() {
		return (this.start!=null && this.stop==null);
	}
	
	public Long ns() {
		if (this.isRunning()) {
			return (System.nanoTime() - this.start);
		} else {
			return this.stop-this.start;
		}
	}
	
	public double ms() {
		return (double)this.ns()/1_000_000;
	}
	
	public String toString() {
		if (this.start == null) {
			return this.name+" silent...";
		}
		if (this.stop == null) {
			return this.name+" running...";
		}
		return this.name+" : "+this.ms()+"ms";
	}
	
	public void print() {
		System.out.println(this);
	}
}
