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

public class OptionEditorFactory {
	public static final int
			RENAME_SLOT = 2, 
			RETURN_SLOT = 4, 
			REMOVE_SLOT = 6;

	public static final String GUI_NAME = "Edit Poll Option";

	public static void openInventory(HumanEntity player, int index) {
		PollCreator poll = PollWizard.newPolls.get(player.getUniqueId());
		poll.resetAwaiting();
		Inventory inv = createInventory(poll, index);
		player.openInventory(inv);
	}

	private static Inventory createInventory(PollCreator poll, int index) {
		Inventory inv = Bukkit.createInventory(null, 9, GUI_NAME);
		inv.setItem(RENAME_SLOT, createRenameItemStack(poll.getOptions().get(index)));
		inv.setItem(RETURN_SLOT, createReturnItemStack());
		inv.setItem(REMOVE_SLOT, createRemoveItemStack());

		return inv;
	}
	
	private static ItemStack createRenameItemStack(String optionText) {
		return new ItemStackUtil(Material.WRITABLE_BOOK)
				.setName(ItemStackUtil.resetColor + optionText)
				.setLoreMessage(ItemStackUtil.subtitleColor + "Click to edit, then type")
				.addLoreMessage(ItemStackUtil.subtitleColor + "your new option in chat")
				.buildItem();
	}

	private static ItemStack createReturnItemStack() {
		return new ItemStackUtil(Material.DIAMOND)
				.setName(ChatColor.AQUA + "Return")
				.buildItem();
	}

	private static ItemStack createRemoveItemStack() {
		return new ItemStackUtil(Material.TNT)
				.setName(ItemStackUtil.resetColor + ChatColor.RED + "Remove")
				.buildItem();
	}
}
