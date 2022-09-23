package model;
import exception.InvalidFormatException;
import exception.InvalidStateException;
import exception.InvalidSymbolException;

public class Mealy extends FiniteStateAutomaton{

	public Mealy(String[] states, String[][] edges, String[] inputAlphabet, String[] outputAlphabet, String initialState) throws InvalidFormatException, InvalidSymbolException, InvalidStateException {
		super(states, edges, inputAlphabet, outputAlphabet, initialState);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void createStates(String[] states) {
		int numOfStates = states.length;
		State[] tempStates = new State[numOfStates];
		for(int i = 0; i < numOfStates; i++) {
			tempStates[i] = new State(states[i]);
		}
		setStates(tempStates);
	}

	@Override
	protected void createEdges(String[][] edges) throws InvalidFormatException, InvalidSymbolException, InvalidStateException {
		// State, input, finalState, output
		for(int i = 0; i < edges.length; i++) {
			try {
				State actualState = findState(edges[i][0]);
				if(actualState == null) {
					throw new InvalidStateException("The state " + actualState + "is not in the set of states");
				}
				State finalState = findState(edges[i][2]);
				if(finalState == null) {
					throw new InvalidStateException("The state " + finalState + "is not in the set of states");
				}
				String inputSymbol = edges[i][1];
				if(!checkInputSymbol(inputSymbol)) {	//If the symbol does not exist in the set of input alphabet
					throw new InvalidSymbolException("The symbol " + inputSymbol + " is not in the set of input alphabet");
				}
				String outputSymbol = edges[i][3];
				if(!checkOutputSymbol(outputSymbol)) {	//If the symbol does not exist in the set of output alphabet
					throw new InvalidSymbolException("The symbol " + outputSymbol + " is not in the set of output alphabet");
				}
				Edge tempEdge = new EdgeMealy(inputSymbol, finalState, outputSymbol);
				actualState.addEdge(tempEdge);
			}catch(IndexOutOfBoundsException e) {
				throw new InvalidFormatException("The format in which the data was entered is not correct");
			}
			
		}
		
	}

}
