package net.dmulloy2.buymagicplus.listeners;

import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.buymagicplus.types.Package;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author dmulloy2
 */

public class PlayerListener implements Listener
{
	private final BuyMagicPlus plugin;
	public PlayerListener(BuyMagicPlus plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();

		// If they have a cached package, give it to them
		if (plugin.getPackageHandler().hasCachedPackage(player.getName()))
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Package pack = plugin.getPackageHandler().getCachedPackage(player.getName());
					pack.perform(player);
				}
			}.runTaskLater(plugin, 60L);
		}
	}
}