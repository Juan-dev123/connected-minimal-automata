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
	
	
	private void findSuccessorsOf(int indexOfState) {
		//the current state is accessible
		getAccessibleStates()[indexOfState] = 1;
		
		for(int i = 0, k = 1; i < getInputSymbols().length; i++, k+=2) {
			int indexOfStateSuccesor = Integer.parseInt(getData()[indexOfState][k+1]);
		
			//If the successor is accessible I stop but if not I go to that state
			if(getAccessibleStates()[indexOfStateSuccesor] != 1) {
				findSuccessorsOf(indexOfStateSuccesor);
			}
		}
	}

	@Override
	public List<List<String>> reduceAutomaton() {
		List<List<String>> reducedAutomaton = findConnectedAutomaton();
		ArrayList<ArrayList<Integer>> partitions = getInitialPartitions(reducedAutomaton);
		partitions = partitionOf(partitions);
		return transformData(partitions);
	}
	
	private ArrayList<ArrayList<Integer>> getInitialPartitions(List<List<String>> reducedAutomaton) {
		ArrayList<ArrayList<Integer>> partitions = new ArrayList<>();
		for(int i = 0; i < reducedAutomaton.size(); i++) {
			boolean fitsInAPartition = false;
			String outputStateOfCurrentState = reducedAutomaton.get(i).get(reducedAutomaton.get(i).size()-1);
			for(int j = 0; j < partitions.size() && !fitsInAPartition; j++) {
				//String outputStateOfPartition = getData()[partitions.get(j).get(0)][getData()[i].length-1];
				String outputStateOfPartition = reducedAutomaton.get(partitions.get(j).get(0)).get(reducedAutomaton.get(i).size()-1);
				//If the output symbols of the states are equals
				if(outputStateOfPartition.equals(outputStateOfCurrentState)) {
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
	
	private ArrayList<ArrayList<Integer>> partitionOf(ArrayList<ArrayList<Integer>> partitions) {
		ArrayList<ArrayList<Integer>> newPartitions = new ArrayList<>();
		ArrayList<Integer> aloneStates = new ArrayList<>();	//Here are the states that leave the partitions
		for(int i = 0; i < partitions.size(); i++) {
			ArrayList<Integer> partition = new ArrayList<>();
			for(int j = 0; j < partitions.get(i).size(); j++) {
				if(j == 0) {
					partition.add(partitions.get(i).get(0));
				}else {
					int indexOfFirstStateInThePartition = partitions.get(i).get(0);
					int indexofCurrentstateInThePartition = partitions.get(i).get(j);
					boolean statesInTheSamePartition = getIndexOfPartitions()[indexOfFirstStateInThePartition] == getIndexOfPartitions()[indexofCurrentstateInThePartition];
					boolean successorsInTheSamePartition = true;
					for(int k = 0; k < getInputSymbols().length && successorsInTheSamePartition; k++) {
						if(getIndexOfPartitions()[getSuccessorOf(indexOfFirstStateInThePartition, k)] != getIndexOfPartitions()[getSuccessorOf(indexofCurrentstateInThePartition, k)]) {
							successorsInTheSamePartition = false;
						}
					}
					if(successorsInTheSamePartition && statesInTheSamePartition) {
						partition.add(partitions.get(i).get(j));
					}else {
						aloneStates.add(partitions.get(i).get(j));
					}
				}
			}
			newPartitions.add(partition);
		}
		if(aloneStates.size()!=0) {
			ArrayList<ArrayList<Integer>> lonelyPartitions = new ArrayList<>();
			partitionLoneStates(aloneStates, partitions, lonelyPartitions);
			for(int i = 0; i < lonelyPartitions.size(); i++) {
				newPartitions.add(lonelyPartitions.get(i));
				for(int j = 0; j < lonelyPartitions.get(i).size(); j++) {
					assignIndexOfPartition(newPartitions.size()-1, lonelyPartitions.get(i).get(j));
				}
			}
			partitions = partitionOf(newPartitions);
		}
		return partitions;
	}
	
	private void partitionLoneStates(ArrayList<Integer> aloneStates, ArrayList<ArrayList<Integer>> prevPartitions, ArrayList<ArrayList<Integer>> newPartitions){
		while(aloneStates.size()>0) {
			ArrayList<Integer> partition = new ArrayList<>();
			ArrayList<Integer> statesForDelete = new ArrayList<>();
			for(int i = 1; i<aloneStates.size(); i++) {
				int indexOfFirstStateInThePartition = aloneStates.get(0);
				int indexofCurrentstateInThePartition = aloneStates.get(i);
				boolean statesInTheSamePartition = getIndexOfPartitions()[indexOfFirstStateInThePartition] == getIndexOfPartitions()[indexofCurrentstateInThePartition];
				boolean successorsInTheSamePartition = true;
				for(int k = 0; k < getInputSymbols().length && successorsInTheSamePartition; k++) {
					if(getIndexOfPartitions()[getSuccessorOf(indexOfFirstStateInThePartition, k)] != getIndexOfPartitions()[getSuccessorOf(indexofCurrentstateInThePartition, k)]) {
						successorsInTheSamePartition = false;
					}
				}
				if(successorsInTheSamePartition && statesInTheSamePartition) {
					partition.add(aloneStates.get(i));
					statesForDelete.add(i);
				}
			}
			if(aloneStates.size() == 1 || statesForDelete.size() == 0) {
				partition.add(aloneStates.get(0));
				aloneStates.remove(0);
			}else {
				partition.add(aloneStates.get(0));
				for(int i = statesForDelete.size(); i > 0; i--) {
					aloneStates.remove(i);
				}
				aloneStates.remove(0);
			}
			newPartitions.add(partition);
		}

	}
	
	private int getSuccessorOf(int indexState, int indexOfInputSymbol) {
		int indexOfOutputState;
		if(indexOfInputSymbol == 0) {
			indexOfOutputState = indexOfInputSymbol+2;
		}else {
			indexOfOutputState = indexOfInputSymbol+3;
		}
		int indexOfSuccessor = Integer.valueOf(getData()[indexState][indexOfOutputState]);
		return indexOfSuccessor;
		

	}
	
	private void assignIndexOfPartition(int indexPartition, int indexState) {
		getIndexOfPartitions()[indexState] = indexPartition;
	}

	private String getInfoAboutAccessibleStates() {
		String msg = "The inaccessible states are:";
		boolean atLeastOne = false;
		for(int i = 0; i < getStates().length; i++) {
			if(getAccessibleStates()[i] == 0) {
				atLeastOne = true;
				msg += " " + String.valueOf(getStates()[i]);
			}
		}
		if(!atLeastOne) {
			msg += " All the states are accessible\n";
		}else {
			msg += "\n";
		}
		return msg;
	}
	
	private List<List<String>> transformData(ArrayList<ArrayList<Integer>>data){
		List<List<String>> newData = new ArrayList<>();
		String info = "The new names of partitions are:\n";
		//Rename the sates
		for(int i = 0; i < data.size(); i++) {
			int newIndex = data.get(i).get(0);
			
			info += getPartitionInString(data.get(i)) + " = ";
			info += getStates()[newIndex] + "\n";
			
			getNewStates()[newIndex] = newIndex;
			for(int j = 1; j < data.get(i).size(); j++) {
				getNewStates()[data.get(i).get(j)] = newIndex;
			}
		}
		
		setInfo(getInfo() + info);
		
		//Create the new data
		for(int i = 0; i < data.size(); i++) {
			String[] oldRow = getData()[data.get(i).get(0)];
			String[] newRow = changeIndices(oldRow);
			newRow[0] = getStates()[Integer.valueOf(newRow[0])]; //Change the name of the state
			newData.add(Arrays.asList(newRow));
		}
		return newData;
	}
	
	private String getPartitionInString(ArrayList<Integer> partition) {
		String partitionInString = "{";
		for(int i = 0; i < partition.size()-1; i++) {
			partitionInString += getStates()[partition.get(i)] + ", ";
		}
		partitionInString += getStates()[partition.get(partition.size()-1)] + "}";
		return partitionInString;
	}
	
	private String[] changeIndices(String[] row) {
		for(int i = 2, k = 0; k < getInputSymbols().length; k++, i+=2) {
			int oldIndex = Integer.valueOf(row[i]);
			int newIndex = getNewStates()[oldIndex];
			String nameOfState = getStates()[newIndex];
			row[i] = String.valueOf(nameOfState);
		}
		return row;
	}
}
