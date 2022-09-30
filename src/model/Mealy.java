package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mealy extends FiniteStateAutomaton{

	public Mealy(String[] states, String[] inputSymbols, String[] outputSymbols, String[][] data) {
		super(states, inputSymbols, outputSymbols, data);
	}

	@Override
	protected List<List<String>> findConnectedAutomaton() {
		boolean allStatesAreAccesible = false;
		for(int m = 0; m < getStates().length && !allStatesAreAccesible; m++) {
			if(getAccessibleStates()[m] == 1) {
				findSuccessorsOf(m);
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
		
		setInfo(getInfo()+getInfoAboutAccessibleStates());
				
		return connectedAutomaton;
	}
	
	@Override
	protected void findSuccessorsOf(int indexOfState) {
		//the current state is accessible
		getAccessibleStates()[indexOfState] = 1;
		
		for(int i = 0, k = 1; i < getInputSymbols().length; i++, k+=3) {
			int indexOfStateSuccesor = Integer.parseInt(getData()[indexOfState][k+2]);
			
			//If the successor is accessible I stop but if not I go to that state
			if(getAccessibleStates()[indexOfStateSuccesor] != 1) {
				findSuccessorsOf(indexOfStateSuccesor);
			}
			
		}
	}
	
	@Override
	protected ArrayList<ArrayList<Integer>> getInitialPartitions(List<List<String>> reducedAutomaton) {
		ArrayList<ArrayList<Integer>> partitions = new ArrayList<>();
		for(int i = 0; i < reducedAutomaton.size(); i++) {
			boolean fitsInAPartition = false;
			String outputSymbolOfCurrentState = getIndexOfOutputSymbols(Integer.valueOf(reducedAutomaton.get(i).get(0)));
			for(int j = 0; j < partitions.size() && !fitsInAPartition; j++) {
				String outputSymbolOfPartition = getIndexOfOutputSymbols(partitions.get(j).get(0));
				//If the output symbols of the states are equals
				if(outputSymbolOfPartition.equals(outputSymbolOfCurrentState)) {
					partitions.get(j).add(i);
					assignIndexOfPartition(j, i);
					fitsInAPartition = true;
				}
			}
			if(!fitsInAPartition) {
				ArrayList<Integer> partition = new ArrayList<Integer>();
				partition.add(i);
				partitions.add(partition);
				assignIndexOfPartition(partitions.size()-1, i);
			}
		}
		return partitions;
	}
	
	private String getIndexOfOutputSymbols(int indexState) {
		String indices = "";
		for(int i = 0, k = 2; i < getInputSymbols().length; i++, k += 3) {
			indices += getData()[indexState][k];
		}
		return indices;
	}
	
	@Override
	protected int getSuccessorOf(int indexState, int indexOfInputSymbol) {
		int indexOfOutputState=(indexOfInputSymbol+1)*3;
		int indexOfSuccessor = Integer.valueOf(getData()[indexState][indexOfOutputState]);
		return indexOfSuccessor;
		
	}
	
	@Override
	protected String[] changeIndices(String[] row) {
		for(int i = 3, k = 0; k < getInputSymbols().length; k++, i+=3) {
			int oldIndex = Integer.valueOf(row[i]);
			int newIndex = getNewStates()[oldIndex];
			String nameOfState = getStates()[newIndex];
			row[i] = String.valueOf(nameOfState);
		}
		return row;
	}

}
