package de.avankziar.diary.main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin 
{
	
	public static Main plugin;
	public File c = null;
	public YamlConfiguration cfg = new YamlConfiguration();
	public File up = null;
	public YamlConfiguration upd = new YamlConfiguration();
	public File g = null;
	public YamlConfiguration ger = new YamlConfiguration();
	public File e = null;
	public YamlConfiguration eng = new YamlConfiguration();
	private Connection connection;
	public String host, database, username, password, table, table2, ssl;
	public int port;
	
	@Override
	public void onEnable() 
	{
		plugin = this;
		c = new File(plugin.getDataFolder(), "config.yml");
		up = new File(plugin.getDataFolder(), "update.yml");
		g = new File(plugin.getDataFolder(), "german.yml");
		e = new File(plugin.getDataFolder(), "english.yml");
		mkdir();
	    loadYamls();
	    getCommand("diary").setExecutor(new CMD_Diary());
	    if(cfg.getString("DY.mysql.status").equals("on"))
		{
			MySQLSetup();
			TableSetup("CREATE TABLE IF NOT EXISTS "+this.table
					+"(GLOBAL_ID INT AUTO_INCREMENT PRIMARY KEY,UUID VARCHAR(40), PERSONAL_ID INT, QUEST_RELATION_RAW TEXT, QUEST_RELATION TEXT, EINTRAG TEXT, DATUM VARCHAR(40))");
			TableSetup("CREATE TABLE IF NOT EXISTS "+this.table2+"(UUID VARCHAR(40), List_ID INT)");
		}
	    getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "Diary" + ChatColor.WHITE + " is " + ChatColor.DARK_GREEN + "running" + ChatColor.WHITE + "!");
	    getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "Diary" + ChatColor.WHITE + " is " +
	    ChatColor.DARK_GREEN + "development " + ChatColor.WHITE + "by " + ChatColor.GOLD + "Avankziar"+ ChatColor.WHITE+ "!");
	}
	
	public void onDisable() 
	{
		saveCon();
		saveUpd();
	}
	
	private void mkdir() 
	{
		if(!c.exists()) 
		{
			saveResource("config.yml", false);
		}
		if(!up.exists())
		{
			saveResource("update.yml", false);
		}
		if(!g.exists())
		{
			saveResource("german.yml", false);
		}
		if(!e.exists())
		{
			saveResource("english.yml", false);
		}
	}
	
	public void loadYamls() 
	{
		try 
		{
			cfg.load(c);
		} catch (IOException | InvalidConfigurationException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			upd.load(up);
		} catch (IOException | InvalidConfigurationException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			ger.load(g);
		} catch (IOException | InvalidConfigurationException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			eng.load(e);
		} catch (IOException | InvalidConfigurationException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	public void saveCon() 
	{
	    try 
	    {
	        cfg.save(c);
	    } catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	public void saveUpd() 
	{
	    try 
	    {
	        upd.save(up);
	    } catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	public void saveGer() 
	{
	    try 
	    {
	        ger.save(g);
	    } catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	public void saveEng() 
	{
	    try 
	    {
	        eng.save(e);
	    } catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	public void MySQLSetup()
	{
		host = cfg.getString("DY.mysql.host");
		port = cfg.getInt("DY.mysql.port");
		database = cfg.getString("DY.mysql.database");
		username = cfg.getString("DY.mysql.username");
		password = cfg.getString("DY.mysql.password");
		table = "diary";
		table2 = "diary_list";
		ssl = cfg.getString("DY.mysql.ssl");
		try
		{
			synchronized (this)
			{
				if(getConnection() != null && !getConnection().isClosed())
				{
					return;
				}
				Class.forName("com.mysql.jdbc.Driver");
				if(ssl.equals("no"))
				{
					setConnection(DriverManager.getConnection("jdbc:mysql://"+this.host+":"+this.port+"/"+this.database+"?autoReconnect=true&useUnicode=yes&useSSL=false&",
							this.username,this.password));
					getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "Diary" + ChatColor.DARK_AQUA + " MySQL Modus " + ChatColor.YELLOW + " without SSL" 
			    			+ ChatColor.WHITE + " is " + ChatColor.DARK_GREEN + "activated" + ChatColor.WHITE + "!");
				} else if(ssl.equals("yes"))
				{
					setConnection(DriverManager.getConnection("jdbc:mysql://"+this.host+":"+this.port+"/"+this.database+"??autoReconnect=true&useUnicode=yes&useSSL=true&",
							this.username,this.password));
					getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "Diary" + ChatColor.DARK_AQUA + " MySQL Modus " + ChatColor.GREEN + " with SSL" 
			    			+ ChatColor.WHITE + " is " + ChatColor.DARK_GREEN + "activated" + ChatColor.WHITE + "!");
				}
				
			}
		} catch(SQLException e)
		{
			e.printStackTrace();
		} catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public void TableSetup(String qyr)
	{
		try
		{
			PreparedStatement statement = getConnection()
					.prepareStatement(qyr);
			statement.executeUpdate();
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public Connection getConnection()
	{
		return connection;
	}
	
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}
	
	public YamlConfiguration lg() {
		if(cfg.getString("DY.language").equalsIgnoreCase("german")) {
			return ger;
		} else if(cfg.getString("DY.language").equalsIgnoreCase("english")) {
			return eng;
		}
		return cfg;
	}
}
