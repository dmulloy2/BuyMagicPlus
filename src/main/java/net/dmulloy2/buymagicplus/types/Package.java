package net.dmulloy2.buymagicplus.types;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dmulloy2.util.InventoryUtil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a buyable BuyMagicPlus package
 * 
 * @author dmulloy2
 */

@Getter
@AllArgsConstructor
public class Package
{
	private String name;
	private List<ItemStack> items;

	public final void perform(Player player) throws ProcessingException
	{
		try
		{
			if (items.isEmpty())
			{
				throw new Exception("Empty Package");
			}
		
			// Attempt to add the items	
			Map<Integer, ItemStack> leftover = InventoryUtil.giveItems(player, items.toArray(new ItemStack[0]));
	
			// Drop items that don't fit
			for (Entry<Integer, ItemStack> leftoverItem : leftover.entrySet())
			{
				player.getWorld().dropItemNaturally(player.getLocation(), leftoverItem.getValue());
			}
		}
		catch (Throwable ex)
		{
			throw new ProcessingException(ex);
		}
	}
}