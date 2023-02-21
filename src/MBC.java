import java.util.ArrayList;
import java.util.*;
import java.io.*;

public class MBC {
	
	public static int count; //how offen is recursion applied ? This allows to verify that this number is constant for dataset A, which it is !
	public static Timer timer = new Timer();
	
	/*
	 * the following variables were used to time each step of the function. We get this result:
	 * DATASET A : size=10000k
	 * Sweep : 7286.3265ms
	 * Main : 9025.7008ms
	 * Basis : 3795.0604ms || Median1 : 2859.9152ms || Median2 : 668.7378ms || Split : 763.6382000000001ms
	 */
	public static double median_exe = 0;
	public static double basis_exe = 0;
	public static double split_exe = 0;
	 
	
	
	public static void run(Dataset dataset) {
		count = 0;
		
		median_exe=0;
		basis_exe=0;
		split_exe=0;
		
		
		ArrayList<Point> hull_up = MBC.up(dataset);
		ArrayList<Point> hull_down = (new Dataset(MBC.up(dataset.inverted()))).inverted().points;
		
		//System.out.println("Dataset <"+dataset.size()+"> : count = "+count); //--> always 5 for A
		
		hull_up.remove(hull_up.size()-1);
		for (int i=0; i<hull_down.size()-1; i++) {
			hull_up.add(hull_down.get(i));
		}
		dataset.main=hull_up;
	}
	
	public static ArrayList<Point> up(Dataset dataset){
		return MBC.up(dataset,new ArrayList<Point>());
	}
	
	public static ArrayList<Point> up(Dataset dataset, ArrayList<Point> hull_up){
		count++;
		Point xm;
		Point[] basis;
		Dataset left,right;
		
		
		if (dataset.size()<=2) {
			//if there are only two left, then they are both in the convex hull
			//we add them to the hull in the right order (from left to right)
			dataset.sortByX();
			
			for (Point point:dataset.points) {
				hull_up.add(point);
			}
			return hull_up;
		}
		
		timer.start();
		xm = MBC.median(dataset);
		timer.stop();
		median_exe = median_exe+timer.ms();
		
		timer.start();
		basis = MBC.basisPoints(xm, dataset);
		timer.stop();
		basis_exe=basis_exe+timer.ms();
		
		timer.start();
		left = MBC.split(basis[0], -1, dataset);
		right = MBC.split(basis[1], 1, dataset);
		timer.stop();
		split_exe = split_exe+timer.ms();
		
		MBC.up(left,hull_up);
		MBC.up(right,hull_up);
		return hull_up;
	}
	
	public static Point median(Dataset dataset) {
		
		/*
		 * corrected median algorithm (we do not pick to medians anymore)
		 */
		Point median,closest;
		median = Median.quickMedian(dataset.points);
		
		Point candidate;
		closest = dataset.get(0);
		if (closest==median) {
			closest=dataset.get(1);
		}
		
		for (int i=0; i<dataset.points.size(); i++) {
			candidate = dataset.get(i);
			if (candidate.x==median.x) continue;
			if (closest==null) {
				closest = candidate;
				continue;
			}
			if (Math.abs(closest.x-median.x)>Math.abs(candidate.x-median.x)) {
				closest=candidate;
			}
		}
		return Point.middle(median, closest);		
	}
	
	/**
	 * Splits around the median, returns left half or right half if comparator is -1 or +1
	 */
	public static Dataset split(Point sep,int comparator, Dataset dataset) {
		Dataset out = new Dataset();
		
		for (Point point:dataset.points) {
			if (point.compareTo(sep)*comparator>=0) {
				out.add(point);
			}
		}
		return out;
	}
	
	/**
	 * 
	 * @param sep
	 * @param dataset
	 * @return
	 */
	private static Point[] findFirstBasis(Point sep, Dataset dataset) {
		Point[] best_basis = new Point[2];
		for (Point point: dataset.points) {
			if (point.compareTo(sep)<0) {
				best_basis[0]=point;
			}
			if (point.compareTo(sep)>0) {
				best_basis[1]=point;
			}
			if (best_basis[0]!=null && best_basis[1] != null) {
				break;
			}
		}
		return best_basis;
	}
	
	public static Point[] basisPoints(Point sep,Dataset dataset) {
		Point[] best_basis = new Point[2];
		Point[] basis = new Point[2];
		ArrayList<Point> added = new ArrayList<Point>();
		Collections.shuffle(dataset.points); //we can check that in fact, removing this step slows down the process
		
		//let's find a first valid basis, before searching for the best one !
		best_basis = findFirstBasis(sep,dataset);
		for (Point point: best_basis) {added.add(point);}
		
		
		//checking if it is possible to find a better basis
		for (Point point:dataset.points) {
			if (point.compareTo(sep)<0) {
				basis[0]=point;basis[1]=best_basis[1];
			} else {
				basis[0]=best_basis[0];basis[1]=point;
			}
			
			if (MBC.score(basis, sep)<=MBC.score(best_basis, sep)) {
				//the current basis is better, let's check if the point can be in another basis that can be better
				//let's check if the point is below the segment (and not the not line) of the current best_basis
				//added.add(point);
				
				if (point.compareTo(sep)<0 && point.compareTo(best_basis[0]) <= 0) {
					added.add(point);
				}
				if (point.compareTo(sep)>0 && point.compareTo(best_basis[1]) >= 0) {
					added.add(point);
				}
			} else {
				//it is possible to find a better basis, let's find it by looping over the already added points
				//loop over added point to find best_basis
				for (Point candidate:added) {
					
					if (candidate.compareTo(sep)<0 && point.compareTo(sep)>0) {
						basis[0]=candidate;basis[1]=point;
					} else if (point.compareTo(sep)<0 && candidate.compareTo(sep)>0) {
						basis[0]=point;basis[1]=candidate;
					} else {
						continue;//both point are on the same side of sep, no basis !
					}
					if (MBC.score(basis, sep)>MBC.score(best_basis, sep)) {
						best_basis[0]=basis[0];best_basis[1]=basis[1];
					}
				}
				added.add(point);
			}
		}
		
		return best_basis;
	}
	
	/**
	 * height of the intersection between basis and sep
	 */
	public static double score(Point[] basis, Point sep) {
		Point a=basis[0],b=basis[1];
		return a.y + (sep.x-a.x) * (b.y-a.y)/(b.x-a.x);
	}
	
	public static boolean isBelow(Point[] segment, Point point) {
		Point a=segment[0],b=segment[1];
		
		if (point.x<a.x || point.x>b.x) return false;
		
		return a.y + (point.x-a.x) * (b.y-a.y)/(b.x-a.x) > point.x;
	}
	
	public static void main(String args[]) {
		int N=1_000_000; //100_000_000 --> out of memory
		Timer timer = new Timer();
		
		Dataset dataset = new A(N);
		System.out.println("DATASET A : size="+N/1000+"k");
		
		
		timer.start();
		dataset.sweep();
		timer.stop();
		System.out.println("Sweep : "+timer.ms()+ "ms");
		
		dataset.shuffle();
		
		timer.start();
		dataset.mbc();
		timer.stop();
		System.out.println("Main : "+timer.ms() + "ms");
		
		System.out.println("Basis : " + MBC.basis_exe + "ms || Median : "+MBC.median_exe+"ms || Split : "+MBC.split_exe+"ms");
	}
}
