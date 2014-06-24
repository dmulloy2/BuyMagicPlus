package net.dmulloy2.buymagicplus.types;

import lombok.Getter;
import net.dmulloy2.types.IPermission;

/**
 * @author dmulloy2
 */

@Getter
public enum Permission implements IPermission
{
	GIVE,
	INFO,
	LIST,
	RELOAD,
	VERSION,
	;

	private final String node;
	private Permission()
	{
		this.node = "cmd." +toString().toLowerCase().replaceAll("_", ".");
	}
}