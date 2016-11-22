package msc.metaheuristics.grasp;

import java.util.Random;

public class TravellingSalesman {

	private Integer[][] graph;
	
	private int numCities;
	
	public TravellingSalesman(int numCities) {
		this.numCities = numCities;
		graph = new Integer[numCities][numCities];
		buildGraph();
	}
	
	public int getNumCities() {
		return numCities;
	}
	
	public void setNumCities(int numCities) {
		this.numCities = numCities;
	}
	
	public void setDistance(int city1, int city2, int distance) {
		graph[city1][city2] = graph[city2][city1] = distance;
	}
	
	public int getDistance(int city1, int city2) {
		return graph[city1][city2];
	}
	
	public void buildGraph() {
		Random generator = new Random(402);
		for (int i = 0; i < numCities; i++) {
			setDistance(i, i, 0);
			for (int j = i + 1; j < numCities; j++) {
				int dist = (int) (generator.nextInt(21) + 1);
				setDistance(i, j, dist);
				setDistance(j, i, dist);
			}
		}
	}
	
	public Integer f(Integer[] solution) {
		Integer cost = 0;
		
		for (int i = 0; i < solution.length - 1; i++) {
			cost += graph[solution[i]][solution[i + 1]];
		}
		
		// plus the cost from returning to first city
		cost += graph[solution[solution.length - 1]][solution[0]];
		
		return cost;
	}
}
