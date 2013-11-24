/**
 * BuyMagicPlus - a bukkit plugin 
 * Copyright (C) 2013 dmulloy2
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
import net.dmulloy2.buymagicplus.commands.CmdGive;
import net.dmulloy2.buymagicplus.commands.CmdHelp;
import net.dmulloy2.buymagicplus.commands.CmdPackageInfo;
import net.dmulloy2.buymagicplus.commands.CmdPackageList;
import net.dmulloy2.buymagicplus.commands.CmdReload;
import net.dmulloy2.buymagicplus.commands.CmdVersion;
import net.dmulloy2.buymagicplus.handlers.CommandHandler;
import net.dmulloy2.buymagicplus.handlers.LogHandler;
import net.dmulloy2.buymagicplus.handlers.PackageHandler;
import net.dmulloy2.buymagicplus.handlers.PermissionHandler;
import net.dmulloy2.buymagicplus.handlers.ResourceHandler;
import net.dmulloy2.buymagicplus.listeners.PlayerListener;
import net.dmulloy2.buymagicplus.types.Reloadable;
import net.dmulloy2.buymagicplus.util.FormatUtil;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author dmulloy2
 */

public class BuyMagicPlus extends JavaPlugin implements Reloadable
{
	/** Handlers **/
	private @Getter PermissionHandler permissionHandler;
	private @Getter ResourceHandler resourceHandler;
	private @Getter PackageHandler packageHandler;
	private @Getter CommandHandler commandHandler;
	private @Getter LogHandler logHandler;

	/** Global Prefix Variable **/
	private @Getter String prefix = FormatUtil.format("&6[&4&lBMP&6] ");

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
		permissionHandler = new PermissionHandler();

		/** Register Commands **/
		commandHandler.setCommandPrefix("bmp");
		commandHandler.registerPrefixedCommand(new CmdGive(this));
		commandHandler.registerPrefixedCommand(new CmdHelp(this));
		commandHandler.registerPrefixedCommand(new CmdPackageInfo(this));
		commandHandler.registerPrefixedCommand(new CmdPackageList(this));
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
     * Gets a message from the messages.properties
     * 
     * @param string - Message key
     */
	public final String getMessage(String string) 
	{
		try
		{
			return resourceHandler.getMessages().getString(string);
		} 
		catch (MissingResourceException ex) 
		{
			logHandler.log(Level.WARNING, getMessage("log_message_missing"),  string);
			return null;
		}
	}

	@Override
	public void reload()
	{
		reloadConfig();

		packageHandler.reload();
	}
}