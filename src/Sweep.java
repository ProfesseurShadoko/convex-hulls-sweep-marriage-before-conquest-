import java.util.ArrayList;

public class Sweep {

	public static void run(Dataset dataset) {
		
		ArrayList<Point> hull_up = Sweep.up(dataset);
		ArrayList<Point> hull_down = (new Dataset(Sweep.up(dataset.inverted()))).inverted().points;
		
		hull_up.remove(hull_up.size()-1);
		for (int i=0; i<hull_down.size()-1; i++) {
			hull_up.add(hull_down.get(i)); //adding them all except the last one, that is the first one of hull_up
		}		
		dataset.sweep=hull_up;
	}
	
	public static ArrayList<Point> up(Dataset dataset) {
		dataset.sortByX();
		ArrayList<Point> hull_up = new ArrayList<Point>();
		int i=0; //swepp index
		Point a,b,c;
		
		while (true) {
			c=dataset.get(i);
			if (hull_up.size()<2) {
				hull_up.add(c);
				i++;
			} else {
				a = hull_up.get(hull_up.size()-2);
				b = hull_up.get(hull_up.size()-1);
				
				if (Dataset.isClockwise(a, b, c)) {
					hull_up.add(c);
					i++;
				} else {
					hull_up.remove(hull_up.size()-1);
				}
			}
			if (i==dataset.size()) {
				//we have added all the points, algorithm is over !
				break;
			}
		}
		return hull_up;
	}
}
