package net.dmulloy2.buymagicplus.commands;

import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.buymagicplus.types.Permission;
import net.dmulloy2.buymagicplus.types.Reloadable;

/**
 * @author dmulloy2
 */

public class CmdReload extends BuyMagicPlusCommand implements Reloadable
{
	public CmdReload(BuyMagicPlus plugin)
	{
		super(plugin);
		this.name = "reload";
		this.aliases.add("rl");
		this.description = "Reload BuyMagicPlus";
		this.permission = Permission.RELOAD;

		this.usesPrefix = true;
	}
	
	@Override
	public void perform()
	{
		reload();
	}

	@Override
	public void reload()
	{
		long start = System.currentTimeMillis();
		
		sendpMessage(getMessage("reload_start"));
		
		plugin.reload();
		
		sendpMessage(getMessage("reload_finish"), System.currentTimeMillis() - start);
	}
}