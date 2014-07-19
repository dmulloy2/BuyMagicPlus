package net.dmulloy2.buymagicplus.commands;

import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.buymagicplus.types.Permission;
import net.dmulloy2.types.StringJoiner;

/**
 * @author dmulloy2
 */

public class CmdVersion extends BuyMagicPlusCommand
{
	public CmdVersion(BuyMagicPlus plugin)
	{
		super(plugin);
		this.name = "version";
		this.aliases.add("v");
		this.description = "Display BuyMagicPlus version";
		this.permission = Permission.VERSION;
		this.usesPrefix = true;
	}

	@Override
	public void perform()
	{
		sendMessage(getMessage("version_header"));

		StringJoiner authors = new StringJoiner("&b, &e");
		authors.appendAll(plugin.getDescription().getAuthors());
		sendMessage(getMessage("version_author"), authors.toString());

		sendMessage(getMessage("version_loaded"), plugin.getDescription().getFullName());
	}
}