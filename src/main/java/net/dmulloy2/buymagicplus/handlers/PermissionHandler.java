package net.dmulloy2.buymagicplus.handlers;

import net.dmulloy2.buymagicplus.types.Permission;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author dmulloy2
 */

public class PermissionHandler
{
	public PermissionHandler()
	{
	}

	public boolean hasPermission(CommandSender sender, Permission permission) 
	{
		return (permission == null) ? true : hasPermission(sender, getPermissionString(permission));
	}

	public boolean hasPermission(CommandSender sender, String permission) 
	{
		if (sender instanceof Player) 
		{
			Player p = (Player) sender;
			return (p.hasPermission(permission) || p.isOp());
		}

		return true;
	}

	public String getPermissionString(Permission permission) 
	{
		return "bmp." + permission.getNode().toLowerCase();
	}
}