package net.dmulloy2.buymagicplus.handlers;

import java.util.ArrayList;
import java.util.List;

import net.dmulloy2.buymagicplus.BuyMagicPlus;
import net.dmulloy2.buymagicplus.commands.BuyMagicPlusCommand;
import net.dmulloy2.buymagicplus.commands.CmdHelp;
import net.dmulloy2.buymagicplus.util.FormatUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

/**
 * @author dmulloy2
 */

public class CommandHandler implements CommandExecutor 
{
	private final BuyMagicPlus plugin;
	// Only need the name of command prefix - all other aliases listed in plugin.yml will be usable
	private String commandPrefix;
	private List<BuyMagicPlusCommand> registeredPrefixedCommands;
	private List<BuyMagicPlusCommand> registeredCommands;
	
	public CommandHandler(BuyMagicPlus plugin)
	{
		this.plugin = plugin;
		registeredCommands = new ArrayList<BuyMagicPlusCommand>();
	}
	
	public void registerCommand(BuyMagicPlusCommand command) 
	{
		PluginCommand pluginCommand = plugin.getCommand(command.getName());
		if (pluginCommand != null)
		{
			pluginCommand.setExecutor(command);
			registeredCommands.add(command);
		} 
		else
		{
			plugin.getLogHandler().log(plugin.getMessage("log_command_missing"), command.getName());
		}
	}

	public void registerPrefixedCommand(BuyMagicPlusCommand command)
	{
		if (commandPrefix != null)
			registeredPrefixedCommands.add(command);
	}

	public List<BuyMagicPlusCommand> getRegisteredCommands() 
	{
		return registeredCommands;
	}

	public List<BuyMagicPlusCommand> getRegisteredPrefixedCommands()
	{
		return registeredPrefixedCommands;
	}

	public String getCommandPrefix() 
	{
		return commandPrefix;
	}

	public void setCommandPrefix(String commandPrefix) 
	{
		this.commandPrefix = commandPrefix;
		registeredPrefixedCommands = new ArrayList<BuyMagicPlusCommand>();
		plugin.getCommand(commandPrefix).setExecutor(this);
	}

	public boolean usesCommandPrefix() 
	{
		return commandPrefix != null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		List<String> argsList = new ArrayList<String>();
		
		if (args.length > 0) 
		{
			String commandName = args[0];
			for (int i = 1; i < args.length; i++)
				argsList.add(args[i]);
			
			for (BuyMagicPlusCommand command : registeredPrefixedCommands) 
			{
				if (commandName.equalsIgnoreCase(command.getName()) || command.getAliases().contains(commandName.toLowerCase()))
				{
					command.execute(sender, argsList.toArray(new String[0]));
					return true;
				}
			}
			
			sender.sendMessage(FormatUtil.format(plugin.getMessage("error") + plugin.getMessage("unknown_command"), args[0]));
		} 
		else 
		{
			new CmdHelp(plugin).execute(sender, args);
		}
		
		return true;
	}
}