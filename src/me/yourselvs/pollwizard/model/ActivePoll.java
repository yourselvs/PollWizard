package me.yourselvs.pollwizard.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.yourselvs.pollwizard.PollWizard;

public class ActivePoll extends Poll {
	private Map<UUID, PollOption> pendingVotes;
	
	public ActivePoll(String name, List<String> options) {
		super(name, options);
		pendingVotes = new HashMap<UUID, PollOption>();
	}
	
	public ActivePoll(int id, String name, List<PollOption> options) {
		super(id, name, options);
		this.pendingVotes = new HashMap<UUID, PollOption>();
	}
	
	public PollOption getPlayerPendingVote(UUID playerId) {
		return pendingVotes.get(playerId);
	}
	
	public void setPlayerPendingVote(UUID playerId, int index) {
		pendingVotes.put(playerId, getOptions().get(index));
	}
	
	public void resetPlayerPendingVote(UUID playerId) {
		pendingVotes.remove(playerId);
	}
	
	public void confirmPlayerPendingVote(UUID playerId) {
		PollOption pollOption = pendingVotes.remove(playerId);
		pollOption.getVotes().add(playerId);
		PollWizard.submitVote(pollOption, playerId);
	}
}
