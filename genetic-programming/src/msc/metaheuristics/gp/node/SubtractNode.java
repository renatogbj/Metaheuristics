package msc.metaheuristics.gp.node;

public class SubtractNode extends Node {

	public SubtractNode() {
		super.setNumChildren(2);
	}
	
	@Override
	public double eval(double value) {
		if (super.getLeftNode() != null && super.getRightNode() != null) {
			return super.getLeftNode().eval(value) - super.getRightNode().eval(value);
		} else {
			System.err.println("Children nodes not defined");
			return -1;
		}
	}
	
	public Node clone() {
		Node clone = new SubtractNode();
		clone.setFitness(super.getFitness());
		if (super.getLeftNode() != null && super.getRightNode() != null) {
			clone.setLeftNode(super.getLeftNode().clone());
			clone.setRightNode(super.getRightNode().clone());
		}
		return clone;
	}

	@Override
	public String toString() {
		return "-";
	}
}
