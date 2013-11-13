package net.dmulloy2.buymagicplus.types;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import net.dmulloy2.buymagicplus.util.FormatUtil;
import net.dmulloy2.buymagicplus.util.InventoryUtil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a buyable BuyMagicPlus package
 * 
 * @author dmulloy2
 */

public class Package
{
	private @Getter List<ItemStack> items;
	public Package(List<ItemStack> items)
	{
		this.items = items;
	}

	public final void perform(Player player)
	{
		for (ItemStack item : items)
		{
			// Attempt to add the item
			Map<Integer, ItemStack> leftover = InventoryUtil.addItems(player.getInventory(), item);

			// Drop items that don't fit
			for (Entry<Integer, ItemStack> leftoverItem : leftover.entrySet())
			{
				player.getWorld().dropItemNaturally(player.getLocation(), leftoverItem.getValue());
			}
		}

		player.sendMessage(FormatUtil.format("&6[&4&lBMP&6] &aThanks for using BuyMagicPlus!"));
	}
}