package msc.metaheuristicas.tabu;

public class Main {

	public static final int NUM_CITIES = 1000;
	
	public static void buildGraph2(TravellingSalesman problem) {
		for (int i = 0; i < NUM_CITIES; i++) {
			for (int j = i + 1; j < NUM_CITIES; j++) {
				int dist = (int) ((Math.random() * 20) + 1);
				problem.setDistance(i, j, dist);
			}
		}
	}

	public static void main(String[] args) {

		TravellingSalesman problem = new TravellingSalesman(NUM_CITIES);
		buildGraph2(problem);
		
		TabuSearch search = new TabuSearch(problem);
		int[] solution = search.find();
		
		System.out.println("Final cost: " + problem.f(solution));
		
		for (Integer cost : search.costs) {
			System.out.print(cost + ", ");
		}
	}
}
