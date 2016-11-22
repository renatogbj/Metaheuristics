package msc.metaheuristics.grasp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Grasp {

	public List<Integer> costs = new ArrayList<>();
	
	private static final int MAX_ITER = 1000;
	private static final int RCL_CARDINALITY = 10;
	private int cardinality;
	
	private TravellingSalesman problem;
	
	public Grasp(TravellingSalesman problem, int cardinality) {
		this.problem = problem;
		this.cardinality = cardinality;
	}
	
	public Grasp(TravellingSalesman problem) {
		this.problem = problem;
		this.cardinality = -1;
	}

	public Integer[] find() {
		Integer[] globalBestSolution = new Integer[problem.getNumCities()];
		Integer globalBestCost = Integer.MAX_VALUE;
		
		for (int i = 0; i < MAX_ITER; i++) {
			Integer[] greedySolution = generateGreedyRandomizedSolution();
			Integer[] localBestSolution = localSearch(greedySolution);
			
			Integer localBestCost = problem.f(localBestSolution);
			
			System.out.println(i + ": Global best (" + globalBestCost + ") | Current best (" + localBestCost + ")");
			
			if (localBestCost < globalBestCost) {
				globalBestSolution = Arrays.copyOf(localBestSolution, localBestSolution.length);
				globalBestCost = localBestCost;
			}
			
			// to plot
			costs.add(globalBestCost);
		}
		
		return globalBestSolution;
	}

	private Integer[] localSearch(Integer[] current) {
		int numCities = problem.getNumCities();
		
		Integer[] best = Arrays.copyOf(current, current.length);
		int bestCost = problem.f(best);
		
		for (int i = 1; i < numCities - 1; i++) {
			// executes a move on current solution
			// a move consists of swapping city i with i + 1
			Integer[] neighbour = swap(current, i, i + 1);
			int neighbourCost = problem.f(neighbour);
			
			if (neighbourCost < bestCost) {
				bestCost = neighbourCost;
				best = Arrays.copyOf(neighbour, neighbour.length);
			}
		}
		
		return best;
	}

	private Integer[] generateGreedyRandomizedSolution() {
		Integer[] solution = new Integer[problem.getNumCities()];
		solution[0] = 0;
		
		Set<Integer> addedToSolution = new TreeSet<>();
		addedToSolution.add(0);
		
		for (int i = 1; i < problem.getNumCities(); i++) {
			
			List<Integer> rcl = new ArrayList<>();
			
			// fill the Restricted Candidate List
			for (int cont = 0; cont < (cardinality > 0 ? cardinality : RCL_CARDINALITY); cont++) {
				
				int candidateCost = Integer.MAX_VALUE;
				int candidate = -1;
				
				// find the best candidate
				for (int possibleCandidate = 0; possibleCandidate < problem.getNumCities(); possibleCandidate++) {
					
					// do not compare it against itself
					if (possibleCandidate != solution[i - 1]) {
						int possibleCandidateCost = problem.getDistance(solution[i - 1], possibleCandidate);
						
						if (!addedToSolution.contains(possibleCandidate) &&
							!rcl.contains(possibleCandidate) &&
							possibleCandidateCost < candidateCost) {
							
							candidate = possibleCandidate;
							candidateCost = possibleCandidateCost;
						}
					}
				}
				
				if (candidate != -1) {
					rcl.add(candidate);
				}
			}
			
			// get a random one and throw it in the solution
			if (!rcl.isEmpty()) {
				int randomCandidate = rcl.get((int) (Math.random() * rcl.size()));
				solution[i] = randomCandidate;
				addedToSolution.add(randomCandidate);
			} else {
				System.out.println("WTF!!! The RCL is Empty!");
			}
		}
		
		return solution;
	}
	
	private Integer[] swap(Integer[] solution, int i, int j) {
		Integer[] newSolution = Arrays.copyOf(solution, solution.length);
		int temp = newSolution[i];
		newSolution[i] = newSolution[j];
		newSolution[j] = temp;
		return newSolution;
	}
	
}
