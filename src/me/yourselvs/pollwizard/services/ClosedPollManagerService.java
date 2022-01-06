package me.yourselvs.pollwizard.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.factories.ClosedPollManagerFactory;
import me.yourselvs.pollwizard.factories.PollManagerFactory;
import me.yourselvs.pollwizard.model.ClosedPollAction;
import me.yourselvs.pollwizard.model.IClickHandler;
import me.yourselvs.pollwizard.model.ManagerAction;

public class ClosedPollManagerService implements IClickHandler {
	private Map<UUID, ClosedPollAction> playerActions = new HashMap<UUID, ClosedPollAction>();

	@Override
	public void handleClick(InventoryClickEvent e) {
		if (!e.isLeftClick() && !e.isRightClick())
			return;
		
		switch(e.getSlot()) {
		case ClosedPollManagerFactory.RETURN_SLOT:
			handleReturnClick(e);
			break;
		case ClosedPollManagerFactory.DELETE_SLOT:
			handleDeleteClick(e);
			break;
		case ClosedPollManagerFactory.CONFIRM_SLOT:
			handleConfirmClick(e);
			break;
		case ClosedPollManagerFactory.CANCEL_SLOT:
			handleCancelClick(e);
			break;
		}
	}
	
	public Map<UUID, ClosedPollAction> getPlayerActions() {
		return playerActions;
	}

	private void handleReturnClick(InventoryClickEvent e) {
		PollManagerFactory.openInventory(e.getWhoClicked());
	}

	private void handleDeleteClick(InventoryClickEvent e) {
		HumanEntity player = e.getWhoClicked();
		UUID playerId = player.getUniqueId();
		ClosedPollAction playerAction = playerActions.get(playerId);
		ClosedPollAction action = new ClosedPollAction(ManagerAction.DELETE, playerAction.poll);
		playerActions.put(playerId, action);	
		ClosedPollManagerFactory.openInventory(player);
	}

	private void handleConfirmClick(InventoryClickEvent e) {
		HumanEntity player = e.getWhoClicked();
		ClosedPollAction action = playerActions.get(player.getUniqueId());
		PollWizard.deleteClosedPoll(action.poll, player);
		playerActions.remove(player.getUniqueId());
		PollManagerFactory.openInventory(player);
	}

	private void handleCancelClick(InventoryClickEvent e) {
		HumanEntity player = e.getWhoClicked();
		UUID playerId = player.getUniqueId();
		ClosedPollAction playerAction = playerActions.get(playerId);
		ClosedPollAction action = new ClosedPollAction(ManagerAction.VIEW, playerAction.poll);
		playerActions.put(playerId, action);	
		ClosedPollManagerFactory.openInventory(player);
	}

}
