package me.yourselvs.pollwizard.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.yourselvs.pollwizard.factories.PollListFactory;
import me.yourselvs.pollwizard.factories.PollVoteFactory;
import me.yourselvs.pollwizard.model.ActivePoll;
import me.yourselvs.pollwizard.model.IClickHandler;

public class PollVoteService implements IClickHandler {
	private Map<UUID, ActivePoll> playerVoting = new HashMap<UUID, ActivePoll>();

	@Override
	public void handleClick(InventoryClickEvent e) {
		if (!e.isLeftClick() && !e.isRightClick())
			return;
		
		HumanEntity player = e.getWhoClicked();
		UUID playerId = player.getUniqueId();
		ActivePoll poll = getPlayerVoting().get(playerId);
		int slot = e.getSlot();
		
		switch(slot) {
		case PollVoteFactory.CANCEL_SLOT:
			getPlayerVoting().remove(playerId);
			poll.resetPlayerPendingVote(playerId);
			PollListFactory.openInventory(player);
			break;
		case PollVoteFactory.SUBMIT_SLOT:
			if(poll.getPlayerPendingVote(playerId) == null)
				break;
			poll.confirmPlayerPendingVote(playerId);
			PollVoteFactory.openInventory(player, poll);
			break;
		default:
			if(slot < poll.getOptions().size() && poll.getPlayerVote(playerId) == null) {
				poll.setPlayerPendingVote(playerId, slot);
				PollVoteFactory.openInventory(player, poll);
			}
		}
	}

	public Map<UUID, ActivePoll> getPlayerVoting() {
		return playerVoting;
	}

}
