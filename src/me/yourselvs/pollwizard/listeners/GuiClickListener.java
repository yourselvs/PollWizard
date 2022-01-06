package me.yourselvs.pollwizard.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.factories.OptionCreatorFactory;
import me.yourselvs.pollwizard.factories.OptionEditorFactory;
import me.yourselvs.pollwizard.factories.ActiveOptionsFactory;
import me.yourselvs.pollwizard.factories.ActivePollManagerFactory;
import me.yourselvs.pollwizard.factories.ClosedPollManagerFactory;
import me.yourselvs.pollwizard.factories.PollCreatorFactory;
import me.yourselvs.pollwizard.factories.PollListFactory;
import me.yourselvs.pollwizard.factories.PollManagerFactory;
import me.yourselvs.pollwizard.factories.PollVoteFactory;

public class GuiClickListener implements Listener {
	
	@EventHandler
	public void onPlayerClickOnItem(InventoryClickEvent e) {		
		if (e.getRawSlot() != e.getSlot())
			return;
		
		boolean wasCancelled = e.isCancelled();
		
		e.setCancelled(true);
		
		switch(e.getWhoClicked().getOpenInventory().getTitle()) {
		case OptionCreatorFactory.GUI_NAME:
			PollWizard.optionCreatorService.handleClick(e);
			break;
		case OptionEditorFactory.GUI_NAME:
			PollWizard.optionEditorService.handleClick(e);
			break;
		case ActiveOptionsFactory.GUI_NAME:
			PollWizard.activeOptionsService.handleClick(e);
			break;
		case ActivePollManagerFactory.GUI_NAME:
			PollWizard.activePollManagerService.handleClick(e);
			break;
		case ClosedPollManagerFactory.GUI_NAME:
			PollWizard.closedPollManagerService.handleClick(e);
			break;
		case PollCreatorFactory.GUI_NAME:
			PollWizard.pollCreatorService.handleClick(e);
			break;
		case PollListFactory.GUI_NAME:
			PollWizard.pollListService.handleClick(e);
			break;
		case PollManagerFactory.GUI_NAME:
			PollWizard.pollManagerService.handleClick(e);
			break;
		case PollVoteFactory.GUI_NAME:
			PollWizard.pollVoteService.handleClick(e);
			break;
		default:
			e.setCancelled(wasCancelled);
		}
	}
}
