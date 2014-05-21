package net.dmulloy2.buymagicplus.types;

/**
 * Represents an {@link Exception} thrown when execution of a {@link Package}
 * fails
 * 
 * @author dmulloy2
 */

public class ProcessingException extends Exception
{
	private static final long serialVersionUID = 6764709186279138551L;

	public ProcessingException()
	{
		//
	}

	public ProcessingException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ProcessingException(Throwable cause)
	{
		super(cause);
	}

	public ProcessingException(String message)
	{
		super(message);
	}
}