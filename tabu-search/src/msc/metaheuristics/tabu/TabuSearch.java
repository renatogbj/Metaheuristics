package msc.metaheuristics.tabu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TabuSearch {

	public List<Integer> costs = new ArrayList<>();
	
	private static final int MAX_ITER = 300;
	private static final int MAX_TABU_LIST_SIZE = 100;
	
	private Queue<TabuMove> tabu = new LinkedList<>();
	private TravellingSalesman problem;
	
	public TabuSearch(TravellingSalesman problem) {
		this.problem = problem;
	}
	
	/**
	 * Run the tabu search to find a solution to the TSP.
	 * 
	 * @return an array representing a solution which index 0 is the first city visited,
	 * 			index 1 the second city and so on.
	 */
	public int[] find() {
		// create an initial solution
		int[] current = createInitialSolution();
		int[] best = Arrays.copyOf(current, current.length);
		
		for (int i = 0; i < MAX_ITER; i++) {
			costs.add(problem.f(best));
			
			System.out.println("Current: " + problem.f(current));
			
			// find s' in V*
			current = getBestNeighbour(current);
			
			int currentCost = problem.f(current);
			int bestCost = problem.f(best);
			
			System.out.println("Best Neighbour: " + currentCost);
			System.out.println("Best: " + bestCost);
			System.out.println();
			
			if (currentCost < bestCost) {
				best = Arrays.copyOf(current, current.length);
				current = best;
			}
		}
		
		return best;
	}
	
	private int[] getBestNeighbour(int[] current) {
		int numCities = problem.getNumCities();
		
		int city = -1;
		
		int[] best = Arrays.copyOf(current, current.length);
		int bestCost = problem.f(best);
		
		for (int i = 0; i < numCities - 1; i++) {
			// executes a move on current solution
			// a move consists of swapping city i with i + 1
			int[] neighbour = swap(current, i, i + 1);
			int neighbourCost = problem.f(neighbour);
			
			// if it is a better neighbour and this move is not tabu, then accept it
			if (neighbourCost < bestCost && !isTabuMove(i, i + 1)) {
				best = Arrays.copyOf(neighbour, neighbour.length);
				city = i;
				bestCost = neighbourCost;
			}
		}
		
		// add to tabu list and remove if its size is greater than allowed
		if (city != -1) {
			if (tabu.size() > MAX_TABU_LIST_SIZE) {
				tabu.remove();
			}
			tabu.add(new TabuMove(city, city + 1));
		}
		
		return Arrays.copyOf(best, best.length);
	}

	private boolean isTabuMove(int i, int j) {
		TabuMove move = new TabuMove(i, j);
		for (TabuMove entry : tabu) {
			if (move.equals(entry)) {
				return true;
			}
		}
		return false;
	}

	private int[] swap(int[] solution, int i, int j) {
		int[] newSolution = Arrays.copyOf(solution, solution.length);
		int temp = newSolution[i];
		newSolution[i] = newSolution[j];
		newSolution[j] = temp;
		return newSolution;
	}

	/**
	 * @return A random initial solution.
	 */
	public int[] createInitialSolution() {
		int numCities = problem.getNumCities();
		int[] solution = new int[numCities];
		
		boolean[] wasChosen = new boolean[numCities];
		
		for (int i = 0; i < numCities; i++) {
			do {
				// pick an unchosen random city
				solution[i] = (int) (Math.random() * numCities);
			} while (wasChosen[solution[i]]);
			wasChosen[solution[i]] = true;
		}
		
		return solution;
	}
	
}
