package model;

import exception.InvalidFormatException;
import exception.InvalidStateException;
import exception.InvalidSymbolException;

public class Moore extends FiniteStateAutomaton{

	public Moore(String[] states, String[][] edges, String[] inputAlphabet, String[] outputAlphabet,
			String initialState) throws InvalidFormatException, InvalidSymbolException, InvalidStateException {
		super(states, edges, inputAlphabet, outputAlphabet, initialState);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createStates(String[] states) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createEdges(String[][] edges)
			throws InvalidFormatException, InvalidSymbolException, InvalidStateException {
		// TODO Auto-generated method stub
		
	}



}
