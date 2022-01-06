package me.yourselvs.pollwizard.factories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.yourselvs.pollwizard.model.ActivePoll;
import me.yourselvs.pollwizard.model.PollOption;
import me.yourselvs.pollwizard.util.ItemStackUtil;

public class PollVoteFactory {
	public static final int 
			QUESTION_SLOT = 11, 
			SUBMIT_SLOT = 14, 
			CANCEL_SLOT = 15;
	
	public static final String GUI_NAME = "Poll Voting";

	public static void openInventory(HumanEntity player, ActivePoll poll) {
		Inventory inv = createInventory(player, poll);
		player.openInventory(inv);
	}
	
	private static Inventory createInventory(HumanEntity player, ActivePoll poll) {
		Inventory inv = Bukkit.createInventory(null, 18, GUI_NAME);
		PollOption pendingVoteId = poll.getPlayerPendingVote(player.getUniqueId());
		PollOption playerVote = pendingVoteId != null ? pendingVoteId : poll.getPlayerVote(player.getUniqueId());
		
		// the vote id is null if the player vote is null
		int voteId = playerVote == null ? -1 : playerVote.getId();
		
		// the vote is confirmed if there is a vote but no vote pending
		boolean confirmedVote = pendingVoteId == null && playerVote != null;
		
		inv.setItem(QUESTION_SLOT, createNewOptionItemStack(poll));
		
		if(confirmedVote)
			inv.setItem(CANCEL_SLOT, createReturnItemStack());
		else {
			inv.setItem(SUBMIT_SLOT, createSubmitItemStack());
			inv.setItem(CANCEL_SLOT, createCancelItemStack());
		}
		
		for (int i = 0; i < poll.getOptions().size(); i++) {
			inv.setItem(i, createOptionItemStack(poll.getOptions().get(i), voteId, confirmedVote));
		}

		return inv;
	}
	
	private static ItemStack createNewOptionItemStack(ActivePoll poll) {
		return new ItemStackUtil(Material.WRITABLE_BOOK)
				.setName(ItemStackUtil.resetColor + "Poll: " + 
						ItemStackUtil.titleColor + poll.getName())
				.setLoreMessage(ItemStackUtil.pluralize(poll.getTotalVotes(), "vote"))
				.buildItem();
	}
	
	private static ItemStack createSubmitItemStack() {
		return new ItemStackUtil(Material.EMERALD)
				.setName(ChatColor.GREEN + "Submit")
				.buildItem();
	}

	private static ItemStack createReturnItemStack() {
		return new ItemStackUtil(Material.DIAMOND)
				.setName(ChatColor.AQUA + "Return")
				.buildItem();
	}

	private static ItemStack createCancelItemStack() {
		return new ItemStackUtil(Material.REDSTONE)
				.setName(ItemStackUtil.resetColor + ChatColor.RED + "Cancel")
				.buildItem();
	}
	
	private static ItemStack createOptionItemStack(PollOption pollOption, int voteId, boolean confirmedVote) {
		Material optionMaterial = Material.PAPER;
		String subtitle = ItemStackUtil.subtitleColor + "Click to choose";
		
		if(voteId >= 0) {
			if (voteId == pollOption.getId()) {
				optionMaterial = confirmedVote ?
						Material.LIME_STAINED_GLASS_PANE :
						Material.YELLOW_STAINED_GLASS_PANE;
				
				subtitle = confirmedVote ?
						"" + ChatColor.GREEN + ChatColor.ITALIC + "Vote confirmed" :
						"" + ChatColor.AQUA + ChatColor.ITALIC + "Click "
						   + ChatColor.GREEN + ChatColor.ITALIC + "Submit"	 
						   + ChatColor.AQUA + ChatColor.ITALIC + " to confirm";
			}
			else if (confirmedVote){
				// if the vote is confirmed on another vote, add no subtitle
				subtitle = null;
			}
		}
		
		ItemStackUtil itemStack = new ItemStackUtil(optionMaterial)
				.setName(ItemStackUtil.titleColor + pollOption.getValue())
				.setLoreMessage(subtitle);
			
		return itemStack.buildItem();
	}
}
