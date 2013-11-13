package net.dmulloy2.buymagicplus.commands;

import java.util.ArrayList;
import java.util.List;

import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.buymagicplus.types.Permission;
import net.dmulloy2.buymagicplus.util.FormatUtil;

/**
 * @author dmulloy2
 */

public class CmdPackageList extends PaginatedCommand
{
	public CmdPackageList(BuyMagicPlus plugin)
	{
		super(plugin);
		this.name = "packagelist";
		this.aliases.add("packages");
		this.optionalArgs.add("page");
		this.description = "Shows available packages";
		this.linesPerPage = 6;
		this.permission = Permission.PACKAGE_LIST;
		
		this.usesPrefix = true;
	}

	@Override
	public int getListSize()
	{
		return plugin.getPackageHandler().getPackages().size();
	}

	@Override
	public String getHeader(int index) 
	{
		return FormatUtil.format(plugin.getMessage("packages_header"), index, getPageCount());
	}


	@Override
	public String getFooter()
	{
		return FormatUtil.format(plugin.getMessage("package_info"));
	}

	@Override
	public List<String> getLines(int startIndex, int endIndex) 
	{
		List<String> lines = new ArrayList<String>();
		for (int i = startIndex; i < endIndex && i < getListSize(); i++) 
		{
			lines.add("&b - &e" + plugin.getPackageHandler().getPackageKeys().get(i));
		}

		return lines;
	}

	@Override
	public String getLine(int index) 
	{
		return null;
	}
}