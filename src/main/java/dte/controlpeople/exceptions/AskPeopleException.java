package dte.controlpeople.exceptions;

public class AskPeopleException extends RuntimeException
{
	private static final long serialVersionUID = 309812103814727403L;
	
	public AskPeopleException(String message) 
	{
		super(message);
	}
	
	public AskPeopleException(Throwable cause) 
	{
		super(cause);
	}
}