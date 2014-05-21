package net.dmulloy2.buymagicplus.types;

import lombok.Getter;

/**
 * @author dmulloy2
 */

@Getter
public enum Permission
{
	GIVE,
	INFO,
	LIST,
	RELOAD,
	VERSION,
	;

	private String node;
	private Permission(boolean command)
	{
		this.node = toString().toLowerCase().replaceAll("_", ".");
		
		if (command) 
			node = "cmd." + node;
	}

	private Permission()
	{
		this(true);
	}
}