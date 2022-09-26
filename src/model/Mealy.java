package model;

import java.util.List;

public class Mealy extends FiniteStateAutomaton{
	
//	private String[] states;
//	private String[] inputSymbols;
//	private String[] outputSymbols;
//	private String[][] data;
//	private int[] accessibleStates;

	public Mealy(String[] states, String[] inputSymbols, String[] outputSymbols, String[][] data) {
		super(states, inputSymbols, outputSymbols, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<List<String>> findConnectedAutomaton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] reduceAutomaton() {
		// TODO Auto-generated method stub
		return null;
	}

}
