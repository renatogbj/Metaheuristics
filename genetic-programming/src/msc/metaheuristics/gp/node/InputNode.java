package msc.metaheuristics.gp.node;

import java.util.Random;

public class InputNode extends Node {

	private int inputIndex;
	
	public InputNode(int numInputs) {
		super.setNumChildren(0);
		Random rand = new Random();
		this.inputIndex = rand.nextInt() % numInputs;
	}
	
	public void setInputIndex(int inputIndex) {
		this.inputIndex = inputIndex;
	}
	
	public double eval(double value) {
		return value;
	}
	
	public Node clone() {
		InputNode clone = new InputNode(1);
		clone.setFitness(super.getFitness());
		clone.setInputIndex(this.inputIndex);
		return clone;
	}
	
	@Override
	public String toString() {
		return "" + this.inputIndex;
	}
}
