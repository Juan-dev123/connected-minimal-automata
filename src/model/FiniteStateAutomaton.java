package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Juan-dev123
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
	
	/**
	 * The constructor method of a finite state automaton. <br>
	 * <b>pre: </b> All parameters are not null. The number of rows in the data is the length of states attribute<br>
	 * <b>post: </b> A finite state automaton is created. <br>
	 * @param states The set of states
	 * @param inputSymbols The set of input symbols
	 * @param outputSymbols The set of output symbols
	 * @param data The data of the finite state automaton
	 */
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
	 * Finds the connected automaton. <br>
	 * <b>pre: </b> The data attribute is not null <br>
	 * @return The connected finite state automaton
	 */
	protected abstract List<List<String>> findConnectedAutomaton();	
	
	/**
	 * Finds all the successor of one state. <br>
	 * <b>post: </b> The accessibleStates attribute will have a 1 in a position if a state is accessible and has that position as an index and 0 if it is not accessible <br>
	 * @param indexOfState The index of the state to which you want to find the successors
	 */
	protected abstract void findSuccessorsOf(int indexOfState);
	
	/**
	 * Gets the initial partitions of an automaton. <br>
	 * <b>pre: </b> reduceAutomaton parameter cannot be null <br>
	 * @param reducedAutomaton The automata from which you want to obtain the initial partitions
	 * @return The initial partitions
	 */
	protected abstract ArrayList<ArrayList<Integer>> getInitialPartitions(List<List<String>> reducedAutomaton);
	
	/**
	 * Gets the successor of a state given an input symbol. <br>
	 * <b>pre: </b> indexState must be less than or equal to the number of rows in data attribute and indexofInput must be less than or equal to the number of input symbols <br>
	 * @param indexState The index of the state in the states attribute
	 * @param indexOfInputSymbol The index of the input symbol in the inputSymbols attribute
	 * @return The index of the successor in the states attribute
	 */
	protected abstract int getSuccessorOf(int indexState, int indexOfInputSymbol);
	
	/**
	 * Changes the indexes of the states by their respective names <br>
	 * @param row The row in which you want to change the indices
	 * @return The row with the names of the states instead of their indices
	 */
	protected abstract String[] changeIndices(String[] row);
	
	
	/**
	 * Assigns each state the index of its partition <br>
	 * <b>pre: </b> indexState must be less than or equal to the number of rows in data attribute and indexPartition must be greater than or equal to 0
	 * <b>post: </b> The indexOfPartition attribute has the indices of the partitions of each state. Only the indices of the states that are in a partition are changed. <br>
	 * @param indexPartition The index of the partition
	 * @param indexState The index of the state in the states attribute
	 */
	protected void assignIndexOfPartition(int indexPartition, int indexState) {
		getIndexOfPartitions()[indexState] = indexPartition;
	}
	
	/**
	 * Gets the information about the accessible states <br>
	 * @return The information of the accessible states
	 */
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
	
	/**
	 * Reduces one automaton <br>
	 * @return The reduced automaton
	 */
	public List<List<String>> reduceAutomaton() {
		List<List<String>> reducedAutomaton = findConnectedAutomaton();
		ArrayList<ArrayList<Integer>> partitions = getInitialPartitions(reducedAutomaton);
		partitions = partitionOf(partitions); //ERRORRR
		renameTheStates(partitions);
		return transformData(partitions);
	}
	
	/**
	 * Partitions the states that were left alone because they came out of a partition <br>
	 * <b>pre: </b> No parameter can be null <br>
	 * @param aloneStates The states that were left alone
	 * @param prevPartitions The partition where the lone states came from
	 * @param newPartitions The list where the new partitions will be storage
	 */
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
	
	/**
	 * Partitions one partition <br>
	 * <b>pre: </b> The parameter partitions can't be null <br>
	 * @param partitions The partition that will be partitioned
	 * @return The partitioned partition
	 */
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
	
	/**
	 * Renames the partitions <br>
	 * <b>post: </b> The newStates attribute has the new indices reflecting the new partition name <br>
	 * @param data The partitions
	 */
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
	
	/**
	 * Creates a new automaton with renamed partitions and state names instead of indices <br>
	 * <b>pre: </b> The data parameter can't be null <br>
	 * @param data The data of the reduced and connected automaton
	 * @return The data of the automaton with renamed partitions and states names instead of indices
	 */
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
	
	/**
	 * Gets a string representation of a partition <br>
	 * <b>pre: </b> The partition parameter can't be null <br>
	 * @param partition The partition
	 * @return The string representation of a partition
	 */
	private String getPartitionInString(ArrayList<Integer> partition) {
		String partitionInString = "{";
		for(int i = 0; i < partition.size()-1; i++) {
			partitionInString += getStates()[partition.get(i)] + ", ";
		}
		partitionInString += getStates()[partition.get(partition.size()-1)] + "}";
		return partitionInString;
	}
	
	/**
	 * @return The states parameter
	 */
	public String[] getStates() {
		return states;
	}


	/**
	 * @return The inputSymbols parameter
	 */
	public String[] getInputSymbols() {
		return inputSymbols;
	}


	/**
	 * @return The outputSymbols parameter
	 */
	public String[] getOutputSymbols() {
		return outputSymbols;
	}


	/**
	 * @return The data parameter
	 */
	public String[][] getData() {
		return data;
	} 

	/**
	 * @return The accessibleStates parameter
	 */
	public int[] getAccessibleStates() {
		return accessibleStates;
	}
	
	/**
	 * @return The indexOfPartitions parameter
	 */
	public int[] getIndexOfPartitions() {
		return indexOfPartitions;
	}
	
	/**
	 * @return The newStates parameter
	 */
	public int[] getNewStates() {
		return newStates;
	}
	
	/**
	 * @return The info parameter
	 */
	public String getInfo() {
		return info;
	}
	
	/**
	 * Sets a string to the info parameter
	 * @param infoP The string
	 */
	protected void setInfo(String infoP) {
		info = infoP;
	}

}
