package me.yourselvs.pollwizard.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Poll {
	private int id;
	private String name;
	private List<PollOption> options;
	
	public Poll(String name, List<String> options) {
		this.name = name;
		setOptionsByString(options);
	}

	public String getName() {
		return name;
	}
	
	public Poll(int id, String name, List<PollOption> options) {
		this.id = id;
		this.name = name;
		this.options = options;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int pollId) {
		this.id = pollId;
	}
	
	public List<PollOption> getOptions() {
		return options;
	}
	
	public void setOptions(List<PollOption> options) {
		this.options = options;
	}
	
	public void setOptionsByString(List<String> options) {
		List<PollOption> newOptions = new ArrayList<PollOption>();
		
		for(int i = 0; i < options.size(); i++) {
			newOptions.add(new PollOption(options.get(i), i));
		}
		
		setOptions(newOptions);
	}

	public PollOption getPlayerVote(UUID playerId) {
		for(PollOption pollOption : options) {
			if (pollOption.getVotes().contains(playerId))
				return pollOption;
		}
		
		return null;
	}
	
	public int getTotalVotes() {
		int total = 0;
		
		for(PollOption pollOption : options) {
			total += pollOption.getVotes().size();
		}
		
		return total;
	}
}
