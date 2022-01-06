package me.yourselvs.pollwizard.model;

public class ClosedPollAction extends PollAction {
	public ClosedPoll poll;

	public ClosedPollAction(ManagerAction action, ClosedPoll poll) {
		super(action);
		this.poll = poll;
	}

}
