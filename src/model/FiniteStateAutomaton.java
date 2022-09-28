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
	private String[][] data; //indices 
	private int[] accessibleStates;
	
	public FiniteStateAutomaton(String[] states, String[] inputSymbols, String[] outputSymbols, String [][] data) {
		this.states = states;
		this.inputSymbols = inputSymbols;
		this.outputSymbols = outputSymbols;
		accessibleStates = new int[states.length];
		accessibleStates[0] = 1;
		this.data = data;
	}
	
	
	/**
	 * <b>pre: </b> The first state in the matrix data is the initial state. <br>
	 * @return
	 */
	protected abstract List<List<String>> findConnectedAutomaton();	
	
	public abstract List<List<String>> reduceAutomaton();

	protected abstract String getInfo();
	
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
