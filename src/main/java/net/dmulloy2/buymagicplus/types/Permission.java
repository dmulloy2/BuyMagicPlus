package net.dmulloy2.buymagicplus.types;

/**
 * @author dmulloy2
 */

public enum Permission
{
	GIVE,

	PACKAGE_LIST,
	PACKAGE_INFO,

	RELOAD,
	VERSION,
	;

	private String node;

	Permission(boolean command)
	{
		this.node = toString().toLowerCase().replaceAll("_", ".");
		
		if (command) node = "cmd." + node;
	}

	Permission()
	{
		this(true);
	}
	
	public String getNode()
	{
		return node;
	}
}