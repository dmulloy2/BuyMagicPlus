package net.dmulloy2.buymagicplus.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import lombok.Getter;
import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.buymagicplus.types.Package;
import net.dmulloy2.buymagicplus.types.ProcessingException;
import net.dmulloy2.buymagicplus.types.Reloadable;
import net.dmulloy2.buymagicplus.util.FormatUtil;
import net.dmulloy2.buymagicplus.util.ItemUtil;
import net.dmulloy2.buymagicplus.util.Util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */

public class PackageHandler implements Reloadable
{
	private @Getter HashMap<String, List<String>> cached;
	private @Getter HashMap<String, Package> packages;

	private final BuyMagicPlus plugin;
	public PackageHandler(BuyMagicPlus plugin)
	{
		this.plugin = plugin;

		this.cached = new HashMap<String, List<String>>();
		this.packages = new HashMap<String, Package>();

		this.load();
		this.loadCache();
	}

	// ---- General Package Stuff ---- //

	/**
	 * Loads all packages
	 */
	private final void load()
	{
		long start = System.currentTimeMillis();

		plugin.getLogHandler().log(plugin.getMessage("package_loading"));

		Map<String, Object> map = plugin.getConfig().getConfigurationSection("packages").getValues(true);

		for (Entry<String, Object> entry : map.entrySet())
		{
			try
			{
				String name = entry.getKey();
	
				plugin.getLogHandler().debug("Attempting to load package \"{0}\"", name);
	
				@SuppressWarnings("unchecked")
				List<String> values = (List<String>) entry.getValue();
	
				List<ItemStack> items = new ArrayList<ItemStack>();
	
				for (String value : values)
				{
					ItemStack item = ItemUtil.readItem(value);
					if (item != null)
						items.add(item);
				}
	
				Package pack = new Package(name, items);
				packages.put(name, pack);
			}
			catch (Exception e)
			{
				plugin.getLogHandler().log(Level.SEVERE, Util.getUsefulStack(e, "loading package " + entry.getKey()));
			}
		}

		plugin.getLogHandler().log(plugin.getMessage("packages_loaded"), packages.size(), System.currentTimeMillis() - start);
	}

	/**
	 * Reloads all packages
	 */
	@Override
	public final void reload()
	{
		packages.clear();

		load();
	}

	/**
	 * Returns the package associated with the given key
	 * 
	 * @param key
	 *        - Package name
	 */
	public final Package getPackage(String key)
	{
		if (packages.containsKey(key.toLowerCase()))
		{
			return packages.get(key.toLowerCase());
		}

		return null;
	}

	/**
	 * Returns whether or not a package with this name exists
	 * 
	 * @param key
	 *        - Package name to check
	 */
	public final boolean isValidPackage(String key)
	{
		return packages.containsKey(key.toLowerCase());
	}

	public final List<String> getPackageKeys()
	{
		List<String> ret = new ArrayList<String>();

		ret.addAll(packages.keySet());

		return ret;
	}

	// ---- Package Caching ---- //

	private final void loadCache()
	{
		try
		{
			File file = new File(plugin.getDataFolder(), "cache.yml");
			if (!file.exists())
			{
				file.createNewFile();
			}

			YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
			for (Entry<String, Object> value : fc.getValues(true).entrySet())
			{
				String playerName = value.getKey().toLowerCase();
				List<String> packages = new ArrayList<String>();

				@SuppressWarnings("unchecked")
				List<String> values = (List<String>) value.getValue();
				for (String val : values)
				{
					if (!isValidPackage(val))
					{
						plugin.getLogHandler().log(Level.WARNING, plugin.getMessage("cache_missing_package"), playerName, val);
						continue;
					}

					packages.add(val);
				}

				cached.put(playerName, packages);
			}
		}
		catch (Exception e)
		{
			plugin.getLogHandler().log(Level.SEVERE, Util.getUsefulStack(e, "loading cache"));
		}
	}

	public final void saveCache()
	{
		try
		{
			File file = new File(plugin.getDataFolder(), "cache.yml");
			if (file.exists())
			{
				file.delete();
			}

			file.createNewFile();

			YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
			for (Entry<String, List<String>> entry : cached.entrySet())
			{
				fc.set(entry.getKey(), entry.getValue());
			}

			fc.save(file);
		}
		catch (Exception e)
		{
			plugin.getLogHandler().log(Level.SEVERE, Util.getUsefulStack(e, "saving cache"));
		}
	}

	public final void cache(String key, String pack)
	{
		if (cached.containsKey(key.toLowerCase()))
		{
			cached.get(key.toLowerCase()).add(pack);
		}
		else
		{
			cached.put(key.toLowerCase(), Util.toList(pack));
		}
	}

	public final List<Package> getCachedPackages(String key)
	{
		List<Package> ret = new ArrayList<Package>();

		if (cached.containsKey(key.toLowerCase()))
		{
			for (String s : cached.get(key.toLowerCase()))
			{
				Package pack = getPackage(s);
				if (pack != null)
					ret.add(pack);
			}
		}

		return ret;
	}

	public final boolean hasCachedPackage(String key)
	{
		return cached.containsKey(key.toLowerCase());
	}

	public final void removeFromCache(String key)
	{
		if (hasCachedPackage(key))
		{
			cached.get(key).clear();
			cached.remove(key.toLowerCase());
		}
	}

	// ---- Processing ---- //

	public final void process(Player player, List<Package> packages)
	{
		for (Package pack : packages)
		{
			process(player, pack, false);
		}

		player.sendMessage(plugin.getPrefix() + 
				FormatUtil.format(plugin.getMessage("thanks")));
	}

	public final void process(Player player, Package pack, boolean tell)
	{
		plugin.getLogHandler().log(plugin.getMessage("log_package_process"), pack.getName(), player.getName());

		try
		{
			pack.perform(player);
		}
		catch (ProcessingException e)
		{
			if (tell)
				player.sendMessage(plugin.getPrefix() + 
						FormatUtil.format("&cCould not process package {0}: {1}", pack.getName(), e.getMessage()));
			
			plugin.getLogHandler().log(Level.SEVERE, Util.getUsefulStack(e, "processing package " + pack.getName()));
		}

		if (hasCachedPackage(player.getName()))
		{
			removeFromCache(player.getName());
		}

		if (tell)
			player.sendMessage(plugin.getPrefix() +
					FormatUtil.format(plugin.getMessage("thanks")));
	}
}