package me.yourselvs.pollwizard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.factories.PollCreatorFactory;
import me.yourselvs.pollwizard.factories.PollManagerFactory;

public class CommandPoll implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			PollWizard.resetState(player);
			
			if(args.length == 0)
				PollManagerFactory.openInventory(player);
			else if(args[0] == "create")
				PollCreatorFactory.openInventory(player);
		}

		return true;
	}

}
