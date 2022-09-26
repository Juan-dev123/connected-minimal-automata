package model;

import java.util.List;

/**
 * @author juanp
 *
 */

public abstract class FiniteStateAutomaton {
	private String[] states;
	private String[] inputSymbols;
	private String[] outputSymbols;
	private String[][] data;
	private int[] accessibleStates;
	
	public FiniteStateAutomaton(String[] states, String[] inputSymbols, String[] outputSymbols, String [][] data) {
		this.states = states;
		this.inputSymbols = inputSymbols;
		this.outputSymbols = outputSymbols;
	}
	
	
	/**
	 * <b>pre: </b> The first state in the matrix data is the initial state. <br>
	 * @return
	 */
	public abstract List<List<String>> findConnectedAutomaton();	
	
	public abstract String[][] reduceAutomaton();


	public String[] getStates() {
		return states;
	}


	public String[] getInputSymbols() {
		return inputSymbols;
	}


	public String[] getOutputSymbols() {
		return outputSymbols;
	}


	public String[][] getData() {
		return data;
	}


	public int[] getAccessibleStates() {
		return accessibleStates;
	}

}
