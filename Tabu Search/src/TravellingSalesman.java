package msc.metaheuristicas.tabu;

public class TravellingSalesman {

	private int[][] graph;
	
	public TravellingSalesman(int numCities) {
		graph = new int[numCities][numCities];
	}
	
	public int getNumCities() {
		return graph.length;
	}
	
	public void setDistance(int city1, int city2, int distance) {
		graph[city1][city2] = graph[city2][city1] = distance;
	}
	
	public int getDistance(int city1, int city2) {
		return graph[city1][city2];
	}
	
	public int f(int[] solution) {
		int cost = 0;
		
		for (int i = 0; i < solution.length - 1; i++) {
			cost += graph[solution[i]][solution[i + 1]];
		}
		
		// plus the cost from returning to first city
		cost += graph[solution[solution.length - 1]][solution[0]];
		
		return cost;
	}

}
