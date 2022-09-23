package model;

public class Edge {
	
	//Attributes
	private String inputSymbol;
	private State nextState;
	
	//Methods
	public Edge(String inputSymbol, State nextState) {
		this.inputSymbol = inputSymbol;
		this.nextState = nextState;
	}
	
	public String getInputSymbol() {
		return inputSymbol;
	}
	
	public State getNextState() {
		return nextState;
	}
}
