/**
 * BuyMagicPlus - a bukkit plugin
 * Copyright (C) 2013 - 2014 dmulloy2
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.dmulloy2.buymagicplus;

import java.io.File;

import lombok.Getter;
import net.dmulloy2.SwornPlugin;
import net.dmulloy2.buymagicplus.commands.CmdGive;
import net.dmulloy2.buymagicplus.commands.CmdInfo;
import net.dmulloy2.buymagicplus.commands.CmdList;
import net.dmulloy2.buymagicplus.commands.CmdReload;
import net.dmulloy2.buymagicplus.commands.CmdVersion;
import net.dmulloy2.buymagicplus.handlers.PackageHandler;
import net.dmulloy2.buymagicplus.listeners.PlayerListener;
import net.dmulloy2.commands.CmdHelp;
import net.dmulloy2.handlers.CommandHandler;
import net.dmulloy2.handlers.LogHandler;
import net.dmulloy2.handlers.PermissionHandler;
import net.dmulloy2.handlers.ResourceHandler;
import net.dmulloy2.util.FormatUtil;

import org.bukkit.plugin.PluginManager;

/**
 * @author dmulloy2
 */

public class BuyMagicPlus extends SwornPlugin
{
	// Handlers
	private @Getter ResourceHandler resourceHandler;
	private @Getter PackageHandler packageHandler;

	// Global prefix
	private @Getter String prefix = FormatUtil.format("&3[&eBuyMagic&3]&e ");

	@Override
	public void onEnable()
	{
		long start = System.currentTimeMillis();

		// Register log handler first
		logHandler = new LogHandler(this);

		// Configuration
		saveDefaultConfig();
		reloadConfig();

		// Messages
		File messages = new File(getDataFolder(), "messages.properties");
		if (messages.exists())
			messages.delete();

		resourceHandler = new ResourceHandler(this, getClassLoader());

		// Register other handlers
		permissionHandler = new PermissionHandler("bmp");
		commandHandler = new CommandHandler(this);
		packageHandler = new PackageHandler(this);

		// Register commands
		commandHandler.setCommandPrefix("bmp");
		commandHandler.registerPrefixedCommand(new CmdGive(this));
		commandHandler.registerPrefixedCommand(new CmdHelp(this));
		commandHandler.registerPrefixedCommand(new CmdInfo(this));
		commandHandler.registerPrefixedCommand(new CmdList(this));
		commandHandler.registerPrefixedCommand(new CmdReload(this));
		commandHandler.registerPrefixedCommand(new CmdVersion(this));

		// Register events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(this), this);

		logHandler.log(getMessage("log_enabled"), getDescription().getFullName(), System.currentTimeMillis() - start);
	}

	@Override
	public void onDisable()
	{
		long start = System.currentTimeMillis();

		// Cancel tasks
		getServer().getScheduler().cancelTasks(this);

		// Save the cache
		packageHandler.saveCache();

		logHandler.log(getMessage("log_disabled"), getDescription().getFullName(), System.currentTimeMillis() - start);
	}

	/**
	 * Gets a message from the messages.properties.
	 *
	 * @param key Message key
	 */
	public final String getMessage(String key)
	{
		return resourceHandler.getMessage(key);
	}

	@Override
	public void reload()
	{
		reloadConfig();

		packageHandler.reload();
	}
}