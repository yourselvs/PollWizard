package me.yourselvs.pollwizard.factories;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.model.ClosedPollAction;
import me.yourselvs.pollwizard.model.PollOptionSummary;
import me.yourselvs.pollwizard.util.ItemStackUtil;

public class ClosedPollManagerFactory {
	public static final int
			UNSORTED_TOOLTIP_SLOT = 0,
			SORTED_TOOLTIP_SLOT = 18,
			QUESTION_SLOT = 37, 
			RETURN_SLOT = 40, 
			CONFIRM_SLOT = 41,
			CANCEL_SLOT = 42,
			DELETE_SLOT = 44;
	
	public static final String GUI_NAME = "Poll Results";

	public static void openInventory(HumanEntity player) {
		Inventory inv = createInventory(player);
		player.openInventory(inv);
	}

	private static Inventory createInventory(HumanEntity player) {
		ClosedPollAction action = PollWizard.closedPollManagerService.getPlayerActions().get(player.getUniqueId());
		Inventory inv = Bukkit.createInventory(null, 45, GUI_NAME);
		inv.setItem(UNSORTED_TOOLTIP_SLOT, createUnsortedTooltipItemStack());
		inv.setItem(SORTED_TOOLTIP_SLOT, createSortedTooltipItemStack());
		inv.setItem(QUESTION_SLOT, createQuestionItemStack(action.poll.getName()));
		
		List<PollOptionSummary> unsortedOptions = action.poll.getOptionSummaries();
		
		for (int i = 0; i < unsortedOptions.size(); i++) {
			inv.setItem(i + 9, createOptionItemStack(unsortedOptions.get(i)));
		}
		
		List<PollOptionSummary> sortedOptions = action.poll.getOptionSummaries();
		
		sortedOptions.sort(new Comparator<PollOptionSummary>() {
			@Override
			public int compare(PollOptionSummary p1, PollOptionSummary p2) {
				return p1.getPosition() - p2.getPosition();
			}
		});
		
		for (int i = 0; i < sortedOptions.size(); i++) {
			inv.setItem(i + 27, createOptionItemStack(sortedOptions.get(i)));
		}
		
		switch(action.action) {
		case VIEW:
			inv.setItem(RETURN_SLOT, createReturnItemStack());
			inv.setItem(DELETE_SLOT, createDeleteItemStack());
			break;
		case DELETE:
			inv.setItem(CONFIRM_SLOT, createConfirmItemStack());
			inv.setItem(CANCEL_SLOT, createCancelItemStack());
			break;
		default:
			break;
		}

		return inv;
	}

	private static ItemStack createReturnItemStack() {
		return new ItemStackUtil(Material.DIAMOND)
				.setName(ChatColor.AQUA + "Return")
				.buildItem();
	}
	
	private static ItemStack createQuestionItemStack(String question) {
		return new ItemStackUtil(Material.ENCHANTED_BOOK)
				.setName(ItemStackUtil.titleColor + "Poll: " + 
						ChatColor.RESET + question)
				.buildItem();
	}
	
	private static ItemStack createConfirmItemStack() {
		return new ItemStackUtil(Material.TNT)
				.setName(ItemStackUtil.resetColor + ChatColor.DARK_RED + "Confirm deletion")
				.setLoreMessage(ItemStackUtil.subtitleColor + "Are you sure? This poll")
				.addLoreMessage(ItemStackUtil.subtitleColor + "cannot be recovered.")
				.buildItem();
	}

	private static ItemStack createUnsortedTooltipItemStack() {
		return new ItemStackUtil(Material.TORCH)
				.setName(ItemStackUtil.resetColor + ChatColor.YELLOW + "Original order")
				.buildItem();
	}
	private static ItemStack createSortedTooltipItemStack() {
		return new ItemStackUtil(Material.SOUL_TORCH)
				.setName(ItemStackUtil.resetColor + ChatColor.AQUA + "Rank order")
				.setLoreMessage(ItemStackUtil.subtitleColor + "Ordered by number of votes")
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
	
	private static ItemStack createOptionItemStack(PollOptionSummary pollOption) {
		int position = pollOption.getPosition();
		NumberFormat formatter = NumberFormat.getPercentInstance();
		formatter.setMinimumFractionDigits(1);
		
		Material[] materials = new Material[] {
			Material.GOLD_BLOCK,
			Material.IRON_BLOCK,
			Material.COPPER_BLOCK,
			Material.DIRT,
			Material.DIRT,
			Material.DIRT,
			Material.DIRT,
			Material.DIRT,
			Material.DIRT
		};
		
		ChatColor[] colors = new ChatColor[] {
			ChatColor.YELLOW,
			ChatColor.WHITE,
			ChatColor.GOLD,
			ChatColor.GRAY,
			ChatColor.GRAY,
			ChatColor.GRAY,
			ChatColor.GRAY,
			ChatColor.GRAY,
			ChatColor.GRAY
		};
		
		return new ItemStackUtil(materials[position])
				.setName(ItemStackUtil.titleColor + pollOption.getValue())
				.setLoreMessage(colors[position] + "#" + (position + 1))
				.addLoreMessage(ItemStackUtil.subtitleColor + "Total votes: " + ChatColor.YELLOW + pollOption.getVotes().size())
				.addLoreMessage(ItemStackUtil.subtitleColor + "% of votes: " + ChatColor.YELLOW + formatter.format(pollOption.getPctOfVote()))
				.buildItem();
	}
}
