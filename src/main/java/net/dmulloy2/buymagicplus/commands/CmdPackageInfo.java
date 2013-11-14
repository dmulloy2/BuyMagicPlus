package net.dmulloy2.buymagicplus.commands;

import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.buymagicplus.types.Package;
import net.dmulloy2.buymagicplus.types.Permission;
import net.dmulloy2.buymagicplus.util.FormatUtil;
import net.dmulloy2.buymagicplus.util.ItemUtil;

import org.apache.commons.lang.WordUtils;
import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */

public class CmdPackageInfo extends BuyMagicPlusCommand
{

	public CmdPackageInfo(BuyMagicPlus plugin)
	{
		super(plugin);
		this.name = "packageinfo";
		this.aliases.add("package");
		this.requiredArgs.add("package");
		this.description = "Shows information on a specific package";
		this.permission = Permission.PACKAGE_INFO;

		this.usesPrefix = true;
	}

	@Override
	public void perform()
	{
		Package pack = plugin.getPackageHandler().getPackage(args[0]);
		if (pack == null)
		{
			err(getMessage("package_not_found"), args[0]);
			return;
		}

		sendMessage(getMessage("package_info_header"), WordUtils.capitalize(args[0]));

		for (ItemStack stack : pack.getItems())
		{
			sendMessage(getMessage("package_info_item_format"), FormatUtil.getFriendlyName(stack.getType()), stack.getAmount(),
					ItemUtil.getEnchantments(stack));
		}
	}
}