package lab1;

import org.jacop.core.IntVar;
import org.jacop.core.Store;

public class Pizza {
	public static void main(String[] args) {
		long T1, T2, T;
		int n = 4;
		int[] price = { 10, 5, 20, 15 };
		int m = 2;
		int[] buy = { 1, 2 };
		int[] free = { 1, 1 };
		T1 = System.currentTimeMillis();
		minCost(n, price, m, buy, free);
		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t*** Execution time = " + T + " ms");
	}

	private static void minCost(int n, int[] price, int m, int[] buy, int[] free) {

		sort(price);
		for (int i = 0; i < free.length; i++)
			if (free[i] == 0)
				buy[i] = 0;
		sort(buy, free);

		Store store = new Store();
		IntVar[] pizzas = new IntVar[price.length];

		// System.out.println(
		// "Number of variables: " + store.size() + "\nNumber of constraints: "
		// + store.numberConstraints());
		//
		// Search<IntVar> search = new DepthFirstSearch<IntVar>();
		// SelectChoicePoint<IntVar> select = new
		// SimpleSelect<IntVar>(ingredients, null, new IndomainMin<IntVar>());
		// search.setSolutionListener(new PrintOutListener<IntVar>());
		// boolean Result = search.labeling(store, select, minusCost);
		// if (Result) {
		// System.out.println("\n*** Yes");
		// System.out.println("Solution : " +
		// java.util.Arrays.asList(ingredients));
		// } else
		// System.out.println("\n*** No");
	}

	private static void sort(int[] a) {
		int temp;
		for (int i = 0; i < a.length; i++)
			for (int j = 1; j < a.length - i; j++)
				if (a[j - 1] > a[j]) {
					temp = a[j];
					a[j] = a[j - 1];
					a[j - 1] = temp;
				}
	}

	private static void sort(int[] a, int[] b) {
		int temp;
		for (int i = 0; i < a.length; i++)
			for (int j = 1; j < a.length - i; j++)
				if (a[j - 1] > a[j]) {
					temp = a[j];
					a[j] = a[j - 1];
					a[j - 1] = temp;

					temp = b[j];
					b[j] = b[j - 1];
					b[j - 1] = temp;
				}
	}
}
