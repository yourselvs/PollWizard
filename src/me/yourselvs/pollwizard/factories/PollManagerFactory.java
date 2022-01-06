package me.yourselvs.pollwizard.factories;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.model.ActivePoll;
import me.yourselvs.pollwizard.model.ClosedPoll;
import me.yourselvs.pollwizard.model.PollCreator;
import me.yourselvs.pollwizard.util.ItemStackUtil;

public class PollManagerFactory {
	public static final int 
			TOOLTIP_ACTIVE_SLOT = 0,
			TOOLTIP_CLOSED_SLOT = 9,
			NEW_POLL_SLOT = 22,
			ERROR_SLOT = 26;

	public static final String GUI_NAME = "Manage Polls";
	
	public static void openInventory(HumanEntity player) {
		Inventory inv = createInventory(player);
		player.openInventory(inv);
	}
	
	private static Inventory createInventory(HumanEntity player) {
		// TODO: maximum number of polls (54)
		// ... or pagination
		Inventory inv;
		List<ActivePoll> activePolls = PollWizard.activePolls;
		List<ClosedPoll> closedPolls = PollWizard.closedPolls;
		
		inv = Bukkit.createInventory(null, 27, GUI_NAME);
		
		inv.setItem(TOOLTIP_ACTIVE_SLOT, createTooltipActiveItemStack(activePolls.size() == 0));
		inv.setItem(TOOLTIP_CLOSED_SLOT, createTooltipCompletedItemStack(closedPolls.size() == 0));
		inv.setItem(NEW_POLL_SLOT, createNewPollItemStack(activePolls.size() + closedPolls.size()));
		
		for(int i = 0; i < activePolls.size(); i++) {
			inv.setItem(i + 1, createActivePollItemStack(activePolls.get(i), player));
		}
		
		for(int i = 0; i < closedPolls.size(); i++) {
			inv.setItem(i + 10, createClosedPollItemStack(closedPolls.get(i), player));
		}
		
		if(!PollWizard.getDBManager().isConnected())
			inv.setItem(ERROR_SLOT, createErrorItemStack());
		
		return inv;
	}
	
	private static ItemStack createTooltipActiveItemStack(boolean empty) {
		ItemStackUtil itemStack = new ItemStackUtil(Material.TORCH)
				.setName(ItemStackUtil.resetColor + ChatColor.YELLOW + "Active polls");
		
		if(empty) 
			itemStack.setLoreMessage(ItemStackUtil.subtitleColor + "There are no active polls");
		
		return itemStack.buildItem();
	}
	
	private static ItemStack createTooltipCompletedItemStack(boolean empty) {
		ItemStackUtil itemStack = new ItemStackUtil(Material.SOUL_TORCH)
				.setName(ItemStackUtil.resetColor + ChatColor.AQUA + "Closed polls");
		
		if(empty) 
			itemStack.setLoreMessage(ItemStackUtil.subtitleColor + "There are no closed polls");
		
		return itemStack.buildItem();
	}
	
	private static ItemStack createNewPollItemStack(int pollsSize) {
		ItemStackUtil itemStack = new ItemStackUtil(Material.WRITABLE_BOOK)
				.setName(ChatColor.GREEN + "New Poll")
				.setLoreMessage(ItemStackUtil.subtitleColor + "Max. " + PollCreator.MAX_POLLS + " polls");
		 
		if(pollsSize >= PollCreator.MAX_POLLS) {
			itemStack.setLoreMessage("" + ChatColor.RED + ChatColor.ITALIC + "Max polls reached. You won't")
			 		 .addLoreMessage("" + ChatColor.RED + ChatColor.ITALIC + "be able to publish until a")
		 			 .addLoreMessage("" + ChatColor.RED + ChatColor.ITALIC + "poll gets deleted.");
		}
		 
		return itemStack.buildItem();
	}
	
	private static ItemStack createActivePollItemStack(ActivePoll poll, HumanEntity player) {
		return new ItemStackUtil(Material.BOOK)
				.setName(ItemStackUtil.titleColor + poll.getName())
				.setLoreMessage(ItemStackUtil.subtitleColor + ItemStackUtil.pluralize(poll.getOptions().size(), "option"))
				.addLoreMessage(ItemStackUtil.subtitleColor + ItemStackUtil.pluralize(poll.getTotalVotes(), "vote"))
				.buildItem();
	}
	
	private static ItemStack createClosedPollItemStack(ClosedPoll poll, HumanEntity player) {
		return new ItemStackUtil(Material.ENCHANTED_BOOK)
				.setName(ItemStackUtil.titleColor + poll.getName())
				.setLoreMessage(ItemStackUtil.subtitleColor + ItemStackUtil.pluralize(poll.getOptions().size(), "option"))
				.addLoreMessage(ItemStackUtil.subtitleColor + ItemStackUtil.pluralize(poll.getTotalVotes(), "vote"))
				.buildItem();
	}
	
	private static ItemStack createErrorItemStack() {
		return new ItemStackUtil(Material.END_CRYSTAL)
				.setName(ChatColor.DARK_RED + "WARNING: DB not connected")
				.setLoreMessage(ChatColor.RED + "Poll actions will not be saved on server restart.")
				.addLoreMessage(ChatColor.RED + "See console for details.")
				.buildItem();
	}
}
