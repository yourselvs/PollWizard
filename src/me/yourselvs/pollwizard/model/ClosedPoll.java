package me.yourselvs.pollwizard.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClosedPoll extends Poll {
	private List<PollOptionSummary> optionSummaries;

	public ClosedPoll(ActivePoll activePoll) {
		super(
			activePoll.getId(), 
			activePoll.getName(), 
			activePoll.getOptions()
		);

		this.optionSummaries = computeSummaries(activePoll.getOptions());
	}
	
	public ClosedPoll(int id, String name, List<PollOption> options) {
		super(id, name, options);
		
		this.optionSummaries = computeSummaries(options);
	}

	public List<PollOptionSummary> getOptionSummaries() {
		return optionSummaries;
	}

	public void setOptionSummaries(List<PollOptionSummary> optionSummaries) {
		this.optionSummaries = optionSummaries;
	}
	
	private List<PollOptionSummary> computeSummaries(List<PollOption> options) {
		int totalVotes = getTotalVotes();
		List<PollOptionSummary> optionSummaries = new ArrayList<PollOptionSummary>();
		List<PollOption> sortedOptions = new ArrayList<PollOption>(options);
		
		sortedOptions.sort(new Comparator<PollOption>() {
			@Override
			public int compare(PollOption p1, PollOption p2) {
				return p2.getVotes().size() - p1.getVotes().size();
			}
		});
		
		for(int i = 0; i < sortedOptions.size(); i++) {
			PollOption option = options.get(i);
			int index = indexOfOption(option.getId(), sortedOptions);
			float pctOfVote = totalVotes == 0 ? 0 : option.getVotes().size() / totalVotes;
			PollOptionSummary optionSummary = new PollOptionSummary(option, index, pctOfVote);
			optionSummaries.add(optionSummary);
		}
		
		return optionSummaries;
	}
	
	private int indexOfOption(int j, List<PollOption> options) {
		for(int i = 0; i < options.size(); i++) {
			if (j == options.get(i).getId())
				return i;
		}
		
		return -1;
	}
}
