package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Juan-dev123
 *
 */
public class Moore extends FiniteStateAutomaton{

	/**
	 * The constructor method of a moore automaton. <br>
	 * <b>pre: </b> All parameters are not null. The number of rows in the data is the length of states attribute<br>
	 * <b>post: </b> A moore automaton is created. <br>
	 * @param states The set of states
	 * @param inputSymbols The set of input symbols
	 * @param outputSymbols The set of output symbols
	 * @param data The data of the moore automaton
	 */
	public Moore(String[] states, String[] inputSymbols, String[] outputSymbols, String[][] data) {
		super(states, inputSymbols, outputSymbols, data);
	}

	/**
	 * Finds the connected automaton. <br>
	 * <b>pre: </b> The data attribute is not null <br>
	 * @return The connected moore automaton
	 */
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
	
	/**
	 * Finds all the successor of one state. <br>
	 * <b>post: </b> The accessibleStates attribute will have a 1 in a position if a state is accessible and has that position as an index and 0 if it is not accessible <br>
	 * @param indexOfState The index of the state to which you want to find the successors
	 */
	@Override
	protected void findSuccessorsOf(int indexOfState) {
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
	
	/**
	 * Gets the initial partitions of a moore automaton. <br>
	 * <b>pre: </b> reduceAutomaton parameter cannot be null <br>
	 * @param reducedAutomaton The moore automata from which you want to obtain the initial partitions
	 * @return The initial partitions
	 */
	@Override
	protected ArrayList<ArrayList<Integer>> getInitialPartitions(List<List<String>> reducedAutomaton) {
		ArrayList<ArrayList<Integer>> partitions = new ArrayList<>();
		for(int i = 0; i < reducedAutomaton.size(); i++) {
			boolean fitsInAPartition = false;
			String outputSymbolOfCurrentState = reducedAutomaton.get(i).get(reducedAutomaton.get(i).size()-1);
			for(int j = 0; j < partitions.size() && !fitsInAPartition; j++) {
				String outputSymbolOfPartition = reducedAutomaton.get(partitions.get(j).get(0)).get(reducedAutomaton.get(i).size()-1);
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
	
	/**
	 * Gets the successor of a state given an input symbol. <br>
	 * <b>pre: </b> indexState must be less than or equal to the number of rows in data attribute and indexofInput must be less than or equal to the number of input symbols <br>
	 * @param indexState The index of the state in the states attribute
	 * @param indexOfInputSymbol The index of the input symbol in the inputSymbols attribute
	 * @return The index of the successor in the states attribute
	 */
	@Override
	protected int getSuccessorOf(int indexState, int indexOfInputSymbol) {
		int indexOfOutputState = (indexOfInputSymbol+1)*2;
		int indexOfSuccessor = Integer.valueOf(getData()[indexState][indexOfOutputState]);
		return indexOfSuccessor;
		

	}
	
	/**
	 * Changes the indexes of the states by their respective names <br>
	 * @param row The row in which you want to change the indices
	 * @return The row with the names of the states instead of their indices
	 */
	@Override
	protected String[] changeIndices(String[] row) {
		for(int i = 2, k = 0; k < getInputSymbols().length; k++, i+=2) {
			int oldIndex = Integer.valueOf(row[i]);
			int newIndex = getNewStates()[oldIndex];
			String nameOfState = getStates()[newIndex];
			row[i] = String.valueOf(nameOfState);
		}
		return row;
	}
}
