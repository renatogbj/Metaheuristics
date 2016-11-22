package msc.metaheuristics.gp;

import java.util.List;
import java.util.Random;

import msc.metaheuristics.gp.node.AddNode;
import msc.metaheuristics.gp.node.ConstNode;
import msc.metaheuristics.gp.node.DivideNode;
import msc.metaheuristics.gp.node.InputNode;
import msc.metaheuristics.gp.node.MultiplyNode;
import msc.metaheuristics.gp.node.Node;
import msc.metaheuristics.gp.node.SubtractNode;

public class GeneticProgram {

	private int maxTreeDepth;
	private int numInputs;
	private int sizePopulation;
	private int numGenerations;
	private int tournamentSize;
	private int mutationRate;
	
	public List<Integer> inputs;
	public List<Integer> outputs;
	
	/**
	 * Use default parameters:
	 * 		- maxTreeDepth 		= 3
	 * 		- numInputs 		= 1
	 * 		- sizePopulation	= 300
	 * 		- numGenerations	= 15
	 * 		- mutationRate		= 1%
	 */
	public GeneticProgram() {
		this(3, 1, 300, 500, 5);
	}
	
	public GeneticProgram(int maxTreeDepth, int numInputs, int sizePopulation, int numGenerations, int mutationRate) {
		this.maxTreeDepth = maxTreeDepth;
		this.numInputs = numInputs;
		this.sizePopulation = sizePopulation;
		this.numGenerations = numGenerations;
		this.mutationRate = mutationRate;
	}
	
	public void run() {
		Node[] population = new Node[sizePopulation];
		
		for (int i = 0; i < sizePopulation; i++) {
			population[i] = buildRandomTree();
		}
		
		calculateFitness(population[0]);
		Node bestTree = population[0];
		int bestTreeGeneration = 0;
		
//		int generation = 0;
//		int converge = 0;
//		double lastGeneration = 0;
//		while (bestTree.getFitness() > 10 && converge < 100) {
		
		for (int generation = 0; generation < numGenerations; generation++) {
		
			double avgPopFitness = 0;
			
			for (int i = 0; i < sizePopulation; i++) {
				avgPopFitness += calculateFitness(population[i]);
				if (population[i].getFitness() < bestTree.getFitness()) {
					bestTree = population[i].clone();
					bestTreeGeneration = generation;
				}
			}
			
			avgPopFitness /= sizePopulation;
			
//			System.out.println("Average population fitness in generation " + generation + ": "
//					+ String.format("%.0f", avgPopFitness));
			System.out.print(String.format("%.0f", avgPopFitness) + ", ");
			
			Node[] newPopulation = tournament(population);
			crossover(newPopulation);
			mutate(newPopulation);
			population = newPopulation;
			
//			generation++;
//			if (lastGeneration == avgPopFitness) {
//				converge++;
//			}
//			lastGeneration = avgPopFitness;
		}
		System.out.println();
		System.out.println("Best tree found in generation " + bestTreeGeneration + " with fitness "
				+ String.format("%.2f", bestTree.getFitness()));
		printTree(bestTree);
	}

	private void mutate(Node[] population) {
		for (int i = 0; i < sizePopulation; i++) {
			mutate(population[i]);
		}
	}
	
	private void mutate(Node tree) {
		Random rand = new Random();
		int chance = rand.nextInt(100);
		if (chance < mutationRate) {
			if (tree.getNumChildren() > 0) {
				int newNodeChoice = rand.nextInt(4);
				Node newNode = null;
				switch (newNodeChoice) {
				case 0:
					newNode = new AddNode();
					break;
				case 1:
					newNode = new SubtractNode();
					break;
				case 2:
					newNode = new MultiplyNode();
					break;
				case 3:
					newNode = new DivideNode();
					break;
				default:
					System.err.println("Something went wrong: invalid random number");
					return;
				}
				newNode.setLeftNode(tree.getLeftNode());
				newNode.setRightNode(tree.getRightNode());
				tree = newNode;
				mutate(newNode.getLeftNode());
				mutate(newNode.getRightNode());
			} else {
				int newNodeChoice = rand.nextInt(2);
				Node newNode = null;
				switch (newNodeChoice) {
				case 0:
					newNode = new ConstNode();
					break;
				case 1:
					newNode = new InputNode(this.numInputs);
					break;
				default:
					System.err.println("Something went wrong: invalid random number");
					return;
				}
				tree = newNode;
			}
		}
	}

