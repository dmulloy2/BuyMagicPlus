package net.dmulloy2.buymagicplus.commands;

import net.dmulloy2.buymagicplus.BuyMagicPlus;

/**
 * @author dmulloy2
 */

public abstract class PaginatedCommand extends net.dmulloy2.commands.PaginatedCommand
{
	protected final BuyMagicPlus plugin;

	public PaginatedCommand(BuyMagicPlus plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}
}