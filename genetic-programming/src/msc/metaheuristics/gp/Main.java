package msc.metaheuristics.gp;

import java.util.Arrays;
import java.util.List;

public class Main {
	
	public static void main(String[] args) {
		GeneticProgram gp = new GeneticProgram();
		
		// x^2 + x + 1
		List<Integer> inputs = Arrays.asList(0, 1, 2, 3, 4, 5, 10, 15, 50, 100);
		List<Integer> outputs = Arrays.asList(1, 3, 7, 13, 21, 31, 111, 241, 2551, 10101);
		
		gp.setInputs(inputs);
		gp.setOutputs(outputs);
		
		gp.run();
	}
}
