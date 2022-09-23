package model;

public class EdgeMealy extends Edge{
	
	private String outputSymbol;

	public EdgeMealy(String inputSymbol, State nextState, String outputSymbol) {
		super(inputSymbol, nextState);
		this.outputSymbol = outputSymbol;
	}
	
	public String getOutputSymbol() {
		return outputSymbol;
	}

}
