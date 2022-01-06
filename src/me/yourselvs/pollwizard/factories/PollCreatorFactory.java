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

public class PollCreatorFactory {
	public static final int 
			NAME_SLOT = 0, 
			OPTIONS_SLOT = 2, 
			SUBMIT_SLOT = 5, 
			CANCEL_SLOT = 6, 
			TOOLTIP_SLOT = 8;

	public static final String GUI_NAME = "Create Poll";

	public static void openInventory(HumanEntity player) {
		if (!PollWizard.newPolls.containsKey(player.getUniqueId())) {
			PollWizard.newPolls.put(player.getUniqueId(), new PollCreator());
		}
		PollCreator poll = PollWizard.newPolls.get(player.getUniqueId());
		poll.resetAwaiting();
		Inventory inv = createInventory(poll);
		player.openInventory(inv);
	}

	private static Inventory createInventory(PollCreator poll) {
		Inventory inv = Bukkit.createInventory(null, 9, GUI_NAME);
		inv.setItem(NAME_SLOT, createNameItemStack(poll.getName()));
		inv.setItem(OPTIONS_SLOT, createOptionsItemStack(poll.getOptions().size()));
		inv.setItem(SUBMIT_SLOT, createSubmitItemStack(PollWizard.activePolls.size()));
		inv.setItem(CANCEL_SLOT, createCancelItemStack());
		inv.setItem(TOOLTIP_SLOT, createTooltipItemStack());

		return inv;
	}


	private static ItemStack createNameItemStack(String name) {
		return new ItemStackUtil(Material.WRITABLE_BOOK)
				.setName(ItemStackUtil.titleColor + "Poll: " + 
						ItemStackUtil.resetColor + name)
				.setLoreMessage(ItemStackUtil.subtitleColor + "Click to edit, then type")
				.addLoreMessage(ItemStackUtil.subtitleColor + "your new question in chat")
				.buildItem();
	}

	private static ItemStack createOptionsItemStack(int count) {
		return new ItemStackUtil(Material.BOOK)
				.setName(ItemStackUtil.titleColor + "Options")
				.setLoreMessage(ItemStackUtil.pluralize(count, "option"))
				.addLoreMessage(ItemStackUtil.subtitleColor + "Click to edit")
				.buildItem();
	}

	private static ItemStack createSubmitItemStack(int pollsSize) {
		String loreMessage = ItemStackUtil.subtitleColor + "Open this poll to the server";
		
		if(pollsSize >= PollCreator.MAX_POLLS) {
			loreMessage = "" + ChatColor.RED + ChatColor.ITALIC + "Max polls reached";
		}
		
		return new ItemStackUtil(Material.EMERALD)
				.setName(ItemStackUtil.resetColor + ChatColor.GREEN + "Publish")
				.setLoreMessage(loreMessage)
				.buildItem();
	}

	private static ItemStack createCancelItemStack() {
		return new ItemStackUtil(Material.REDSTONE)
				.setName(ItemStackUtil.resetColor + ChatColor.RED + "Cancel")
				.setLoreMessage(ItemStackUtil.subtitleColor + "Delete this poll")
				.buildItem();
	}

	private static ItemStack createTooltipItemStack() {
		return new ItemStackUtil(Material.SOUL_TORCH)
				.setName(ItemStackUtil.resetColor + ChatColor.AQUA + "Autosave enabled")
				.setLoreMessage(ItemStackUtil.subtitleColor + "This menu is automatically saved.")
				.addLoreMessage(ItemStackUtil.subtitleColor + "You can safely close this screen.")
				.addLoreMessage(ItemStackUtil.subtitleColor + "Data is only reset on \"Cancel\".").buildItem();
	}

}
