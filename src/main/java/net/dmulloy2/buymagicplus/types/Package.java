package net.dmulloy2.buymagicplus.types;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
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
	private @Getter String name;
	private @Getter List<ItemStack> items;

	public Package(String name, List<ItemStack> items)
	{
		this.name = name;
		this.items = items;
	}

	public void perform(Player player) throws ProcessingException
	{
		try
		{
			if (items.isEmpty())
			{
				throw new Exception("Empty Package");
			}
		
			// Attempt to add the items	
			Map<Integer, ItemStack> leftover = InventoryUtil.addItems(player.getInventory(), items.toArray(new ItemStack[0]));
	
			// Drop items that don't fit
			for (Entry<Integer, ItemStack> leftoverItem : leftover.entrySet())
			{
				player.getWorld().dropItemNaturally(player.getLocation(), leftoverItem.getValue());
			}
		}
		catch (Exception e)
		{
			throw new ProcessingException(e, this);
		}
	}
}