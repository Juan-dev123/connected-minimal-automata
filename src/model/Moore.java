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
	public List<List<String>> findConnectedAutomaton() {
		boolean allStatesAreAccesible = false;
		for(int i = 0; i < getInputSymbols().length && !allStatesAreAccesible; i++) {
			findSuccesorOf(0, i);
			allStatesAreAccesible = true;
			for(int j = 0; j < getAccessibleStates().length && allStatesAreAccesible; j++) {
				if(getAccessibleStates()[j] == 0) {
					allStatesAreAccesible = false;
				}
			}
		}
		
		List<List<String>> reducedAutomaton = new ArrayList<>();
		
		for(int i = 0; i < getAccessibleStates().length; i++) {
			if(getAccessibleStates()[i] == 1) {
				List<String> row = Arrays.asList(getData()[i]);
				reducedAutomaton.add(row);
			}
		}
		
		return reducedAutomaton;
	}
	
	private void findSuccesorOf(int indexOfState, int indexOfsymbol) {
		//the current state is accessible
		getAccessibleStates()[indexOfState] = 1;
		int indexOfStateSuccesor = Integer.parseInt(getData()[indexOfState][indexOfsymbol+1]);
		
		//If the successor is accessible I stop but if not I go to that state
		if(getAccessibleStates()[indexOfStateSuccesor] != 1) {
			findSuccesorOf(indexOfStateSuccesor, indexOfsymbol);
		}
	}

	@Override
	public String[][] reduceAutomaton() {
		// TODO Auto-generated method stub
		return null;
	}

}
