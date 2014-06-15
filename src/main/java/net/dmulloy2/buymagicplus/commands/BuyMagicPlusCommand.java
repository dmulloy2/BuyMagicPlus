package net.dmulloy2.buymagicplus.commands;

import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.commands.Command;

/**
 * @author dmulloy2
 */

public abstract class BuyMagicPlusCommand extends Command
{
	protected final BuyMagicPlus plugin;

	public BuyMagicPlusCommand(BuyMagicPlus plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	protected final String getMessage(String msg)
	{
		return plugin.getMessage(msg);
	}
}