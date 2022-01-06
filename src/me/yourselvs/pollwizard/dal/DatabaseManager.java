package me.yourselvs.pollwizard.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.yourselvs.pollwizard.PollWizard;

public class DatabaseManager {
	private static final int dbVersion = 1;
	
	private PollWizard plugin;
	
	private Connection connection;

	public DatabaseManager(PollWizard plugin) {
		this.plugin = plugin;
		
		initializeDB();
	}

	public boolean isConnected() {
		return connection != null; 
	}
	
	public void connect() throws ClassNotFoundException, SQLException {
		if (!isConnected())
			connection = DriverManager.getConnection("jdbc:sqlite:plugins/PollWizard/" + plugin.getConfig().getString("dbFilename"));
	}
	
	public void disconnect() {
		try {
			if(isConnected() && !connection.isClosed())
				connection.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	private void initializeDB() {
		try {
			connect();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			plugin.getLogger().severe("PollWizard database could not connect to a database file.");
		}
		
		if (isConnected()) {
			plugin.getLogger().info("Database connected.");
			createTables();
			insertVersion();
		}
	}
	
	private void createTables() {
		try {
			createPollsTable();
			createOptionsTable();
			createVotesTable();
			createUpdatesTable();
			createIndexes();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createPollsTable() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS polls ("
				+ " poll_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " question VARCHAR(255) NOT NULL,"
				+ " created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
				+ " created_by VARCHAR(255) NOT NULL,"
				+ " closed_at TIMESTAMP NULL,"
				+ " closed_by VARCHAR(255) NULL,"
				+ " deleted_at TIMESTAMP NULL,"
				+ " deleted_by VARCHAR(255) NULL);");
		ps.executeUpdate();
	}
	
	private void createOptionsTable() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS options ("
				+ " option_id INTEGER PRIMARY KEY,"
				+ " poll_id INTEGER NOT NULL,"
				+ " option_text VARCHAR(255) NOT NULL,"
				+ " option_order int NOT NULL,"
				+ " FOREIGN KEY (poll_id) REFERENCES polls (poll_id));");
		ps.executeUpdate();
	}
	
	private void createVotesTable() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS votes ("
				+ " option_id INTEGER NOT NULL,"
				+ " player_id VARCHAR(255) NOT NULL,"
				+ " FOREIGN KEY (option_id) REFERENCES options (option_id));");
		ps.executeUpdate();
	}
	
	private void createUpdatesTable() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS updates ("
				+ " version_num INTEGER NOT NULL UNIQUE,"
				+ " updated_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);");
		ps.executeUpdate();
	}
	
	private void createIndexes() throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("CREATE INDEX IF NOT EXISTS polls_created_at ON polls (poll_id, question, created_at);");
		ps.executeUpdate();
		ps = connection.prepareStatement("CREATE INDEX IF NOT EXISTS polls_closed_at ON polls (poll_id, question, closed_at);");
		ps.executeUpdate();
		ps = connection.prepareStatement("CREATE INDEX IF NOT EXISTS options_pollid ON options (option_id, poll_id);");
		ps.executeUpdate();
	}
	
	private void insertVersion() {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT version_num FROM updates ORDER BY version_num DESC LIMIT 1");
			ResultSet results = ps.executeQuery();
			if(!results.next()) {
				ps = connection.prepareStatement("INSERT INTO updates (version_num) VALUES (?)");
				ps.setInt(1, dbVersion);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
