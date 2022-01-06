package me.yourselvs.pollwizard.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.factories.ActiveOptionsFactory;
import me.yourselvs.pollwizard.factories.ActivePollManagerFactory;
import me.yourselvs.pollwizard.factories.PollManagerFactory;
import me.yourselvs.pollwizard.model.ActivePollAction;
import me.yourselvs.pollwizard.model.IClickHandler;
import me.yourselvs.pollwizard.model.ManagerAction;

public class ActivePollManagerService implements IClickHandler {
	private Map<UUID, ActivePollAction> playerActions = new HashMap<UUID, ActivePollAction>();

	@Override
	public void handleClick(InventoryClickEvent e) {
		if (!e.isLeftClick() && !e.isRightClick())
			return;
		
		switch(e.getSlot()) {
		case ActivePollManagerFactory.OPTIONS_SLOT:
			handleOptionsClick(e);
			break;
		case ActivePollManagerFactory.SUBMIT_SLOT:
			handleSubmitClick(e);
			break;
		case ActivePollManagerFactory.CANCEL_SLOT:
			handleCancelClick(e);
			break;
		case ActivePollManagerFactory.DELETE_SLOT:
			handleDeleteClick(e);
			break;
		}
	}
	
	public Map<UUID, ActivePollAction> getPlayerActions() {
		return playerActions;
	}

	private void handleOptionsClick(InventoryClickEvent e) {
		HumanEntity player = e.getWhoClicked();
		ActivePollAction action = playerActions.get(player.getUniqueId());
		ActiveOptionsFactory.openInventory(player, action.poll);
	}

	private void handleSubmitClick(InventoryClickEvent e) {
		HumanEntity player = e.getWhoClicked();
		ActivePollAction playerAction = playerActions.get(player.getUniqueId());
		
		if(playerAction.action == ManagerAction.VIEW) {
			ActivePollAction newAction = new ActivePollAction(ManagerAction.CLOSE, playerAction.poll); 
			playerActions.put(player.getUniqueId(), newAction);
			ActivePollManagerFactory.openInventory(player);
		}
		else if(playerAction.action == ManagerAction.CLOSE) {
			PollWizard.closePoll(playerAction.poll, player);
			playerActions.remove(player.getUniqueId());
			PollManagerFactory.openInventory(player);
		}
		else {
			PollWizard.deleteActivePoll(playerAction.poll, player);
			playerActions.remove(player.getUniqueId());
			PollManagerFactory.openInventory(player);
		}		
	}

	private void handleCancelClick(InventoryClickEvent e) {
		HumanEntity player = e.getWhoClicked();
		ActivePollAction playerAction = playerActions.get(player.getUniqueId());
		
		if(playerAction.action == ManagerAction.VIEW) {
			playerActions.remove(player.getUniqueId());
			PollManagerFactory.openInventory(player);
		}
		else {
			ActivePollAction newAction = new ActivePollAction(ManagerAction.VIEW, playerAction.poll); 
			playerActions.put(player.getUniqueId(), newAction);
			ActivePollManagerFactory.openInventory(player);
		}	
	}

	private void handleDeleteClick(InventoryClickEvent e) {
		HumanEntity player = e.getWhoClicked();
		ActivePollAction playerAction = playerActions.get(player.getUniqueId());
		ActivePollAction newAction = new ActivePollAction(ManagerAction.DELETE, playerAction.poll); 
		playerActions.put(player.getUniqueId(), newAction);
		ActivePollManagerFactory.openInventory(player);
	}
}
