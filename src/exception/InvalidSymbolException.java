package exception;

public class InvalidSymbolException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public InvalidSymbolException(String msg) {
		super(msg);
	}

}
