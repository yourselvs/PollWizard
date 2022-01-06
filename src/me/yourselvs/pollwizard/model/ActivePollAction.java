package me.yourselvs.pollwizard.model;

public class ActivePollAction extends PollAction {
	public ActivePoll poll;

	public ActivePollAction(ManagerAction action, ActivePoll poll) {
		super(action);
		this.poll = poll;
	}

}
