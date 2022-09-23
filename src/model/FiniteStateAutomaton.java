package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exception.InvalidFormatException;
import exception.InvalidStateException;
import exception.InvalidSymbolException;

public abstract class FiniteStateAutomaton {
	
	private State[] states;
	private String[] inputAlphabet;
	private String[] outputAlphabet;
	private State initialState;
	
	public FiniteStateAutomaton(String[] states, String[][] edges, String[] inputAlphabet, String[] outputAlphabet, String initialState) throws InvalidFormatException, InvalidSymbolException, InvalidStateException {
		this.inputAlphabet = inputAlphabet;
		this.outputAlphabet = outputAlphabet;
		createStates(states);
		createEdges(edges);
		State tempState = findState(initialState);
		if(tempState == null) {
			throw new InvalidStateException("The state " + initialState + "is not in the set of states");
		}else {
			this.initialState = tempState;
		}
		checkEdges();
	}
	
	public State findState(String nameOfState) {
		State tempState = null;
		boolean stop = false;
		for(int i = 0; i < states.length && stop; i++) {
			if(states[i].getName().equals(nameOfState)) {
				tempState = states[i];
				stop = true;
			}
		}
		return tempState;
	}
	
	public boolean checkInputSymbol(String symbol) {
		boolean exists = false;
		for(int i = 0; i < inputAlphabet.length && !exists; i++) {
			if(inputAlphabet[i].equals(symbol)) {
				exists = true;
			}
		}
		return exists;
	}
	
	public boolean checkOutputSymbol(String symbol) {
		boolean exists = false;
		for(int i = 0; i < outputAlphabet.length && !exists; i++) {
			if(outputAlphabet[i].equals(symbol)) {
				exists = true;
			}
		}
		return exists;
	}
	
	private void checkEdges() {
		for(int i = 0; i < states.length; i++) {
			ArrayList<Edge> tempEdges = new ArrayList<>(states[i].getEdges());
			List<String> tempInputAlphabet = Arrays.asList(inputAlphabet);
			int numOfEdges = tempEdges.size();
			int j = 0;
			while(i < numOfEdges || tempEdges.size() == 0) {
				boolean stop = false;
				for(int k = 0; k < tempInputAlphabet.size() && !stop; k++) {
					if(tempInputAlphabet.get(k).equals(tempEdges.get(0).getInputSymbol())) {
						tempInputAlphabet.remove(k);
						tempEdges.remove(0);
						stop = true;
					}
				}
				i++;
			}
			if(tempInputAlphabet.size() != 0) {
				//The state does not have all the input symbols
			}
			
			if(tempEdges.size() > 0) {
				//There is at least one repeated input symbol 
			}
		}
	}
	
	public State[] getStates() {
		return states;
	}
	
	public String[] getInputAlphabet() {
		return inputAlphabet;
	}
	
	public String[] getOutputAlphabet() {
		return outputAlphabet;
	}

	public void setStates(State[] states) {
		this.states = states;
	}

	public void setInputAlphabet(String[] inputAlphabet) {
		this.inputAlphabet = inputAlphabet;
	}

	public void setOutputAlphabet(String[] outputAlphabet) {
		this.outputAlphabet = outputAlphabet;
	}
	
	abstract protected void createStates(String[] states);
	
	abstract protected void createEdges(String[][] edges) throws InvalidFormatException, InvalidSymbolException, InvalidStateException;
	
}
