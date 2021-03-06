package exc3;

import org.jacop.constraints.LinearInt;
import org.jacop.constraints.SumWeight;
import org.jacop.constraints.XeqY;
import org.jacop.constraints.XplusYeqC;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;

public class GoodBurger {
	public static void main(String[] args) {
		long T1, T2, T;
		T1 = System.currentTimeMillis();
		burger();
		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t*** Execution time = " + T + " ms");
	}

	@SuppressWarnings({ "deprecation" })
	private static void burger() {

		Store store = new Store();

		IntVar beef = new IntVar(store, "beef", 1, 5);
		IntVar bun = new IntVar(store, "bun", 1, 5);
		IntVar cheese = new IntVar(store, "cheese", 1, 5);
		IntVar onions = new IntVar(store, "onion", 1, 5);
		IntVar pickles = new IntVar(store, "pickle", 1, 5);
		IntVar lettuce = new IntVar(store, "lettuce", 1, 5);
		IntVar ketchup = new IntVar(store, "ketchup", 1, 5);
		IntVar tomato = new IntVar(store, "tomato", 1, 5);

		int[] sodium = new int[] { 50, 330, 310, 1, 260, 3, 160, 3 };
		int[] fat = new int[] { 17, 9, 6, 2, 0, 0, 0, 0 };
		int[] calories = new int[] { 220, 260, 70, 10, 5, 4, 20, 9 };
		int[] price = new int[] { 25, 15, 10, 9, 3, 4, 2, 4 };

		IntVar ingredients[] = { beef, bun, cheese, onions, pickles, lettuce, ketchup, tomato };

		store.impose(new LinearInt(store, ingredients, sodium, "<=", 3000));
		store.impose(new LinearInt(store, ingredients, fat, "<=", 150));
		store.impose(new LinearInt(store, ingredients, calories, "<=", 3000));

		IntVar cost = new IntVar(store, "cost", 0, 500);
		IntVar minusCost = new IntVar(store, "-cost", -500, 0);
		store.impose(new XplusYeqC(minusCost, cost, 0));
		store.impose(new SumWeight(ingredients, price, cost));

		store.impose(new XeqY(ketchup, lettuce));
		store.impose(new XeqY(pickles, tomato));

		System.out.println(
				"Number of variables: " + store.size() + "\nNumber of constraints: " + store.numberConstraints());

		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(ingredients, null, new IndomainMin<IntVar>());
		search.setSolutionListener(new PrintOutListener<IntVar>());
		boolean Result = search.labeling(store, select, minusCost);
		if (Result) {
			System.out.println("\n*** Yes");
			System.out.println("Solution : " + java.util.Arrays.asList(ingredients));
		} else
			System.out.println("\n*** No");
		int count = 0;
		int p = 0;
		int cal = 0;
		int fa = 0;
		int sod = 0;
		int nbr = 0;
		for (IntVar ing : ingredients) {
			cal += ing.value() * calories[count];
			fa += ing.value() * fat[count];
			sod += ing.value() * sodium[count];
			p += ing.value() * price[count];
			nbr += ing.value();
			count++;
		}
		p = Math.abs(p);
		System.out.println("Price: $" + (double) p * 0.01);
		System.out.println("Sodium: " + sod + " mg");
		System.out.println("Fat: " + fa + " g");
		System.out.println("Calories: " + cal);
		System.out.println("Ingredients: " + nbr);

	}
}
