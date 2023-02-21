import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Median {
		
		public static Point sort_n_get(ArrayList<Point> points) {
			if (points.size()%2==0) {
				return sort_n_select(points,points.size()/2);
			} else {
				return sort_n_select(points,points.size()/2+1);
			}
		}
		
		private static Point sort_n_select(ArrayList<Point> points,int rank) {
			Collections.sort(points);
			return points.get(rank-1);
		}
		
		public static Point quickMedian(ArrayList<Point> points) {
			if (points.size()%2==0) {
				return quickSelect(new MedianInstance(points),points.size()/2);
			} else {
				return quickSelect(new MedianInstance(points),points.size()/2+1);
			}
		}
		
		private static Point quickSelect(MedianInstance mi,int rank) {
			
			if (mi.size()<=5) {
				return sort_n_select(mi.toList(),rank);
			}
			
			
			Point pivot = findPivot(mi,rank);
			int index = mi.split(pivot);
			
			if (index==rank-1) {
				return mi.get(index);
			} else if (index>rank-1){
				return quickSelect(new MedianInstance(mi,0,index),rank);
			} else {
				return quickSelect(new MedianInstance(mi,index+1,mi.size()),rank-index-1);
			}
		}
		
		private static Point findPivot(MedianInstance mi) {
			//return medianOfMedians(mi);
			return mi.get(0);
		}
		
		private static Point findPivot(MedianInstance mi,int rank) {
			//return medianOfMedians(mi);
			//return mi.get(0);
			//return mi.get((int)(Point.random().x*mi.size()));
			if (rank==0) {
				rank++;
			}
			return mi.get(rank-1); //performs better when trying to find 2 medians after removing just one element (cf MBC.median)
			//if the list is already "sorted", this will return the correct median the first time
		}
		
		public static Point medianOfMedians(MedianInstance mi) {
			ArrayList<Point> medians = new ArrayList<Point>();
			ArrayList<Point> group;
			for (int i=0; i<mi.size()/5; i++) {
				group = new ArrayList<Point>();
				for (int j=0; j<5; j++) {
					group.add(mi.get(5*i+j));
				}
				medians.add(quickMedian(group));
			}
			return quickMedian(medians);
			
		}
		
		public static void test(int N) {
			ArrayList<Point> set = (new A(N)).points;
			ArrayList<Point> copy = new ArrayList<Point>();
			for (Point point:set) {copy.add(point);}
			assert quickMedian(set)==sort_n_get(copy);
		}
		
		
		public static void main(String args[]) throws IOException{
			
			test(1000);
			test(1001);
			
			int N,AVG;
			Timer timer = new Timer();
			Dataset[] sets;
			
			N=100_000;
			AVG=100;
			
			sets = Dataset.arrayB(AVG,N);
			
			timer.start();
			for (Dataset set:sets) {
				Median.quickMedian(set.points);
			}
			timer.stop();
			System.out.println("<MEDIAN> Average execution time (N="+N+") : "+String.format("%.4f",timer.ms()/AVG)+"ms");
			
			sets = Dataset.arrayB(AVG,N);
			
			timer.start();
			for (Dataset set:sets) {
				set.sortByX();
			}
			timer.stop();
			System.out.println("<SORT>   Average execution time (N="+N+") : "+String.format("%.4f",timer.ms()/AVG)+"ms");
			
		}
}


/**
 * allows list slicing
 */
class MedianInstance {
	private ArrayList<Point> points;
	private int bottom,top;
	
	public MedianInstance(ArrayList<Point>points) {
		this.points = points;
		this.bottom=0;
		this.top=this.points.size();		
	}
	
	public MedianInstance(MedianInstance mi, int bottom, int top) {
		this.points = mi.points;
		this.bottom = mi.bottom+bottom;
		this.top = mi.bottom+top;
	}
	
	public int size() {
		return top-bottom;
	}
	
	public Point get(int i) {
		//assert((i<this.size()),"IndexOutOfBounds");
		return this.points.get(i+this.bottom);
	}
	
	public Point set(int i,Point value) {
		return this.points.set(i+this.bottom,value);
	}
	
	public void swap(int i,int j) {
		Point tmp = this.get(i);
		this.set(i, this.get(j));
		this.set(j, tmp);
	}
	
	public int split(Point pivot) {
		int limit = 0;
		Point curr;
		for (int i=0; i<this.size(); i++) {
			curr = this.get(i);
			if (curr == pivot) {
				this.swap(i,this.size()-1);
				curr = this.get(i);
			}
			if (curr.compareTo(pivot)<0) {
				this.swap(limit, i);
				limit++;
			}
		}
		this.swap(limit, this.size()-1);
		return limit;
	}
	
	public ArrayList<Point> toList() {
		ArrayList<Point> out = new ArrayList<Point>();
		for (int i = 0; i<this.size(); i++) {
			out.add(this.get(i));
		}
		return out;
	}
	
	public void print() {
		System.out.println("MedianInstance <size="+this.size()+"> : bottom="+this.bottom+" & top="+this.top);
	}
}
