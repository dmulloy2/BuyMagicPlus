package net.dmulloy2.buymagicplus.types;

import net.dmulloy2.types.IPermission;
import lombok.Getter;

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

	private String node;
	private Permission()
	{
		this.node = "cmd." +toString().toLowerCase().replaceAll("_", ".");
	}
}