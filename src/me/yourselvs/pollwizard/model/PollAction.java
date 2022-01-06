package me.yourselvs.pollwizard.model;

public abstract class PollAction {
	public ManagerAction action;
	
	public PollAction(ManagerAction action) {
		this.action = action;
	}
}