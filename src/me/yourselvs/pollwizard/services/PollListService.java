package me.yourselvs.pollwizard.services;

import java.util.List;

import org.bukkit.event.inventory.InventoryClickEvent;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.factories.PollVoteFactory;
import me.yourselvs.pollwizard.model.ActivePoll;
import me.yourselvs.pollwizard.model.IClickHandler;

public class PollListService implements IClickHandler {

	@Override
	public void handleClick(InventoryClickEvent e) {
		if (!e.isLeftClick() && !e.isRightClick())
			return;
		
		List<ActivePoll> polls = PollWizard.activePolls;
		int slot = e.getSlot();
		
		if(slot < polls.size()) {
			ActivePoll poll = polls.get(slot);
			PollWizard.pollVoteService.getPlayerVoting().put(e.getWhoClicked().getUniqueId(), poll);
			PollVoteFactory.openInventory(e.getWhoClicked(), poll);
		}
	}

}
