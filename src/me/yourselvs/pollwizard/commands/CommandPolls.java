package me.yourselvs.pollwizard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.factories.PollListFactory;

public class CommandPolls implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			PollWizard.resetState(player);

			if (player.hasPermission(PollWizard.getPermissionPolls())) {
				PollListFactory.openInventory(player);
			}
		}

		return true;
	}

}
