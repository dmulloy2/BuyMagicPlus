package net.dmulloy2.buymagicplus.types;

import lombok.Getter;

/**
 * Represents an {@link Exception} thrown when execution of a {@link Package}
 * fails
 * 
 * @author dmulloy2
 */

public class ProcessingException extends Exception
{
	private static final long serialVersionUID = 6764709186279138551L;

	private final @Getter Package pack;

	public ProcessingException(String s, Package pack)
	{
		super(s);
		this.pack = pack;
	}

	public ProcessingException(Exception e, Package pack)
	{
		this(e.getMessage(), pack);
	}
}