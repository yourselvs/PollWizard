package me.yourselvs.pollwizard.factories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.model.ActivePollAction;
import me.yourselvs.pollwizard.util.ItemStackUtil;

public class ActivePollManagerFactory {
	public static final int
			QUESTION_SLOT = 0,
			OPTIONS_SLOT = 2,
			SUBMIT_SLOT = 5, 
			CANCEL_SLOT = 6,
			DELETE_SLOT = 8;

	public static final String GUI_NAME = "Manage Poll";
	
	public static void openInventory(HumanEntity player) {
		ActivePollAction action = PollWizard.activePollManagerService.getPlayerActions().get(player.getUniqueId());
		Inventory inv = createInventory(action);
		player.openInventory(inv);
	}
	
	private static Inventory createInventory(ActivePollAction action) {
		Inventory inv = Bukkit.createInventory(null, 9, GUI_NAME);
		inv.setItem(QUESTION_SLOT, createQuestionItemStack(action.poll.getName()));
		inv.setItem(OPTIONS_SLOT, createOptionsItemStack(action.poll.getOptions().size()));
		
		switch(action.action) {
		case CLOSE:
			inv.setItem(SUBMIT_SLOT, createConfirmCloseItemStack());
			inv.setItem(CANCEL_SLOT, createCancelItemStack());	
			break;
		case DELETE:
			inv.setItem(SUBMIT_SLOT, createConfirmDeleteItemStack());
			inv.setItem(CANCEL_SLOT, createCancelItemStack());	
			break;
		default:
			inv.setItem(SUBMIT_SLOT, createSubmitItemStack(action.poll.getTotalVotes()));
			inv.setItem(CANCEL_SLOT, createCancelItemStack());	
			inv.setItem(DELETE_SLOT, createDeleteItemStack());
		}

		return inv;
	}


	private static ItemStack createQuestionItemStack(String name) {
		return new ItemStackUtil(Material.WRITABLE_BOOK)
				.setName(ItemStackUtil.titleColor + "Poll: " + 
						ItemStackUtil.resetColor + name)
				.buildItem();
	}

	private static ItemStack createOptionsItemStack(int count) {
		return new ItemStackUtil(Material.BOOK)
				.setName(ItemStackUtil.titleColor + "Options")
				.setLoreMessage(ItemStackUtil.pluralize(count, "option"))
				.addLoreMessage(ItemStackUtil.subtitleColor + "Click to view")
				.buildItem();
	}

	private static ItemStack createSubmitItemStack(int votes) {		
		return new ItemStackUtil(Material.EMERALD)
				.setName(ItemStackUtil.resetColor + ChatColor.GREEN + "Close poll")
				.setLoreMessage(ItemStackUtil.subtitleColor + ItemStackUtil.pluralize(votes, "vote"))
				.buildItem();
	}
	
	private static ItemStack createConfirmCloseItemStack() {
		return new ItemStackUtil(Material.DIAMOND)
				.setName(ItemStackUtil.resetColor + ChatColor.AQUA + "Confirm close")
				.setLoreMessage(ItemStackUtil.subtitleColor + "Are you sure? Voting can't")
				.addLoreMessage(ItemStackUtil.subtitleColor + "be re-opened for this poll.")
				.buildItem();
	}

	private static ItemStack createCancelItemStack() {
		return new ItemStackUtil(Material.REDSTONE)
				.setName(ItemStackUtil.resetColor + ChatColor.RED + "Cancel")
				.buildItem();
	}

	private static ItemStack createDeleteItemStack() {
		return new ItemStackUtil(Material.TNT)
				.setName(ItemStackUtil.resetColor + ChatColor.RED + "Delete")
				.buildItem();
	}
	
	private static ItemStack createConfirmDeleteItemStack() {
		return new ItemStackUtil(Material.TNT)
				.setName(ItemStackUtil.resetColor + ChatColor.DARK_RED + "Confirm deletion")
				.setLoreMessage(ItemStackUtil.subtitleColor + "Are you sure? This poll")
				.addLoreMessage(ItemStackUtil.subtitleColor + "cannot be recovered.")
				.buildItem();
	}

}
