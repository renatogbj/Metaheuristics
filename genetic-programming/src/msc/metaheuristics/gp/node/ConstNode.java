package msc.metaheuristics.gp.node;

import java.util.Random;

public class ConstNode extends Node {

	private double constValue;
	
	public ConstNode() {
		super.setNumChildren(0);
		Random rand = new Random();
		this.constValue = rand.nextDouble();
	}
	
	public ConstNode(double constValue) {
		super.setNumChildren(0);
		this.constValue = constValue;
	}
	
	public double eval(double value) {
		return this.constValue;
	}
	
	public Node clone() {
		Node clone = new ConstNode(this.constValue);
		clone.setFitness(super.getFitness());
		return clone;
	}
	
	@Override
	public String toString() {
		return String.format("%.2f", this.constValue);
	}
}
