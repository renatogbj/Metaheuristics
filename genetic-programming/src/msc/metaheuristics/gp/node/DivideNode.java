package msc.metaheuristics.gp.node;

public class DivideNode extends Node {

	public DivideNode() {
		super.setNumChildren(2);
	}
	
	@Override
	public double eval(double value) {
		if (super.getLeftNode() != null && super.getRightNode() != null) {
			if (super.getRightNode().eval(value) != 0) {
				return super.getLeftNode().eval(value) / super.getRightNode().eval(value);
			} else {
				return 1;
			}
		} else {
			System.err.println("Children nodes not defined");
			return -1;
		}
	}
	
	public Node clone() {
		Node clone = new DivideNode();
		clone.setFitness(super.getFitness());
		if (super.getLeftNode() != null && super.getRightNode() != null) {
			clone.setLeftNode(super.getLeftNode().clone());
			clone.setRightNode(super.getRightNode().clone());
		}
		return clone;
	}

	@Override
	public String toString() {
		return "/";
	}
}
