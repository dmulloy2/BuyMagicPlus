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
import net.dmulloy2.buymagicplus.util.ItemUtil;
import net.dmulloy2.buymagicplus.util.Util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */

public class PackageHandler
{
	private @Getter HashMap<String, String> cached;
	private @Getter HashMap<String, Package> packages;
	
	private final BuyMagicPlus plugin;
	public PackageHandler(BuyMagicPlus plugin)
	{
		this.plugin = plugin;

		this.cached = new HashMap<String, String>();
		this.packages = new HashMap<String, Package>();

		load();
		loadCache();
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
			String name = entry.getKey();

			@SuppressWarnings("unchecked")
			List<String> values = (List<String>) entry.getValue();

			List<ItemStack> items = new ArrayList<ItemStack>();

			for (String value : values)
			{
				ItemStack item = ItemUtil.readItem(value);
				if (item != null)
					items.add(item);
			}

			Package pack = new Package(items);
			packages.put(name, pack);
		}

		plugin.getLogHandler().log(plugin.getMessage("packages_loaded"), System.currentTimeMillis() - start);
	}

	/**
	 * Reloads all packages
	 */
	public final void reload()
	{
		packages.clear();

		load();
	}

	/**
	 * Returns the package associated with the given key
	 * 
	 * @param key - Package name
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
	 * @param key - Package name to check
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
			if (! file.exists())
			{
				file.createNewFile();
			}

			YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
			for (Entry<String, Object> value : fc.getValues(true).entrySet())
			{
				String playerName = value.getKey().toLowerCase();

				String packName = (String) value.getValue();
				if (! isValidPackage(packName))
				{
					plugin.getLogHandler().log(Level.WARNING, plugin.getMessage("cache_missing_package"), playerName, packName);
					continue;
				}

				cached.put(playerName, packName);
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
			for (Entry<String, String> entry : cached.entrySet())
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
		cached.put(key, pack);
	}

	public final Package getCachedPackage(String key)
	{
		if (cached.containsKey(key.toLowerCase()))
		{
			return getPackage(cached.get(key));
		}

		return null;
	}

	public final boolean hasCachedPackage(String key)
	{
		return cached.containsKey(key.toLowerCase());
	}
}