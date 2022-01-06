package me.yourselvs.pollwizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

import me.yourselvs.pollwizard.commands.CommandPoll;
import me.yourselvs.pollwizard.commands.CommandPolls;
import me.yourselvs.pollwizard.dal.DatabaseManager;
import me.yourselvs.pollwizard.dal.PollDAL;
import me.yourselvs.pollwizard.listeners.ChatListener;
import me.yourselvs.pollwizard.listeners.JoinListener;
import me.yourselvs.pollwizard.model.ActivePoll;
import me.yourselvs.pollwizard.model.ClosedPoll;
import me.yourselvs.pollwizard.model.PollCreator;
import me.yourselvs.pollwizard.model.PollOption;
import me.yourselvs.pollwizard.services.OptionCreatorService;
import me.yourselvs.pollwizard.services.OptionEditorService;
import me.yourselvs.pollwizard.services.ActiveOptionsService;
import me.yourselvs.pollwizard.services.ActivePollManagerService;
import me.yourselvs.pollwizard.services.ClosedPollManagerService;
import me.yourselvs.pollwizard.services.PollCreatorService;
import me.yourselvs.pollwizard.services.PollManagerService;
import me.yourselvs.pollwizard.services.PollListService;
import me.yourselvs.pollwizard.services.PollVoteService;
import me.yourselvs.pollwizard.util.ChatUtil;
import me.yourselvs.pollwizard.listeners.GuiClickListener;

public class PollWizard extends JavaPlugin {
	private static String PERMISSION_POLL, PERMISSION_POLLS;
	private static DatabaseManager dbManager;
	private static PollDAL pollDAL;

	public static List<ActivePoll> activePolls = new ArrayList<ActivePoll>();
	public static List<ClosedPoll> closedPolls = new ArrayList<ClosedPoll>();
	public static Map<UUID, PollCreator> newPolls = new HashMap<UUID, PollCreator>();

	public static ActiveOptionsService activeOptionsService;
	public static ActivePollManagerService activePollManagerService;
	public static ClosedPollManagerService closedPollManagerService; 
	public static OptionCreatorService optionCreatorService;
	public static OptionEditorService optionEditorService;
	public static PollCreatorService pollCreatorService; 
	public static PollManagerService pollManagerService;
	public static PollListService pollListService;
	public static PollVoteService pollVoteService;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		
		initializeDatabase();
		initializeServices();
		
		if (dbManager.isConnected())
			loadData();
		
		registerCommands();
		registerListeners();
	}

	@Override
	public void onDisable() {
		dbManager.disconnect();
	}

	private void initializeDatabase() {
		dbManager = new DatabaseManager(this);
		pollDAL = new PollDAL(dbManager.getConnection(), this);

		if(!dbManager.isConnected())
			this.getLogger().severe("PollWizard data cannot be retrieved. Starting from a blank state. Data will not be saved.");
	}
	
	private void initializeServices() {
		activeOptionsService = new ActiveOptionsService();
		activePollManagerService = new ActivePollManagerService();
		closedPollManagerService = new ClosedPollManagerService();
		optionCreatorService = new OptionCreatorService();
		optionEditorService = new OptionEditorService();
		pollCreatorService = new PollCreatorService();
		pollManagerService = new PollManagerService();
		pollListService = new PollListService();
		pollVoteService = new PollVoteService();
	}
	
	private void loadData() {
		activePolls = pollDAL.getActivePolls();
		closedPolls = pollDAL.getClosedPolls();
	}

	private void registerCommands() {
		PluginCommand pollCmd = this.getCommand("poll");
		PluginCommand pollsCmd = this.getCommand("polls");
		
		pollCmd.setExecutor(new CommandPoll());
		pollsCmd.setExecutor(new CommandPolls());
		
		PERMISSION_POLL = pollCmd.getPermission();
		PERMISSION_POLLS = pollCmd.getPermission();
	}
	
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		getServer().getPluginManager().registerEvents(new GuiClickListener(), this);
	}
	
	public static void resetState(HumanEntity player) {
		activePollManagerService.getPlayerActions().remove(player.getUniqueId());
		closedPollManagerService.getPlayerActions().remove(player.getUniqueId());
		pollVoteService.getPlayerVoting().remove(player.getUniqueId());
		
		PollCreator poll = newPolls.get(player.getUniqueId());
		if (poll != null) 
			poll.resetAwaiting();
	}
	
	public static DatabaseManager getDBManager() {
		return dbManager;
	}
	
	public static String getPermissionPoll() {
		return PERMISSION_POLL;
	}
	
	public static String getPermissionPolls() {
		return PERMISSION_POLLS;
	}

	public static void createPoll(PollCreator newPoll, HumanEntity player) {
		ActivePoll poll = new ActivePoll(newPoll.getName(), newPoll.getOptions());
		pollDAL.createPoll(poll, player);

		ChatUtil.broadcast(ChatUtil.chatColor + "New poll: " 
				+ ChatColor.RESET + newPoll.getName() 
				+ ChatUtil.chatColor + " Use "
				+ ChatColor.LIGHT_PURPLE + "/polls"
				+ ChatUtil.chatColor + " to vote.");
		
		activePolls.add(poll);
	}
	
	public static void closePoll(ActivePoll activePoll, HumanEntity player) {
		ClosedPoll closedPoll = new ClosedPoll(activePoll);
		pollDAL.closePoll(activePoll, player);
		PollWizard.activePolls.remove(activePoll);
		PollWizard.closedPolls.add(closedPoll);
	}

	public static void deleteActivePoll(ActivePoll poll, HumanEntity player) {
		pollDAL.deletePoll(poll, player);
		PollWizard.activePolls.remove(poll);
	}
	
	public static void deleteClosedPoll(ClosedPoll poll, HumanEntity player) {
		pollDAL.deletePoll(poll, player);
		PollWizard.closedPolls.remove(poll);
	}
	
	public static void submitVote(PollOption option, UUID playerId) {
		pollDAL.createVote(option, playerId);
	}
}
