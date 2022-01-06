package me.yourselvs.pollwizard.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;

import me.yourselvs.pollwizard.PollWizard;
import me.yourselvs.pollwizard.model.ActivePoll;
import me.yourselvs.pollwizard.model.ClosedPoll;
import me.yourselvs.pollwizard.model.Poll;
import me.yourselvs.pollwizard.model.PollOption;

public class PollDAL {
	private Connection connection;
	private PollWizard plugin;

	public PollDAL(Connection connection, PollWizard plugin) {
		this.connection = connection;
		this.plugin = plugin;
	}

	public void createPoll(Poll poll, HumanEntity player) {
		int pollId = -1;
		
		String sql = " INSERT INTO polls (question, created_by)"
				   + " VALUES (?, ?)";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, poll.getName());
			stmt.setString(2, player.getName());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			pollId = rs.getInt(1);
			
			for(PollOption option : poll.getOptions())
				createOption(pollId, option);
		} catch (SQLException e) {
			plugin.getLogger().severe(sql);
			e.printStackTrace();
		}
		
		poll.setId(pollId);
	}
	
	private void createOption(int pollId, PollOption option) {
		int optionId = -1;
		
		String sql = " INSERT INTO options (poll_id, option_text, option_order)"
				   + " VALUES (?, ?, ?)";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, pollId);
			stmt.setString(2, option.getValue());
			stmt.setInt(3, option.getOrder());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			optionId = rs.getInt(1);
		} catch (SQLException e) {
			plugin.getLogger().severe(sql);
			e.printStackTrace();
		}

		option.setId(optionId);
	}  
	
	public void closePoll(ActivePoll poll, HumanEntity player) {
		String sql = " UPDATE polls"
				   + " SET closed_at = CURRENT_TIMESTAMP, closed_by = ?"
				   + " WHERE poll_id = ?";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, player.getName());
			stmt.setInt(2, poll.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			plugin.getLogger().severe(sql);
			e.printStackTrace();
		}
	}
	
	public void deletePoll(Poll poll, HumanEntity player) {
		String sql = "UPDATE polls "
				   + "SET deleted_at = CURRENT_TIMESTAMP, deleted_by = ?"
				   + "WHERE poll_id = ?";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, player.getName());
			stmt.setInt(2, poll.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			plugin.getLogger().severe(sql);
			e.printStackTrace();
		}
	}
	
	public void createVote(PollOption option, UUID playerId) {
		String sql = " INSERT INTO votes (option_id, player_id)"
				   + " VALUES (?, ?)";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, option.getId());
			stmt.setString(2, playerId.toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			plugin.getLogger().severe(sql);
			e.printStackTrace();
		}
	}
	
	public List<ActivePoll> getActivePolls() {
		List<ActivePoll> polls = new ArrayList<ActivePoll>();
		String sql = " SELECT poll_id, question"
				   + " FROM polls"
				   + " WHERE deleted_at IS NULL AND closed_at IS NULL"
				   + " ORDER BY created_at";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				int pollId = rs.getInt("poll_id");
				String question = rs.getString("question");
				List<PollOption> options = getPollOptions(pollId);
				
				ActivePoll poll = new ActivePoll(pollId, question, options);
				polls.add(poll);
				plugin.getLogger().info("Loaded poll \"" + question + "\". Found " + options.size() + " options and " + poll.getTotalVotes() + " total votes.");
			}
		} catch (SQLException e) {
			plugin.getLogger().severe(sql);
			e.printStackTrace();
		}
		return polls;
	}
	
	public List<ClosedPoll> getClosedPolls() {
		List<ClosedPoll> polls = new ArrayList<ClosedPoll>();
		String sql = " SELECT poll_id, question"
				   + " FROM polls"
				   + " WHERE deleted_at IS NULL AND closed_at IS NOT NULL"
				   + " ORDER BY closed_at";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				int pollId = rs.getInt("poll_id");
				String question = rs.getString("question");
				List<PollOption> options = getPollOptions(pollId);
				
				ClosedPoll poll = new ClosedPoll(pollId, question, options);
				polls.add(poll);
			}
		} catch (SQLException e) {
			plugin.getLogger().severe(sql);
			e.printStackTrace();
		}
		return polls;
	}
	
	private List<PollOption> getPollOptions(int pollId) {
		List<PollOption> options = new ArrayList<PollOption>();
		String sql = " SELECT option_id, option_text, option_order"
				   + " FROM options"
				   + " WHERE poll_id = ?"
				   + " ORDER BY option_order";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, pollId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				int optionId = rs.getInt("option_id");
				String option_text = rs.getString("option_text");
				int order = rs.getInt("option_order");
				Set<UUID> votes = getOptionVotes(optionId);
				
				PollOption option = new PollOption(optionId, option_text, order, votes);
				options.add(option);
			}
		} catch (SQLException e) {
			plugin.getLogger().severe(sql);
			e.printStackTrace();
		}
		return options;
	}
	
	private Set<UUID> getOptionVotes(int optionId) {
		Set<UUID> votes = new HashSet<UUID>();
		String sql = " SELECT player_id"
				   + " FROM votes"
				   + " WHERE option_id = ?";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, optionId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				UUID player_id = UUID.fromString(rs.getString("player_id"));
				votes.add(player_id);
			}
		} catch (SQLException e) {
			plugin.getLogger().severe(sql);
			e.printStackTrace();
		}
		return votes;
	}
}
