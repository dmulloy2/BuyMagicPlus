package net.dmulloy2.buymagicplus.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import lombok.Getter;
import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.buymagicplus.types.Package;
import net.dmulloy2.buymagicplus.types.ProcessingException;
import net.dmulloy2.io.UUIDFetcher;
import net.dmulloy2.types.Reloadable;
import net.dmulloy2.util.FormatUtil;
import net.dmulloy2.util.ItemUtil;
import net.dmulloy2.util.ListUtil;
import net.dmulloy2.util.Util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */

public class PackageHandler implements Reloadable
{
	private @Getter Map<UUID, List<String>> cached;
	private @Getter Map<String, Package> packages;

	private final BuyMagicPlus plugin;
	public PackageHandler(BuyMagicPlus plugin)
	{
		this.plugin = plugin;

		this.cached = new HashMap<>();
		this.packages = new HashMap<>();

		this.load();
		this.loadCache();
	}

	// ---- General Package Stuff

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
				plugin.getLogHandler().debug("Loading package \"{0}\"", name);

				@SuppressWarnings("unchecked") // No way to check this :I
				List<String> values = (List<String>) entry.getValue();
				List<ItemStack> items = ItemUtil.readItems(values, plugin);

				Package pack = new Package(name, items);
				packages.put(name.toLowerCase(), pack);
			}
			catch (Throwable ex)
			{
				plugin.getLogHandler().log(Level.SEVERE, Util.getUsefulStack(ex, "loading package: " + entry.getKey()));
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
	 * @param key Package name
	 */
	public final Package getPackage(String key)
	{
		return packages.get(key.toLowerCase());
	}

	/**
	 * Returns whether or not a package with this name exists
	 *
	 * @param key Package name to check
	 */
	public final boolean isValidPackage(String key)
	{
		return packages.containsKey(key.toLowerCase());
	}

	/**
	 * @return All of the loaded packages' names.
	 */
	public final List<String> getPackageKeys()
	{
		return new ArrayList<>(packages.keySet());
	}

	// ---- Cache

	private final void loadCache()
	{
		try
		{
			File file = new File(plugin.getDataFolder(), "cache.yml");
			if (! file.exists())
				file.createNewFile();

			YamlConfiguration config = new YamlConfiguration();
			config.load(file);

			for (Entry<String, Object> entry : config.getValues(true).entrySet())
			{
				UUID uuid = UUIDFetcher.getUUID(entry.getKey());
				List<String> packages = new ArrayList<>();

				@SuppressWarnings("unchecked") // No way to check this :I
				List<String> values = (List<String>) entry.getValue();
				for (String val : values)
				{
					if (! isValidPackage(val))
					{
						plugin.getLogHandler().log(Level.WARNING, plugin.getMessage("cache_missing_package"), val);
						continue;
					}

					packages.add(val);
				}

				cached.put(uuid, packages);
			}
		}
		catch (Throwable ex)
		{
			plugin.getLogHandler().log(Level.SEVERE, Util.getUsefulStack(ex, "loading cache"));
		}
	}

	/**
	 * Saves the cache to disk.
	 */
	public final void saveCache()
	{
		try
		{
			File file = new File(plugin.getDataFolder(), "cache.yml");
			if (file.exists())
				file.delete();

			file.createNewFile();

			YamlConfiguration config = new YamlConfiguration();
			config.load(file);

			for (Entry<UUID, List<String>> entry : cached.entrySet())
			{
				config.set(entry.getKey().toString(), entry.getValue());
			}

			config.save(file);
		}
		catch (Throwable ex)
		{
			plugin.getLogHandler().log(Level.SEVERE, Util.getUsefulStack(ex, "saving cache"));
		}
	}

	@Deprecated
	public final void cache(String key, String pack) throws Exception
	{
		UUID uuid = UUIDFetcher.getUUID(key);
		cache(uuid, pack);
	}

	public final void cache(UUID uuid, String pack)
	{
		if (cached.containsKey(uuid))
		{
			cached.get(uuid).add(pack);
		}
		else
		{
			cached.put(uuid, ListUtil.toList(pack));
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

	// ---- Processing

	public final void process(Player player, List<Package> packages)
	{
		for (Package pack : packages)
		{
			try
			{
				process(player, pack, false);
			}
			catch (ProcessingException ex)
			{
				player.sendMessage(FormatUtil.format("&cError: &4Failed to process package {0}: {1}", pack.getName(), ex));
			}
		}

		player.sendMessage(plugin.getPrefix() + FormatUtil.format(plugin.getMessage("thanks")));
	}

	public final void process(Player player, Package pack, boolean thank) throws ProcessingException
	{
		plugin.getLogHandler().log(plugin.getMessage("log_package_process"), pack.getName(), player.getName());

		pack.perform(player);

		if (hasCachedPackage(player.getName()))
		{
			removeFromCache(player.getName());
		}

		if (thank) player.sendMessage(plugin.getPrefix() + FormatUtil.format(plugin.getMessage("thanks")));
	}
}