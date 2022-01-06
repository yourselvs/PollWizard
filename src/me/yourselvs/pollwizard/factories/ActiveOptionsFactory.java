package me.yourselvs.pollwizard.factories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.yourselvs.pollwizard.model.ActivePoll;
import me.yourselvs.pollwizard.model.PollCreator;
import me.yourselvs.pollwizard.util.ItemStackUtil;

public class ActiveOptionsFactory {
	public static final int 
			TOOLTIP_SLOT = 12, 
			RETURN_SLOT = 14;

	public static final String GUI_NAME = "View Poll Options";

	public static void openInventory(HumanEntity player, ActivePoll poll) {
		Inventory inv = createInventory(poll);
		player.openInventory(inv);
	}

	private static Inventory createInventory(ActivePoll poll) {
		Inventory inv = Bukkit.createInventory(null, 18, GUI_NAME);
		inv.setItem(TOOLTIP_SLOT, createTooltipItemStack());
		inv.setItem(RETURN_SLOT, createReturnItemStack());
		
		for (int i = 0; i < poll.getOptions().size() && i < PollCreator.MAX_OPTIONS; i++) {
			inv.setItem(i, createOptionItemStack(poll.getOptions().get(i).getValue()));
		}

		return inv;
	}

	private static ItemStack createReturnItemStack() {
		return new ItemStackUtil(Material.DIAMOND)
				.setName(ChatColor.AQUA + "Return")
				.buildItem();
	}

	private static ItemStack createTooltipItemStack() {
		return new ItemStackUtil(Material.SOUL_TORCH)
				.setName(ItemStackUtil.resetColor + ChatColor.AQUA + "Votes are hidden")
				.setLoreMessage(ItemStackUtil.subtitleColor + "You won't be able to see vote")
				.addLoreMessage(ItemStackUtil.subtitleColor + "numbers until poll is closed")
				.buildItem();
	}
	
	private static ItemStack createOptionItemStack(String string) {
		return new ItemStackUtil(Material.PAPER)
				.setName(ItemStackUtil.titleColor + string)
				.buildItem();
	}
}
