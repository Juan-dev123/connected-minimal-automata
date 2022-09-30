package model;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	protected abstract void findSuccessorsOf(int indexOfState);
	
	protected abstract ArrayList<ArrayList<Integer>> getInitialPartitions(List<List<String>> reducedAutomaton);
	
	protected abstract int getSuccessorOf(int indexState, int indexOfInputSymbol);
	
	protected abstract String[] changeIndices(String[] row);
	
	protected void assignIndexOfPartition(int indexPartition, int indexState) {
		getIndexOfPartitions()[indexState] = indexPartition;
	}
	
	protected String getInfoAboutAccessibleStates() {
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
	
	public List<List<String>> reduceAutomaton() {
		List<List<String>> reducedAutomaton = findConnectedAutomaton();
		ArrayList<ArrayList<Integer>> partitions = getInitialPartitions(reducedAutomaton);
		partitions = partitionOf(partitions); //ERRORRR
		renameTheStates(partitions);
		return transformData(partitions);
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
	
	private void renameTheStates(ArrayList<ArrayList<Integer>>data) {
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
	}
	
	private List<List<String>> transformData(ArrayList<ArrayList<Integer>>data){
		//Create the new data
		List<List<String>> newData = new ArrayList<>();
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
