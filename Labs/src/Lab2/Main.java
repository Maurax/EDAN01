package logistics;

import java.util.ArrayList;

import org.jacop.constraints.Constraint;
import org.jacop.constraints.Element;
import org.jacop.constraints.IfThenElse;
import org.jacop.constraints.Or;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.constraints.Subcircuit;
import org.jacop.constraints.Sum;
import org.jacop.constraints.XeqC;
import org.jacop.constraints.XeqY;
import org.jacop.constraints.XneqC;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleMatrixSelect;

public class Main3 {

	public static void main(String[] args) {
		long T1, T2, T;
		T1 = System.currentTimeMillis();
		logistics(new Data3());
		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t***	Execution time = " + T + " ms");
	}

	public static void logistics(Data data) {
		Store store = new Store();

		int n_edges = data.n_edges + data.graph_size;
		int graph_size = data.graph_size + 1;
		int[] from = data.from;
		int[] to = data.to;
		int[] cost = data.cost;
		int start = data.start;
		int n_dests = data.n_dests;
		int dest[] = data.dest;

		int[][] distances = new int[graph_size][graph_size];
		for (int i = 0; i < graph_size; i++)
			for (int j = 0; j < graph_size; j++) {
				distances[i][j] = 100;
				if (i == j)
					distances[i][j] = 0;
			}

		for (int i = 0; i < from.length; i++) {
			distances[from[i]][to[i]] = cost[i];
			distances[to[i]][from[i]] = cost[i];
		}
		for (int i = 0; i < n_dests; i++)
			distances[dest[i]][start] = 0;

		int[] cost2 = new int[graph_size * graph_size];
		for (int i = 0; i < graph_size; i++)
			for (int j = 0; j < graph_size; j++)
				cost2[j + i * graph_size] = distances[i][j];
				
		
		IntVar[][] x = new IntVar[n_dests][graph_size];
		for (int i = 0; i < n_dests; i++)
			for (int j = 0; j < graph_size; j++)
				x[i][j] = new IntVar(store, "x" + i + " " + j, 0, graph_size);

		/*alla måste starta i start och sluta i sin egen dest*/
		for (int i = 0; i < x.length; i++) {
			store.impose(new Subcircuit(x[i]));
			store.impose(new XneqC(x[i][start], start + 1));
			store.impose(new XneqC(x[i][dest[i]], dest[i] + 1));
		}

		IntVar[][] d = new IntVar[graph_size][n_dests];
		for (int i = 0; i < graph_size; i++)
			for (int j = 0; j < n_dests; j++) {
				d[i][j] = new IntVar(store, "dist" + j, 0, 1000);
				IntVar v = new IntVar(store, 0, 100);
				store.impose(new Element(x[j][i], distances[i], v));
				//store.impose(new XeqY(d[i][j], v));
				
				ArrayList<PrimitiveConstraint> s = new ArrayList<PrimitiveConstraint>();
				for(int k=j+1;k<n_dests;k++){
						s.add(new XeqY(x[j][i], x[k][i]));
				}
				store.impose(new IfThenElse(new Or(s), new XeqC(d[i][j], 0), new XeqY(d[i][j], v)));
			}

//		for (int i = 0; i < graph_size; i++)
//			for (int j = 0; j < n_dests; j++) {
//				IntVar v = new IntVar(store, 0, 100);
//				store.impose(new Element(d[i][j], distances[i], v));
//			}
			
		
		IntVar[] d2 = new IntVar[graph_size];
		for (int i = 0; i < graph_size; i++) {
			d2[i] = new IntVar(store, 0, 100);
			IntVar sum = new IntVar(store, 0, 100);

			store.impose(new Sum(d[i], sum));
		}

		IntVar distance[] = new IntVar[d.length];
		for (int i = 0; i < distance.length; i++)

		{
			distance[i] = new IntVar(store, 0, 1000);
			Constraint distanceCost = new Sum(d[i], distance[i]);
			store.impose(distanceCost);
		}

		IntVar distance2 = new IntVar(store, 0, 1000);
		store.impose(new Sum(distance, distance2));
		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleMatrixSelect<IntVar>(x, new IndomainMin<IntVar>());
		// SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(distance,
		// null, new IndomainMin<IntVar>());

		search.setSolutionListener(new PrintOutListener<IntVar>());
		boolean Result = search.labeling(store, select, distance2);
		if (Result)

		{
			System.out.println(store);
			System.out.println("\n***Yes");
			System.out.println("Price: " + distance2.value());
			System.out.println("\nFrom \tTo");
			for(int j=0;j<n_dests;j++){
			for (int i = 0; i < graph_size; i++)
				System.out.println(i + "\t" + (x[j][i].value() - 1));
			System.out.println();
			}
		}

		System.out.println("\n");
		for (

		int i = 0; i < d.length; i++)

		{
			for (int j = 0; j < d[0].length; j++)
				System.out.print(d[i][j].value() + "\t");
			System.out.println();
		}

	}
}
