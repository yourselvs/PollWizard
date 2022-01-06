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
import me.yourselvs.pollwizard.model.PollOption;
import me.yourselvs.pollwizard.util.InventoryUtil;
import me.yourselvs.pollwizard.util.ItemStackUtil;

public class PollListFactory {
	public static final int TOOLTIP_EMPTY_SLOT = 4;
	
	public static final String GUI_NAME = "Polls";

	public static void openInventory(HumanEntity player) {
		Inventory inv = createInventory(player);
		player.openInventory(inv);
	}

	private static Inventory createInventory(HumanEntity player) {
		// TODO: maximum number of polls (54)
		// ... or pagination
		Inventory inv;
		List<ActivePoll> polls = PollWizard.activePolls;
		
		if(polls.size() > 0) {
			int inventorySize = InventoryUtil.roundUp(polls.size(), 9);
			inv = Bukkit.createInventory(null, inventorySize, GUI_NAME);
			
			for(int i = 0; i < polls.size(); i++) {
				inv.setItem(i, createPollItemStack(polls.get(i), player));
			}
		}
		else {
			inv = Bukkit.createInventory(null, 9, GUI_NAME);
			inv.setItem(TOOLTIP_EMPTY_SLOT, createTooltipEmptyItemStack());
		}

		return inv;
	}

	private static ItemStack createTooltipEmptyItemStack() {
		return new ItemStackUtil(Material.SOUL_TORCH)
				.setName(ItemStackUtil.resetColor + ChatColor.AQUA + "No active polls")
				.buildItem();
	}

	private static ItemStack createPollItemStack(ActivePoll poll, HumanEntity player) {
		PollOption playerVote = poll.getPlayerVote(player.getUniqueId());
		
		Material material = playerVote == null ?
				Material.BOOK :
				Material.ENCHANTED_BOOK;
		
		String loreMessage = playerVote == null ?
				ItemStackUtil.subtitleColor + "You haven't voted" :
				ItemStackUtil.subtitleColor + "You voted: " + 
				ChatColor.YELLOW + ChatColor.ITALIC + playerVote.getValue();
		
		return new ItemStackUtil(material)
				.setName(ItemStackUtil.titleColor + poll.getName())
				.setLoreMessage(ItemStackUtil.pluralize(poll.getOptions().size(), "option"))
				.addLoreMessage(loreMessage)
				.buildItem();
	}
}
