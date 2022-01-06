package me.yourselvs.pollwizard.util;

public class InventoryUtil {
	public static int roundUp(int source, int multiple) {
		int divided = source / multiple;
		int result = multiple * (source % multiple == 0 ? divided : divided + 1);
		return result == 0 ? multiple : result; 
	}
}
