package me.yourselvs.pollwizard.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.model.ActivePoll;
import me.yourselvs.pollwizard.util.ChatUtil;

public class JoinListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		int nonVoted = 0;
		
		for(ActivePoll poll : PollWizard.activePolls) {
			if(poll.getPlayerVote(event.getPlayer().getUniqueId()) == null) {
				nonVoted++;
			}
		}
		
		if (nonVoted > 0) {
			String activePolls = nonVoted == 1 ?
					"is " + ChatColor.LIGHT_PURPLE + "1" + ChatColor.YELLOW + " active poll" :
					"are " + ChatColor.LIGHT_PURPLE + nonVoted + ChatColor.YELLOW + " active polls";
			ChatUtil.message(event.getPlayer(), ChatColor.YELLOW + "There " 
					+ activePolls + " you haven't voted in. Use " 
					+ ChatColor.LIGHT_PURPLE + "/polls" 
					+ ChatColor.YELLOW + " to vote.");
		}
	}
}
