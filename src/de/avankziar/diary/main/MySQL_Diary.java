package de.avankziar.diary.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQL_Diary 
{
	public static boolean global_ID_exists(Integer global_id)
	{
		try {
			PreparedStatement statement = Main.plugin.getConnection()
					.prepareStatement("SELECT GLOBAL_ID FROM "+Main.plugin.table
							+" WHERE GLOBAL_ID=?");
			statement.setInt(1, global_id);
			ResultSet results = statement.executeQuery();
			if(results.next())
			{
				return true;
			} else
			{
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean personal_ID_exists(Integer personal_id, UUID uuid)
	{
		try {
			PreparedStatement statement = Main.plugin.getConnection()
					.prepareStatement("SELECT PERSONAL_ID FROM "+Main.plugin.table
							+" WHERE PERSONAL_ID=? AND UUID=?");
			statement.setInt(1, personal_id);
			statement.setString(2, uuid.toString());
			ResultSet results = statement.executeQuery();
			if(results.next())
			{
				return true;
			} else
			{
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean UUID_list_exists(UUID uuid)
	{
		try {
			PreparedStatement statement = Main.plugin.getConnection()
					.prepareStatement("SELECT * FROM "+Main.plugin.table2
							+" WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if(results.next())
			{
				return true;
			} else
			{
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean insert_diary(UUID uuid, Integer personal_id, String quest_relation_raw, String quest_relation, String eintrag, String datum)
	{
		try
		{
			PreparedStatement insert = Main.plugin.getConnection()
					.prepareStatement("INSERT INTO "+Main.plugin.table
							+ " (UUID, PERSONAL_ID, QUEST_RELATION_RAW, QUEST_RELATION, EINTRAG, DATUM)"
							+ " VALUE (?,?,?,?,?,?)");
			insert.setString(1, uuid.toString());
			insert.setInt(2, personal_id);
			insert.setString(3, quest_relation_raw);
			insert.setString(4, quest_relation);
			insert.setString(5, eintrag);
			insert.setString(6, datum);
			insert.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void insert_list(UUID uuid, Integer list_id)
	{
		try
		{
			PreparedStatement insert = Main.plugin.getConnection()
					.prepareStatement("INSERT INTO "+Main.plugin.table2
							+ " (UUID, LIST_ID)"
							+ " VALUE (?,?)");
			insert.setString(1, uuid.toString());
			insert.setInt(2, list_id);
			insert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static void update_list(UUID uuid, Integer list_id)
	{
		try
		{
			PreparedStatement statement = Main.plugin.getConnection()
					.prepareStatement("UPDATE " + Main.plugin.table2 
							+ " SET LIST_ID=? WHERE UUID=?");
			statement.setInt(1, list_id);
			statement.setString(2, uuid.toString());
			statement.executeUpdate();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static Integer getList_id(UUID uuid)
	{
		try {
			PreparedStatement statement = Main.plugin.getConnection()
					.prepareStatement("SELECT * FROM "+Main.plugin.table2
							+" WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if(results.next())
			{
				return results.getInt("LIST_ID");
			} else
			{
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String getEntryPerQuest(UUID uuid, Integer id, String quest)
	{
		try {
			PreparedStatement statement = Main.plugin.getConnection()
					.prepareStatement("SELECT * FROM "+Main.plugin.table
							+" WHERE UUID=? AND PERSONAL_ID=? AND QUEST_RELATION_RAW=?");
			statement.setString(1, uuid.toString());
			statement.setInt(2, id);
			statement.setString(3, quest);
			ResultSet results = statement.executeQuery();
			if(results.next())
			{
				return results.getString("EINTRAG");
			} else
			{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getQuestRelation(UUID uuid, Integer id)
	{
		try {
			PreparedStatement statement = Main.plugin.getConnection()
					.prepareStatement("SELECT * FROM "+Main.plugin.table
							+" WHERE UUID=? AND PERSONAL_ID=?");
			statement.setString(1, uuid.toString());
			statement.setInt(2, id);
			ResultSet results = statement.executeQuery();
			if(results.next())
			{
				return results.getString("QUEST_RELATION");
			} else
			{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getEntry(UUID uuid, Integer id)
	{
		try {
			PreparedStatement statement = Main.plugin.getConnection()
					.prepareStatement("SELECT * FROM "+Main.plugin.table
							+" WHERE UUID=? AND PERSONAL_ID=?");
			statement.setString(1, uuid.toString());
			statement.setInt(2, id);
			ResultSet results = statement.executeQuery();
			if(results.next())
			{
				return results.getString("EINTRAG");
			} else
			{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getDatum(UUID uuid, Integer id)
	{
		try {
			PreparedStatement statement = Main.plugin.getConnection()
					.prepareStatement("SELECT * FROM "+Main.plugin.table
							+" WHERE UUID=? AND PERSONAL_ID=?");
			statement.setString(1, uuid.toString());
			statement.setInt(2, id);
			ResultSet results = statement.executeQuery();
			if(results.next())
			{
				return results.getString("DATUM");
			} else
			{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void deleteEntry(final UUID uuid, Integer personal_id)
	{
		try
		{
			PreparedStatement insert = Main.plugin.getConnection()
					.prepareStatement("DELETE FROM "+Main.plugin.table
							+" WHERE UUID=? AND PERSONAL_ID=?");
			insert.setString(1, uuid.toString());
			insert.setInt(2, personal_id);
			insert.executeUpdate();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
}
