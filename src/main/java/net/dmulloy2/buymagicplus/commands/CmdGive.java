package net.dmulloy2.buymagicplus.commands;

import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.buymagicplus.types.Package;
import net.dmulloy2.buymagicplus.types.Permission;
import net.dmulloy2.util.Util;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author dmulloy2
 */

public class CmdGive extends BuyMagicPlusCommand
{
	public CmdGive(BuyMagicPlus plugin)
	{
		super(plugin);
		this.name = "give";
		this.requiredArgs.add("player");
		this.requiredArgs.add("package");
		this.description = "Give a player a package";
		this.permission = Permission.GIVE;
		this.usesPrefix = true;
	}

	@Override
	public void perform()
	{
		// Check to make sure a valid player was supplied
		OfflinePlayer offlineTarget = Util.matchOfflinePlayer(args[0]);
		if (offlineTarget == null)
		{
			err(getMessage("player_not_found"), args[0]);
			return;
		}

		// Make sure a valid package was supplied
		if (! plugin.getPackageHandler().isValidPackage(args[1]))
		{
			err(getMessage("package_not_found"), args[1]);
			return;
		}

		Package pack = plugin.getPackageHandler().getPackage(args[1]);

		// Cache if the player is not online
		if (! offlineTarget.isOnline())
		{
			plugin.getLogHandler().log(getMessage("give_cache"), offlineTarget.getName());
			plugin.getPackageHandler().cache(offlineTarget.getName(), args[1]);
			return;
		}

		// Process
		plugin.getPackageHandler().process((Player) offlineTarget, pack, true);
	}
}