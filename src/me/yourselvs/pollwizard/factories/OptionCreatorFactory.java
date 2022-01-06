package me.yourselvs.pollwizard.factories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.model.PollCreator;
import me.yourselvs.pollwizard.util.ItemStackUtil;

public class OptionCreatorFactory {
	public static final int NEW_OPTION_SLOT = 11, 
			RETURN_SLOT = 13, 
			CLEAR_SLOT = 15;

	public static final String GUI_NAME = "Manage Poll Options";

	public static void openInventory(HumanEntity player) {
		PollCreator poll = PollWizard.newPolls.get(player.getUniqueId());
		poll.resetAwaiting();
		Inventory inv = createInventory(poll);
		player.openInventory(inv);
	}

	private static Inventory createInventory(PollCreator poll) {
		Inventory inv = Bukkit.createInventory(null, 18, GUI_NAME);
		inv.setItem(NEW_OPTION_SLOT, createNewOptionItemStack(poll.getOptions().size()));
		inv.setItem(RETURN_SLOT, createReturnItemStack());
		inv.setItem(CLEAR_SLOT, createClearItemStack());
		
		for (int i = 0; i < poll.getOptions().size() && i < PollCreator.MAX_OPTIONS; i++) {
			inv.setItem(i, createOptionItemStack(poll.getOptions().get(i)));
		}

		return inv;
	}
	
	private static ItemStack createNewOptionItemStack(int optionsSize) {
		String nameColor = ItemStackUtil.resetColor;
		String loreMessage = ItemStackUtil.subtitleColor + "Max. " + PollCreator.MAX_OPTIONS + " options";
		 
		if(optionsSize >= PollCreator.MAX_OPTIONS) {
			loreMessage = "" + ChatColor.RED + ChatColor.ITALIC + "Max options reached";
		}
		 
		return new ItemStackUtil(Material.FEATHER)
				.setName(nameColor + "New Option")
				.setLoreMessage(loreMessage)
				.buildItem();
	}

	private static ItemStack createReturnItemStack() {
		return new ItemStackUtil(Material.DIAMOND)
				.setName(ChatColor.AQUA + "Return")
				.buildItem();
	}

	private static ItemStack createClearItemStack() {
		return new ItemStackUtil(Material.TNT)
				.setName(ItemStackUtil.resetColor + ChatColor.RED + "Clear Options")
				.buildItem();
	}
	
	private static ItemStack createOptionItemStack(String string) {
		return new ItemStackUtil(Material.PAPER)
				.setName(ItemStackUtil.titleColor + "Option: " + 
						ItemStackUtil.resetColor + string)
				.setLoreMessage(ItemStackUtil.subtitleColor + "Click to edit")
				.buildItem();
	}
}
