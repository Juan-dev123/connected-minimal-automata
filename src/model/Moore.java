package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Moore extends FiniteStateAutomaton{
	
//	private String[] states;
//	private String[] inputSymbols;
//	private String[] outputSymbols;
//	private String[][] data;
//	private int[] accessibleStates;

	public Moore(String[] states, String[] inputSymbols, String[] outputSymbols, String[][] data) {
		super(states, inputSymbols, outputSymbols, data);
	}

	@Override
	protected List<List<String>> findConnectedAutomaton() {
		boolean allStatesAreAccesible = false;
		for(int m = 0; m < getStates().length && !allStatesAreAccesible; m++) {
			if(getAccessibleStates()[m] == 1) {
				findSuccesorsOf(m);
				allStatesAreAccesible = true;
				for(int j = 0; j < getAccessibleStates().length && allStatesAreAccesible; j++) {
					if(getAccessibleStates()[j] == 0) {
						allStatesAreAccesible = false;
					}
				}
			}
		}
		
		//Create a new table without the inaccessible states
		List<List<String>> connectedAutomaton = new ArrayList<>();
		
		for(int i = 0; i < getAccessibleStates().length; i++) {
			if(getAccessibleStates()[i] == 1) {
				List<String> row = Arrays.asList(getData()[i]);
				connectedAutomaton.add(row);
			}
		}
		
		return connectedAutomaton;
	}
	
	
	private void findSuccesorsOf(int indexOfState) {
		//the current state is accessible
		getAccessibleStates()[indexOfState] = 1;
		
		for(int i = 0, k = 1; i < getInputSymbols().length; i++, k+=2) {
			int indexOfStateSuccesor = Integer.parseInt(getData()[indexOfState][k+1]);
		
			//If the successor is accessible I stop but if not I go to that state
			if(getAccessibleStates()[indexOfStateSuccesor] != 1) {
				findSuccesorsOf(indexOfStateSuccesor);
			}
		}
	}

	@Override
	public List<List<String>> reduceAutomaton() {
		List<List<String>> reducedAutomaton = findConnectedAutomaton();
		transformData(reducedAutomaton);
		return reducedAutomaton;
	}

	@Override
	public String getInfo() {
		String msg = "The inaccessible states are:";
		boolean atLeastOne = false;
		for(int i = 0; i < getStates().length; i++) {
			if(getAccessibleStates()[i] == 0) {
				atLeastOne = true;
				msg += " " + String.valueOf(getStates()[i]);
			}
		}
		if(!atLeastOne) {
			msg += " All the states are accessible";
		}
		
		return msg;
	}
	
	private void transformData(List<List<String>> data){
		for(int i = 0; i < data.size(); i++) {
			for(int j = 0; j < (getInputSymbols().length*2)+2; j+=2) {
				int indexOfState = Integer.valueOf(data.get(i).get(j));
				data.get(i).set(j, getStates()[indexOfState]);
			}
		}
	}

}
