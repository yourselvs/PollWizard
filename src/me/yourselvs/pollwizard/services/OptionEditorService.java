package me.yourselvs.pollwizard.services;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.factories.OptionCreatorFactory;
import me.yourselvs.pollwizard.factories.OptionEditorFactory;
import me.yourselvs.pollwizard.model.IClickHandler;
import me.yourselvs.pollwizard.model.PollCreator;
import me.yourselvs.pollwizard.util.ChatUtil;

public class OptionEditorService implements IClickHandler {

	@Override
	public void handleClick(InventoryClickEvent e) {
		if (!e.isLeftClick() && !e.isRightClick())
			return;
		
		HumanEntity player = e.getWhoClicked();
		PollCreator poll = PollWizard.newPolls.get(player.getUniqueId());
		
		switch (e.getSlot()) {
		case OptionEditorFactory.RENAME_SLOT:
			ChatUtil.notifyNew(player, "option");
			player.closeInventory();
			poll.setAwaitingOption(poll.getEditingOption());
			break;
		case OptionEditorFactory.RETURN_SLOT:
			OptionCreatorFactory.openInventory(player);
			break;
		case OptionEditorFactory.REMOVE_SLOT:
			poll.getOptions().remove(poll.getEditingOption());
			OptionCreatorFactory.openInventory(player);
			break;
		}
	}

}