	private void crossover(Node[] population) {
		Node[] newPopulation = new Node[sizePopulation];
		Random rand = new Random();
		
		int i = 0;
		while (i < sizePopulation) {
			Node parent1 = population[rand.nextInt(sizePopulation)];
			Node parent2 = population[rand.nextInt(sizePopulation)];
			
			Node child1 = null;
			Node child2 = null;
			
			crossover(parent1, parent2, child1, child2);
			
			newPopulation[i++] = child1;
			newPopulation[i++] = child2;
		}
		
		population = newPopulation;
	}
	
	private void crossover(Node parent1, Node parent2, Node child1, Node child2) {
		if (parent1.getNumChildren() > 0 && parent2.getNumChildren() > 0) {
			child1 = parent1.clone();
			child1.setRightNode(parent2.getRightNode().clone());
			child2 = parent2.clone();
			child2.setRightNode(parent1.getRightNode().clone());
		} else {
			child1 = parent1.clone();
			child2 = parent2.clone();
		}
	}

	private Node[] tournament(Node[] population) {
		Node[] newPopulation = new Node[sizePopulation];
		Random rand = new Random();
		
		for (int i = 0; i < sizePopulation; i++) {
			
			Node selected = population[rand.nextInt(sizePopulation)].clone();
			
			for (int j = 1; j < tournamentSize; j++) {
				Node temp = population[rand.nextInt(sizePopulation)].clone();
				
				if (temp.getFitness() < selected.getFitness()) {
					selected = temp;
				}
			}
			
			newPopulation[i] = selected;
		}
		
		return newPopulation;
	}

	/**
	 * Fitness is measured by the summation of the absolute difference of all the inputs and outputs.
	 * 
	 * @param node The individual tree.
	 * @return The fitness.
	 */
	private double calculateFitness(Node tree) {
		double sumAbsErr = 0;
		for (int i = 0; i < inputs.size(); i++) {
			sumAbsErr += Math.abs(outputs.get(i) - tree.eval(inputs.get(i)));
		}
		tree.setFitness(sumAbsErr);
		return sumAbsErr;
	}
	
	private void printTree(Node tree) {
		printTree(tree, 0);
	}
	
	private void printTree(Node tree, int depth) {
		for (int i = 0; i < depth; i++) {
			System.out.print("..");
		}
		
		System.out.println(tree);
		
		if (tree.getNumChildren() > 0) {
			printTree(tree.getLeftNode(), depth + 1);
			printTree(tree.getRightNode(), depth + 1);
		}
	}

	private Node buildRandomTree() {
		return buildRandomTree(0);
	}
	
	private Node buildRandomTree(int depth) {
		Node node = buildRandomNode(depth);
		
		if (node.getNumChildren() > 0) {
			node.setLeftNode(buildRandomTree(depth + 1));
			node.setRightNode(buildRandomTree(depth + 1));
		}
		
		return node;
	}
	
	private Node buildRandomNode(int depth) {
		int numChoices = 6;
		Random rand = new Random();
		
		if (depth >= this.maxTreeDepth) {
			numChoices = 2;
		}
		
		int choice = rand.nextInt(numChoices);
		
		switch (choice) {
		case 0:
			return new ConstNode();
		case 1:
			return new InputNode(this.numInputs);
		case 2:
			return new AddNode();
		case 3:
			return new SubtractNode();
		case 4:
			return new MultiplyNode();
		case 5:
			return new DivideNode();
		default:
			System.err.println("Something went wrong: invalid random number");
			return null;
		}
	}

	public void setInputs(List<Integer> inputs) {
		this.inputs = inputs;
	}

	public void setOutputs(List<Integer> outputs) {
		this.outputs = outputs;
	}
	
}
