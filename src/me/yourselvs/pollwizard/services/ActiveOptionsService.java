package me.yourselvs.pollwizard.services;

import org.bukkit.event.inventory.InventoryClickEvent;

import me.yourselvs.pollwizard.factories.ActiveOptionsFactory;
import me.yourselvs.pollwizard.factories.ActivePollManagerFactory;
import me.yourselvs.pollwizard.model.IClickHandler;

public class ActiveOptionsService implements IClickHandler {

	@Override
	public void handleClick(InventoryClickEvent e) {
		if (!e.isLeftClick() && !e.isRightClick())
			return;
		
		if(e.getSlot() == ActiveOptionsFactory.RETURN_SLOT) {
			ActivePollManagerFactory.openInventory(e.getWhoClicked());
		}	
	}

}
