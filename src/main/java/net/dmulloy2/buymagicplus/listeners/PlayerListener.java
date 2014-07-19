package net.dmulloy2.buymagicplus.listeners;

import java.util.List;

import lombok.AllArgsConstructor;
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

@AllArgsConstructor
public class PlayerListener implements Listener
{
	private final BuyMagicPlus plugin;

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
					List<Package> packages = plugin.getPackageHandler().getCachedPackages(player.getName());
					plugin.getPackageHandler().process(player, packages);
					plugin.getPackageHandler().removeFromCache(player.getName());
				}
			}.runTaskLater(plugin, 60L);
		}
	}
}