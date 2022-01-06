package me.yourselvs.pollwizard.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;

public class ChatUtil {
	public static String chatColor = "" + ChatColor.YELLOW + ChatColor.ITALIC;
	
	private static String prefix = 
			ChatColor.BLUE + "[" +
			ChatColor.AQUA + "PollWizard" + 
			ChatColor.BLUE + "] ";
	
	public static void broadcast(String message) {
		Bukkit.broadcastMessage(prefix + message);
	}
	
	public static void broadcast(String message, String permission) {
		Bukkit.broadcast(prefix + message, permission);
	}
	
	public static void message(HumanEntity player, String message) {
		player.sendMessage(prefix + message);
	}

	public static void notifyNew(HumanEntity player, String subject) {
		message(player, chatColor + "Type your new " + subject + " in chat");
	}
	
	public static void notifyReceived(HumanEntity player, String subject, String value) {
		message(player, chatColor + subject + " received: " + ChatColor.RESET + value);
	}
	
	public static void error(HumanEntity player, String message) {
		message(player, "" + ChatColor.RED + ChatColor.ITALIC + message);
	}
}
