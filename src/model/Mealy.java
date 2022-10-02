package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Juan-dev123
 *
 */
public class Mealy extends FiniteStateAutomaton{

	/**
	 * The constructor method of a mealy automaton. <br>
	 * <b>pre: </b> All parameters are not null. The number of rows in the data is the length of states attribute<br>
	 * <b>post: </b> A mealy automaton is created. <br>
	 * @param states The set of states
	 * @param inputSymbols The set of input symbols
	 * @param outputSymbols The set of output symbols
	 * @param data The data of the mealy automaton
	 */
	public Mealy(String[] states, String[] inputSymbols, String[] outputSymbols, String[][] data) {
		super(states, inputSymbols, outputSymbols, data);
	}

	/**
	 * Finds the connected automaton. <br>
	 * <b>pre: </b> The data attribute is not null <br>
	 * @return The connected mealy automaton
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
		
		for(int i = 0, k = 1; i < getInputSymbols().length; i++, k+=3) {
			int indexOfStateSuccesor = Integer.parseInt(getData()[indexOfState][k+2]);
			
			//If the successor is accessible I stop but if not I go to that state
			if(getAccessibleStates()[indexOfStateSuccesor] != 1) {
				findSuccessorsOf(indexOfStateSuccesor);
			}
			
		}
	}
	
	/**
	 * Gets the initial partitions of a mealy automaton. <br>
	 * <b>pre: </b> reduceAutomaton parameter cannot be null <br>
	 * @param reducedAutomaton The mealy automata from which you want to obtain the initial partitions
	 * @return The initial partitions
	 */
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
	
	/**
	 * Gets a string with all the output symbols of a state <br>
	 * <b>pre: </b> indexState must be less than or equal to the length of the states parameter<br>
	 * @param indexState The index of the state
	 * @return A string with all the output symbols of a state
	 */
	private String getIndexOfOutputSymbols(int indexState) {
		String indices = "";
		for(int i = 0, k = 2; i < getInputSymbols().length; i++, k += 3) {
			indices += getData()[indexState][k];
		}
		return indices;
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
		int indexOfOutputState=(indexOfInputSymbol+1)*3;
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
		for(int i = 3, k = 0; k < getInputSymbols().length; k++, i+=3) {
			int oldIndex = Integer.valueOf(row[i]);
			int newIndex = getNewStates()[oldIndex];
			String nameOfState = getStates()[newIndex];
			row[i] = String.valueOf(nameOfState);
		}
		return row;
	}

}
