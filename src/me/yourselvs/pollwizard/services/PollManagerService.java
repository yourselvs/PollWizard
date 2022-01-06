package me.yourselvs.pollwizard.services;

import java.util.List;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.factories.ActivePollManagerFactory;
import me.yourselvs.pollwizard.factories.ClosedPollManagerFactory;
import me.yourselvs.pollwizard.factories.PollCreatorFactory;
import me.yourselvs.pollwizard.factories.PollManagerFactory;
import me.yourselvs.pollwizard.model.ActivePoll;
import me.yourselvs.pollwizard.model.ActivePollAction;
import me.yourselvs.pollwizard.model.ClosedPoll;
import me.yourselvs.pollwizard.model.ClosedPollAction;
import me.yourselvs.pollwizard.model.IClickHandler;
import me.yourselvs.pollwizard.model.ManagerAction;

public class PollManagerService implements IClickHandler {

	@Override
	public void handleClick(InventoryClickEvent e) {
		if (!e.isLeftClick() && !e.isRightClick())
			return;
		
		List<ActivePoll> activePolls = PollWizard.activePolls;
		List<ClosedPoll> closedPolls = PollWizard.closedPolls;
		int slot = e.getSlot();
		HumanEntity player = e.getWhoClicked();
		
		int activePollsEnd = PollManagerFactory.TOOLTIP_ACTIVE_SLOT + activePolls.size(); 
		int closedPollsEnd = PollManagerFactory.TOOLTIP_CLOSED_SLOT + closedPolls.size(); 
		
		if(slot == PollManagerFactory.NEW_POLL_SLOT) {
			PollCreatorFactory.openInventory(player);
		}
		else if(slot > PollManagerFactory.TOOLTIP_ACTIVE_SLOT && slot <= activePollsEnd) {
			ActivePoll poll = activePolls.get(slot- 1);
			ActivePollAction pollAction = new ActivePollAction(ManagerAction.VIEW, poll);
			PollWizard.activePollManagerService.getPlayerActions().put(player.getUniqueId(), pollAction);
			ActivePollManagerFactory.openInventory(player);
		}
		else if(slot > PollManagerFactory.TOOLTIP_CLOSED_SLOT && slot <= closedPollsEnd) {
			ClosedPoll poll = closedPolls.get(slot - PollManagerFactory.TOOLTIP_CLOSED_SLOT - 1);
			ClosedPollAction pollAction = new ClosedPollAction(ManagerAction.VIEW, poll);
			PollWizard.closedPollManagerService.getPlayerActions().put(player.getUniqueId(), pollAction);
			ClosedPollManagerFactory.openInventory(player);
		}
	}

}
