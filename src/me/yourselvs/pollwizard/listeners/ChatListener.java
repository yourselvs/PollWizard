package me.yourselvs.pollwizard.listeners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.factories.OptionCreatorFactory;
import me.yourselvs.pollwizard.factories.PollCreatorFactory;
import me.yourselvs.pollwizard.model.PollCreator;
import me.yourselvs.pollwizard.util.ChatUtil;

// Synchronous chat events are deprecated, but opening an inventory must be synchronous
@SuppressWarnings("deprecation")
public class ChatListener implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(PlayerChatEvent e) {
		Player player = e.getPlayer();
		PollCreator poll = PollWizard.newPolls.get(player.getUniqueId());

		if (poll == null) {
			return;
		}
		
		int awaitingOption = poll.getAwaitingOption();
		
		String message = e.getMessage();
		if (poll.isAwaitingName()) {
			poll.setName(message);
			PollCreatorFactory.openInventory(player);
			ChatUtil.notifyReceived(player, "Question", message);
			e.setCancelled(true);
		}
		else if (awaitingOption >= 0) {
			List<String> options = poll.getOptions();
			
			if(awaitingOption >= options.size())
				options.add(message);
			else
				options.set(awaitingOption, message);

			OptionCreatorFactory.openInventory(player);
			ChatUtil.notifyReceived(player, "Option", message);
			e.setCancelled(true);
		}
	}
}
