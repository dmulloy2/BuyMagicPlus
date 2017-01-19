package net.dmulloy2.buymagicplus.commands;

import org.apache.commons.lang.WordUtils;
import org.bukkit.inventory.ItemStack;

import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.buymagicplus.types.Package;
import net.dmulloy2.buymagicplus.types.Permission;
import net.dmulloy2.util.ItemUtil;
import net.dmulloy2.util.MaterialUtil;

/**
 * @author dmulloy2
 */

public class CmdInfo extends BuyMagicPlusCommand
{
	public CmdInfo(BuyMagicPlus plugin)
	{
		super(plugin);
		this.name = "info";
		this.aliases.add("packageinfo");
		this.addRequiredArg("package");
		this.description = "Shows information on a specific package";
		this.permission = Permission.INFO;
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
			sendMessage(getMessage("package_info_item_format"), MaterialUtil.getName(stack), stack.getAmount(),
					ItemUtil.getEnchantments(stack));
		}
	}
}