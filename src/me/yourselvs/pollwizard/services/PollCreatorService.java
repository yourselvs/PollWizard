package me.yourselvs.pollwizard.services;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.factories.OptionCreatorFactory;
import me.yourselvs.pollwizard.factories.PollCreatorFactory;
import me.yourselvs.pollwizard.factories.PollManagerFactory;
import me.yourselvs.pollwizard.model.IClickHandler;
import me.yourselvs.pollwizard.model.PollCreator;
import me.yourselvs.pollwizard.util.ChatUtil;

public class PollCreatorService implements IClickHandler {

	@Override
	public void handleClick(InventoryClickEvent e) {
		if ((!e.isLeftClick() && !e.isRightClick()) || e.isShiftClick())
			return;
		
		HumanEntity player = e.getWhoClicked();
		PollCreator newPoll = PollWizard.newPolls.get(player.getUniqueId());

		switch (e.getSlot()) {
		case PollCreatorFactory.NAME_SLOT:
			ChatUtil.notifyNew(player, "question");
			newPoll.setAwaitingName(true);
			player.closeInventory();
			break;
		case PollCreatorFactory.OPTIONS_SLOT:
			OptionCreatorFactory.openInventory(player);
			break;
		case PollCreatorFactory.SUBMIT_SLOT:
			if (PollWizard.activePolls.size() + PollWizard.closedPolls.size() >= PollCreator.MAX_POLLS)
				ChatUtil.error(player, "Maximum poll limit reached");
			else if (newPoll.getOptions().size() < 2)
				ChatUtil.error(player, "A poll must have at least two options");
			else {
				PollWizard.createPoll(newPoll, player);
				PollWizard.newPolls.remove(player.getUniqueId());
				PollManagerFactory.openInventory(player);
			}
			break;
		case PollCreatorFactory.CANCEL_SLOT:
			PollWizard.newPolls.remove(player.getUniqueId());
			PollManagerFactory.openInventory(player);
			break;
		}
	}

}
