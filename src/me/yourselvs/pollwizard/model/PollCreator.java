package me.yourselvs.pollwizard.model;

import java.util.ArrayList;
import java.util.List;

public class PollCreator {
	public static final int MAX_OPTIONS = 9;
	public static final int MAX_POLLS = 5;
	
	private String name;
	private boolean awaitingName;
	private int awaitingOption, editingOption;
	private List<String> options;

	public PollCreator() {
		this.name = "What is the question?";
		this.awaitingName = false;
		this.awaitingOption = -1;
		this.options = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAwaitingName() {
		return awaitingName;
	}
	
	public void setAwaitingName(boolean awaitingName) {
		this.awaitingName = awaitingName;
		
		if (awaitingName)
			this.awaitingOption = -1;
	}

	public int getAwaitingOption() {
		return awaitingOption;
	}
	
	public void setAwaitingOption(int awaitingOption) {
		if (awaitingOption < 0 || 
				awaitingOption >= options.size() + 1 || 
				editingOption >= MAX_OPTIONS)
			this.awaitingOption = -1;
		else
			this.awaitingOption = awaitingOption;
		
		if (awaitingOption >= 0)
			this.awaitingName = false;
	}
	
	public int getEditingOption() {
		return editingOption;
	}
	
	public void setEditingOption(int editingOption) {
		if (editingOption < 0 || 
				editingOption >= options.size() || 
				editingOption >= MAX_OPTIONS)
			this.editingOption = -1;
		else
			this.editingOption = editingOption;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public void resetAwaiting() {
		this.awaitingName = false;
		this.awaitingOption = -1;
	}
}
