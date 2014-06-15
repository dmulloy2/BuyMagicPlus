package net.dmulloy2.buymagicplus.commands;

import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.buymagicplus.types.Permission;

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
		
		StringBuilder line = new StringBuilder();
		line.append(getMessage("version_author") + " ");

		for (String author : plugin.getDescription().getAuthors())
		{
			line.append("&e" + author + ", ");
		}
		
		if (line.lastIndexOf(",") >= 0)
		{
			line.deleteCharAt(line.lastIndexOf(","));
		}

		sendMessage(line.toString());
		
		sendMessage(getMessage("version_loaded"), plugin.getDescription().getFullName());
//		sendMessage(getMessage("version_update"), plugin.updateNeeded() ? "true" : "false");
//		sendMessage(getMessage("version_download"));
	}
}