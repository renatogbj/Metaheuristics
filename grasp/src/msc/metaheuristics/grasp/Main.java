package msc.metaheuristics.grasp;

import java.util.List;

public class Main {

	private static final int NUM_CITIES = 200;
	
	public static void main(String[] args) {
		TravellingSalesman problem = new TravellingSalesman(NUM_CITIES);
		
		Grasp search = new Grasp(problem);
		Integer[] solution = search.find();
		print(search.costs, problem.f(solution));
		
	}
	
	private static void print(List<Integer> costs, int finalCost) {
		System.out.println("Final cost: " + finalCost);
		
		for (Integer cost : costs) {
			System.out.print(cost + ", ");
		}
		
		System.out.println("\n");
	}
}
