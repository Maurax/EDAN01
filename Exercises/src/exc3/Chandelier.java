package exc3;

import org.jacop.constraints.Alldiff;
import org.jacop.constraints.LinearInt;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;

public class Chandelier {
	public static void main(String[] args) {
		long T1, T2, T;
		T1 = System.currentTimeMillis();
		balance();
		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t*** Execution time = " + T + " ms");
	}

	private static void balance() {
		Store store = new Store();

		IntVar weights[] = new IntVar[9];
		for (int i = 0; i < weights.length; i++)
			weights[i] = new IntVar(store, Character.toString((char) ('a' + i)), 1, 9);

		store.impose(new Alldiff(weights));

		store.impose(new LinearInt(store, weights, new int[] { 3, 3, 3, -2, -2, -2, -3, -3, -3 }, "==", 0));
		store.impose(new LinearInt(store, weights, new int[] { -2, 1, 2, 0, 0, 0, 0, 0, 0 }, "==", 0));
		store.impose(new LinearInt(store, weights, new int[] { 0, 0, 0, 2, 1, -1, 0, 0, 0 }, "==", 0));
		store.impose(new LinearInt(store, weights, new int[] { 0, 0, 0, 0, 0, 0, 2, 1, -3 }, "==", 0));

		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(weights, null, new IndomainMin<IntVar>());

		search.setSolutionListener(new PrintOutListener<IntVar>());

		boolean Result = search.labeling(store, select);

		if (Result) {
			System.out.println("\n*** Yes");
			System.out.println("Solution : " + java.util.Arrays.asList(weights));
		} else
			System.out.println("\n*** No");
	}

}