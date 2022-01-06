package me.yourselvs.pollwizard.model;

public class PollOptionSummary extends PollOption {
	private int position;
	private float pctOfVote;

	public PollOptionSummary(PollOption pollOption, int position, float pctOfVote) {
		super(
				pollOption.getId(),
				pollOption.getValue(),
				pollOption.getOrder(),
				pollOption.getVotes()
		);
		
		this.setPosition(position);
		this.setPctOfVote(pctOfVote);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public float getPctOfVote() {
		return pctOfVote;
	}

	public void setPctOfVote(float pctOfVote) {
		this.pctOfVote = pctOfVote;
	}
}
