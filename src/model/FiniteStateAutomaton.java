package model;

import java.util.List;

/**
 * @author juanp
 *
 */

public abstract class FiniteStateAutomaton {
	private String[] states;
	private int[] newStates;
	private String[] inputSymbols;
	private String[] outputSymbols;
	private String[][] data; //indices 
	private int[] accessibleStates;
	private int[] indexOfPartitions;
	String info;
	
	public FiniteStateAutomaton(String[] states, String[] inputSymbols, String[] outputSymbols, String [][] data) {
		this.states = states;
		this.inputSymbols = inputSymbols;
		this.outputSymbols = outputSymbols;
		accessibleStates = new int[states.length];
		accessibleStates[0] = 1;
		this.data = data;
		indexOfPartitions = new int[states.length];
		newStates = new int[states.length];
		info = "";
	}
	
	
	/**
	 * <b>pre: </b> The first state in the matrix data is the initial state. <br>
	 * @return
	 */
	protected abstract List<List<String>> findConnectedAutomaton();	
	
	public abstract List<List<String>> reduceAutomaton();
	
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
	
	public int[] getIndexOfPartitions() {
		return indexOfPartitions;
	}
	
	public int[] getNewStates() {
		return newStates;
	}
	
	public String getInfo() {
		return info;
	}
	
	protected void setInfo(String infoP) {
		info = infoP;
	}

}
