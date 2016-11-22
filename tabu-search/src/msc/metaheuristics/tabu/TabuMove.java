package msc.metaheuristics.tabu;

public class TabuMove {

	private int city1;
	private int city2;
	
	public TabuMove(int city1, int city2) {
		this.city1 = city1;
		this.city2 = city2;
	}
	
	public int getCity1() {
		return city1;
	}

	public int getCity2() {
		return city2;
	}

	@Override
	public boolean equals(Object obj) {
		TabuMove tm = (TabuMove) obj;
		return super.equals(obj) && tm.getCity1() == city1 && tm.getCity2() == city2;
	}
}
