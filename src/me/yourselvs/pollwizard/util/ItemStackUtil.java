package me.yourselvs.pollwizard.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
 
public class ItemStackUtil {
	public static final String resetColor = "" + ChatColor.RESET;
	public static final String subtitleColor = resetColor + ChatColor.GRAY + ChatColor.ITALIC;
	public static final String titleColor = resetColor + ChatColor.YELLOW;
 
    public ItemStack item;
    
    public ItemStackUtil(Material material) {
        item = new ItemStack(material);
    }
    
    public ItemStackUtil setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }
    
    public ItemStackUtil setName(String name) {
    	if (name == null)
    		return this;
    	
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);
        return this;
    }
    
    public ItemStackUtil setLoreMessage(String message) {
    	if (message == null)
    		return this;
    	
    	ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', message));
        meta.setLore(lore);
        item.setItemMeta(meta);
    	return this;
    }
    
    public ItemStackUtil addLoreMessage(String message) {
    	if (message == null)
    		return this;
    	
    	ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(ChatColor.translateAlternateColorCodes('&', message));
        meta.setLore(lore);
        item.setItemMeta(meta);
    	return this;
    }
    
    public ItemStack buildItem(){
        return item;
    }
    
    public static String pluralize(int count, String name) {
    	String result = count + " " + name;
    	
    	if (count != 1)
    		result += "s";
    	
    	return result;
    }
}