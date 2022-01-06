package me.yourselvs.pollwizard.services;

import java.util.ArrayList;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.factories.OptionCreatorFactory;
import me.yourselvs.pollwizard.factories.OptionEditorFactory;
import me.yourselvs.pollwizard.factories.PollCreatorFactory;
import me.yourselvs.pollwizard.model.IClickHandler;
import me.yourselvs.pollwizard.model.PollCreator;
import me.yourselvs.pollwizard.util.ChatUtil;

public class OptionCreatorService implements IClickHandler {

	@Override
	public void handleClick(InventoryClickEvent e) {
		if ((!e.isLeftClick() && !e.isRightClick()) || e.isShiftClick())
			return;
		
		HumanEntity player = e.getWhoClicked();
		PollCreator poll = PollWizard.newPolls.get(player.getUniqueId());
		int slot = e.getSlot();
		
		switch (slot) {
		case OptionCreatorFactory.NEW_OPTION_SLOT:
			if (poll.getOptions().size() >= PollCreator.MAX_OPTIONS)
				return;
			
			ChatUtil.notifyNew(player, "option");
			player.closeInventory();
			poll.setAwaitingOption(poll.getOptions().size());
			break;
		case OptionCreatorFactory.CLEAR_SLOT:
			poll.setOptions(new ArrayList<String>());
			OptionCreatorFactory.openInventory(player);
			break;
		case OptionCreatorFactory.RETURN_SLOT:
			PollCreatorFactory.openInventory(player);
			break;
		default:
			if(slot < poll.getOptions().size()) {
				poll.setEditingOption(slot);
				OptionEditorFactory.openInventory(player, slot);
			}
		}	
	}

}
