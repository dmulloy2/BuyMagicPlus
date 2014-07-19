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

import java.util.MissingResourceException;
import java.util.logging.Level;

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
import net.dmulloy2.types.Reloadable;
import net.dmulloy2.util.FormatUtil;

import org.bukkit.plugin.PluginManager;

/**
 * @author dmulloy2
 */

public class BuyMagicPlus extends SwornPlugin implements Reloadable
{
	/** Handlers **/
	private @Getter ResourceHandler resourceHandler;
	private @Getter PackageHandler packageHandler;

	/** Global Prefix **/
	private @Getter String prefix = FormatUtil.format("&3[&eBuyMagic&3]&e ");

	@Override
	public void onEnable()
	{
		long start = System.currentTimeMillis();

		/** Configuration **/
		saveDefaultConfig();
		reloadConfig();

		/** Resource Handler / Messages **/
		saveResource("messages.properties", true);
		resourceHandler = new ResourceHandler(this, getClassLoader());

		/** Register Handlers **/
		logHandler = new LogHandler(this);
		commandHandler = new CommandHandler(this);
		packageHandler = new PackageHandler(this);
		permissionHandler = new PermissionHandler("bmp");

		/** Register Commands **/
		commandHandler.setCommandPrefix("bmp");
		commandHandler.registerPrefixedCommand(new CmdGive(this));
		commandHandler.registerPrefixedCommand(new CmdHelp(this));
		commandHandler.registerPrefixedCommand(new CmdInfo(this));
		commandHandler.registerPrefixedCommand(new CmdList(this));
		commandHandler.registerPrefixedCommand(new CmdReload(this));
		commandHandler.registerPrefixedCommand(new CmdVersion(this));

		/** Register Events **/
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(this), this);

		logHandler.log(getMessage("log_enabled"), getDescription().getFullName(), System.currentTimeMillis() - start);
	}

	@Override
	public void onDisable()
	{
		long start = System.currentTimeMillis();

		/** Cancel Tasks **/
		getServer().getScheduler().cancelTasks(this);

		/** Save Cache **/
		packageHandler.saveCache();

		logHandler.log(getMessage("log_disabled"), getDescription().getFullName(), System.currentTimeMillis() - start);
	}

	/**
	 * Gets a message from the messages.properties.
	 *
	 * @param string Message key
	 */
	public final String getMessage(String string)
	{
		try
		{
			return resourceHandler.getMessages().getString(string);
		}
		catch (MissingResourceException ex)
		{
			logHandler.log(Level.WARNING, getMessage("log_message_missing"), string);
			return FormatUtil.format("<Missing Message: {0}>", string);
		}
	}

	@Override
	public void reload()
	{
		reloadConfig();

		packageHandler.reload();
	}
}