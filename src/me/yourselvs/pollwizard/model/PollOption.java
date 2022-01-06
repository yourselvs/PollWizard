package me.yourselvs.pollwizard.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PollOption {
	private int id;
	private String value;
	private int order;
	private Set<UUID> votes;
	
	public PollOption(String value, int order) {
		this.value = value;
		this.order = order;
		this.votes = new HashSet<UUID>();
	}
	
	public PollOption(int optionId, String value, int order, Set<UUID> votes) {
		this.id = optionId;
		this.value = value;
		this.order = order;
		this.votes = votes;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getOrder() {
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
	
	public Set<UUID> getVotes() {
		return votes;
	}
	
	public void setVotes(Set<UUID> votes) {
		this.votes = votes;
	}
}
