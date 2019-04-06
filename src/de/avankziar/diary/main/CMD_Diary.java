package de.avankziar.diary.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CMD_Diary implements CommandExecutor 
{
	public String tl(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public TextComponent tc(String s)
	{
		return new TextComponent(s);
	}
	
	public YamlConfiguration lg = Main.plugin.lg();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(!(sender instanceof Player)) 
		{
			return false;
		}
		Player p = (Player)sender;
		boolean mysql = Main.plugin.cfg.getString("DY.mysql.status").equals("on");
		YamlConfiguration cfg = Main.plugin.cfg;
		if(args.length==0)
		{
			if(p.hasPermission("diary.info.admin")) 
			{
				TextComponent msg2 = tc(tl(lg.getString("DY.CMD_Diary.info.msg02")));
				msg2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary"));
				TextComponent msg3 = tc(tl(lg.getString("DY.CMD_Diary.info.msg03")));
				msg3.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary entry <Player> <Quest-Relation> <Eintrag>"));
				TextComponent msg4 = tc(tl(lg.getString("DY.CMD_Diary.info.msg04")));
				msg4.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary page <Page>"));
				TextComponent msg5 = tc(tl(lg.getString("DY.CMD_Diary.info.msg05")));
				msg5.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary last"));
				TextComponent msg6 = tc(tl(lg.getString("DY.CMD_Diary.info.msg06")));
				msg6.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary delete <Player> <ID>"));
				TextComponent msg7 = tc(tl(lg.getString("DY.CMD_Diary.info.msg07")));
				msg7.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary reload"));
				TextComponent msg8 = tc(tl(lg.getString("DY.CMD_Diary.info.msg08")));
				msg8.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary mysql <host> <port> <database> <username> <passwort> <ssl>"));
				TextComponent msg9 = tc(tl(lg.getString("DY.CMD_Diary.info.msg09")));
				msg9.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary questrelation <search> <page>"));
				TextComponent msg10 = tc(tl(lg.getString("DY.CMD_Diary.info.msg10")));
				msg10.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary givebook <volume>"));
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.info.msg01")));
				p.spigot().sendMessage(msg2);
				p.spigot().sendMessage(msg3);
				p.spigot().sendMessage(msg4);
				p.spigot().sendMessage(msg5);
				p.spigot().sendMessage(msg6);
				p.spigot().sendMessage(msg7);
				p.spigot().sendMessage(msg8);
				p.spigot().sendMessage(msg9);
				p.spigot().sendMessage(msg10);
				return true;
			} else if(p.hasPermission("diary.info"))
			{
				TextComponent msg1 = tc(tl(lg.getString("DY.CMD_Diary.info.msg02")));
				msg1.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary"));
				TextComponent msg2 = tc(tl(lg.getString("DY.CMD_Diary.info.msg03")));
				msg2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary entry <Player> <Quest-Relation> <Eintrag>"));
				TextComponent msg3 = tc(tl(lg.getString("DY.CMD_Diary.info.msg04")));
				msg3.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary page <Page>"));
				TextComponent msg4 = tc(tl(lg.getString("DY.CMD_Diary.info.msg05")));
				msg4.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary last"));
				TextComponent msg9 = tc(tl(lg.getString("DY.CMD_Diary.info.msg09")));
				msg9.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary questrelation <search> <page>"));
				TextComponent msg10 = tc(tl(lg.getString("DY.CMD_Diary.info.msg10")));
				msg10.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/diary givebook <volume>"));
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.info.msg01")));
				p.spigot().sendMessage(msg1);
				p.spigot().sendMessage(msg2);
				p.spigot().sendMessage(msg3);
				p.spigot().sendMessage(msg4);
				p.spigot().sendMessage(msg9);
				p.spigot().sendMessage(msg10);
				return true;
			} else
			{
				no_perm(p);
				return false;
			}
		} else if(args[0].equals("eintrag") || args[0].equals("entry")) 
		{
			if(!p.hasPermission("diary.entry")) 
			{
				no_perm(p);
				return false;
			}
			if(args.length<4) 
			{
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.entry.msg01")));
				return false;
			}
			String player = args[1];
			if(Bukkit.getPlayer(player) == null)
			{
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.msg04")));
				return false;
			}
			Player t = Bukkit.getPlayer(player);
			UUID uuid = t.getUniqueId();
			String quest_relation = args[2];
			String quest_relation_raw = removeColor(quest_relation);
			if(!mysql) 
			{
				no_mysql(p); 
				return false;
			}
			String entry = "";
	        for(int i = 3; i < args.length; i++)
	        {
	            String arg = args[i] + " ";
	            entry = entry + arg;
	        }
	        Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat(lg.getString("DY.CMD_Diary.entry.date_format"));
			String time = format.format(now).toString();
			if(MySQL_Diary.getList_id(uuid) == 0)
			{
				 MySQL_Diary.insert_diary(uuid, 1, quest_relation_raw, quest_relation, entry, time);
				 MySQL_Diary.insert_list(uuid, 1);
			} else
			{
				int id = MySQL_Diary.getList_id(uuid) + 1;
				MySQL_Diary.insert_diary(uuid, id, quest_relation_raw, quest_relation, entry, time);
				MySQL_Diary.update_list(uuid, id);
			}
			if(quest_relation_raw.equals("null"))
			{
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.entry.msg02"))
		        		.replaceAll("%time%", time).replaceAll("%entry%", entry));
		        if(!p.getName().equals(t.getName()))
		        {
		        	t.sendMessage(tl(lg.getString("DY.CMD_Diary.entry.msg02"))
		        			.replaceAll("%time%", time).replaceAll("%entry%", entry));
		        }
		        return true;
			} else
			{
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.entry.msg03"))
		        		.replaceAll("%time%", time).replaceAll("%entry%", entry).replaceAll("%qr%", quest_relation_raw));
		        if(!p.getName().equals(t.getName()))
		        {
		        	t.sendMessage(tl(lg.getString("DY.CMD_Diary.entry.msg03"))
		        			.replaceAll("%time%", time).replaceAll("%entry%", entry).replaceAll("%qr%", quest_relation_raw));
		        }
		        return true;
			}
		} else if(args[0].equals("seite") || args[0].equals("page")) 
		{
			if(!p.hasPermission("diary.page")) 
			{
				no_perm(p);
				return false;
			}
			if(!mysql) 
			{
				no_mysql(p); 
				return false;
			}
			if(args.length != 2) 
			{
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.page.msg01")));
				return false;
			}
			UUID uuid = p.getUniqueId();
			try
			{
				Integer.parseInt(args[1]);
			} catch(NumberFormatException e){
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.msg05")));
				return false;
			}
			int page = new Integer(args[1]);
			int start = page*5+1;
			int stopp = page*5+5;
			/*Bei page = 0 haben wir start 1 und stopp 5
			 * Bei page = 1 haben wir start 6 und stopp 10
			 * Bei page = 2 haben wir start 11 und stopp 15*/
			int list = MySQL_Diary.getList_id(uuid);
			if(list == 0)
			{
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.page.msg02")));
				return false;
			}
			if(stopp>list)
			{
				stopp = list;
				start = list-4;
			}
			p.sendMessage(tl(lg.getString("DY.CMD_Diary.page.msg03")).replaceAll("%page%", String.valueOf(page)));
			while(stopp>=start)
			{
				if(MySQL_Diary.getEntry(uuid,start) != null)
				{
					
					if(MySQL_Diary.getQuestRelation(uuid,start).contains("null"))
					{
						p.sendMessage(tl(lg.getString("DY.CMD_Diary.page.msg04")
								.replace("%id%", String.valueOf(start))
								.replace("%date%", MySQL_Diary.getDatum(uuid, start))
								.replaceAll("%entry%", MySQL_Diary.getEntry(uuid,start))));
					} else
					{
						p.sendMessage(tl(lg.getString("DY.CMD_Diary.page.msg05")
								.replace("%id%", String.valueOf(start))
								.replace("%date%", MySQL_Diary.getDatum(uuid, start))
								.replaceAll("%entry%", MySQL_Diary.getEntry(uuid,start))
								.replaceAll("%qr%", MySQL_Diary.getQuestRelation(uuid,start))));
					}
				}
				start++;
			}
			return true;
		} else if(args[0].equals("last") || args[0].equals("letzte")) 
		{
			if(!p.hasPermission("diary.last")) {
				no_perm(p);
				return false;
			}
			if(!mysql) 
			{
				no_mysql(p); 
				return false;
			}
			if(args.length != 1) {
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.last.msg01")));
				return false;
			}
			UUID uuid = p.getUniqueId();
			int list = MySQL_Diary.getList_id(uuid);
			int start = list-4;
			int stopp = list;
			/*Bei page = 0 haben wir start 1 und stopp 5
			 * Bei page = 1 haben wir start 6 und stopp 10
			 * Bei page = 2 haben wir start 11 und stopp 15*/
			if(list == 0)
			{
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.last.msg02")));
				return false;
			}
			p.sendMessage(tl(lg.getString("DY.CMD_Diary.last.msg03")));
			while(stopp>=start)
			{
				if(MySQL_Diary.getEntry(uuid,start) != null)
				{
					if(MySQL_Diary.getQuestRelation(uuid,start).contains("null"))
					{
						p.sendMessage(tl(lg.getString("DY.CMD_Diary.last.msg04")
								.replace("%id%", String.valueOf(start))
								.replace("%date%", MySQL_Diary.getDatum(uuid, start))
								.replaceAll("%entry%", MySQL_Diary.getEntry(uuid,start))));
					} else
					{
						p.sendMessage(tl(lg.getString("DY.CMD_Diary.last.msg05")
								.replace("%id%", String.valueOf(start))
								.replace("%date%", MySQL_Diary.getDatum(uuid, start))
								.replaceAll("%entry%", MySQL_Diary.getEntry(uuid,start))
								.replaceAll("%qr%", MySQL_Diary.getQuestRelation(uuid,start))));
					}
				}
				start++;
			}
			return true;
		} else if(args[0].equals("questrelation") || args[0].equals("qr") || args[0].equalsIgnoreCase("search")) 
		{
			if(!p.hasPermission("diary.questrelation")) {
				no_perm(p);
				return false;
			}
			if(!mysql) 
			{
				no_mysql(p); 
				return false;
			}
			if(args.length < 2 || args.length > 3 ) {
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.qr.msg01")));
				return false;
			}
			UUID uuid = p.getUniqueId();
			String qr = args[1];
			if(args.length==2)
			{
				int list = MySQL_Diary.getList_id(uuid);
				int start = list-4;
				int stopp = list;
				if(list == 0)
				{
					p.sendMessage(tl(lg.getString("DY.CMD_Diary.qr.msg02")));
					return false;
				}
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.qr.msg04")));
				while(stopp>=start)
				{
					if(MySQL_Diary.getEntryPerQuest(uuid,start,qr) != null)
					{
						p.sendMessage(tl(lg.getString("DY.CMD_Diary.qr.msg05")
								.replace("%id%", String.valueOf(start))
								.replace("%date%", MySQL_Diary.getDatum(uuid, start))
								.replaceAll("%entry%", MySQL_Diary.getEntry(uuid,start))
								.replaceAll("%qr%", MySQL_Diary.getQuestRelation(uuid,start))));
					}
					start++;
				}
				return true;
			} else
			{
				try
				{
					Integer.parseInt(args[2]);
				} catch(NumberFormatException e){
					p.sendMessage(tl(lg.getString("DY.CMD_Diary.msg05")));
					return false;
				}
				int page = new Integer(args[2]);
				int start = page*5+1;
				int stopp = page*5+5;
				/*Bei page = 0 haben wir start 1 und stopp 5
				 * Bei page = 1 haben wir start 6 und stopp 10
				 * Bei page = 2 haben wir start 11 und stopp 15*/
				int list = MySQL_Diary.getList_id(uuid);
				if(list == 0)
				{
					p.sendMessage(tl(lg.getString("DY.CMD_Diary.qr.msg02")));
					return false;
				}
				if(stopp>list)
				{
					stopp = list;
					start = list-4;
				}
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.qr.msg03")).replaceAll("%page%", String.valueOf(page)));
				while(stopp>=start)
				{
					if(MySQL_Diary.getEntryPerQuest(uuid,start,qr) != null)
					{
						p.sendMessage(tl(lg.getString("DY.CMD_Diary.qr.msg05")
								.replace("%id%", String.valueOf(start))
								.replace("%date%", MySQL_Diary.getDatum(uuid, start))
								.replaceAll("%entry%", MySQL_Diary.getEntry(uuid,start))
								.replaceAll("%qr%", MySQL_Diary.getQuestRelation(uuid,start))));
					}
					start++;
				}
				return true;
			}
		} else if(args[0].equalsIgnoreCase("givebook") ||args[0].equalsIgnoreCase("gb")) 
		{
			if(!p.hasPermission("diary.givebook")) {
				no_perm(p);
				return false;
			}
			if(!mysql) 
			{
				no_mysql(p); 
				return false;
			}
			if(args.length != 2) {
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.givebook.msg01")));
				return false;
			}
			UUID uuid = p.getUniqueId();
			try
			{
				Integer.parseInt(args[1]);
			} catch(NumberFormatException e){
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.msg05")));
				return false;
			}
			int bookvolume = new Integer(args[1]);
			int list = MySQL_Diary.getList_id(uuid);
			if(list == 0)
			{
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.givebook.msg02")));
				return false;
			}
			int start = bookvolume*50+1;
			int stopp = bookvolume*50+50;
			if(stopp>list)
			{
				stopp = list;
				start = list-50;
				if(start<1)
				{
					start = 1;
				}
			}
			ItemStack is = new ItemStack(Material.WRITTEN_BOOK,1);
	        BookMeta bm = (BookMeta) is.getItemMeta();
	        bm.setAuthor(p.getName());
	        bm.setDisplayName(tl(lg.getString("DY.CMD_Diary.givebook.msg03").replaceAll("%nr%", String.valueOf(bookvolume)).replaceAll("%player%", p.getName())));
	        bm.setTitle(tl(lg.getString("DY.CMD_Diary.givebook.msg03").replaceAll("%nr%", String.valueOf(bookvolume)).replaceAll("%player%", p.getName())));
			while(stopp>=start)
			{
				if(MySQL_Diary.getEntry(uuid,start) != null)
				{
					if(MySQL_Diary.getQuestRelation(uuid,start).contains("null"))
					{
						bm.addPage(tl(lg.getString("DY.CMD_Diary.givebook.msg04")
								.replace("%id%", String.valueOf(start))
								.replace("%date%", MySQL_Diary.getDatum(uuid, start))
								.replaceAll("%entry%", MySQL_Diary.getEntry(uuid,start))));
					} else
					{
						bm.addPage(tl(lg.getString("DY.CMD_Diary.givebook.msg05")
								.replace("%id%", String.valueOf(start))
								.replace("%date%", MySQL_Diary.getDatum(uuid, start))
								.replaceAll("%entry%", MySQL_Diary.getEntry(uuid,start))
								.replaceAll("%qr%", MySQL_Diary.getQuestRelation(uuid,start))));
					}
				}
				start++;
			}
			is.setItemMeta(bm);
			p.getInventory().addItem(is);
			p.sendMessage(tl(lg.getString("DY.CMD_Diary.givebook.msg06").replaceAll("%nr%", String.valueOf(bookvolume))));
			return true;
		} else if(args[0].equals("delete") || args[0].equals("löschen")) 
		{
			if(!p.hasPermission("diary.delete")) {
				no_perm(p);
				return false;
			}
			if(!mysql) 
			{
				no_mysql(p); 
				return false;
			}
			if(args.length != 3) {
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.delete.msg01")));
				return false;
			}
			String player = args[1];
			if(Bukkit.getPlayer(player) == null)
			{
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.msg04")));
				return false;
			}
			Player t = Bukkit.getPlayer(player);
			UUID uuid = t.getUniqueId();
			int id = new Integer(args[2]);
			if(MySQL_Diary.personal_ID_exists(id, uuid))
			{
				MySQL_Diary.deleteEntry(uuid, id);
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.delete.msg02")).replaceAll("%id%", String.valueOf(id)));
				return true;
			} else {
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.delete.msg03")));
				return false;
			}
		} else if(args[0].equalsIgnoreCase("mysql"))
		{
			if(!p.hasPermission("diary.mysql"))
			{
				no_perm(p);
				return false;
			}
			if(args.length != 7)
			{
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.mysql.msg01")));
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.mysql.msg02")));
				return false;
			}
			if(args[6].equalsIgnoreCase("yes") || args[6].equalsIgnoreCase("no"))
			{
				String host = args[1];
				String ports = args[2];
				int port = Integer.parseInt(ports);
				String database = args[3];
				String username = args[4];
				String password = args[5];
				String ssl = args[6];
				cfg.set("DY.mysql.status", "on");
				cfg.set("DY.mysql.host", host);
				cfg.set("DY.mysql.port", port);
				cfg.set("DY.mysql.database", database);
				cfg.set("DY.mysql.username", username);
				cfg.set("DY.mysql.password", password);
				cfg.set("DY.mysql.ssl", ssl);
				Main.plugin.saveCon();
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.mysql.msg03")));
				return true;
			} else
			{
				p.sendMessage(tl(lg.getString("DY.CMD_Diary.mysql.msg04")));
			}
		} else if(args[0].equalsIgnoreCase("reload"))
		{
			if(!p.hasPermission("diary.reload"))
			{
				no_perm(p);
				return false;
			}
			Main plugin = (Main) Bukkit.getServer().getPluginManager().getPlugin("Diary");
		    plugin.getPluginLoader().disablePlugin(plugin);
		    plugin.getPluginLoader().enablePlugin(plugin);
		    p.sendMessage(tl(lg.getString("DY.CMD_Diary.reload.msg01")));
		    Main.plugin.getServer().getConsoleSender().sendMessage(ChatColor.GOLD+"Diary "+ChatColor.GREEN+" was reloaded!");
		    return true;
		} else
		{
			p.sendMessage(tl(lg.getString("DY.CMD_Diary.msg03")));
			return false;
		}
		return false;
	}

	private void no_mysql(Player p)
	{
		p.sendMessage(tl(lg.getString("DY.CMD_Diary.msg02")));
	}
	
	private void no_perm(Player p)
	{
		p.sendMessage(tl(lg.getString("DY.CMD_Diary.msg01")));
	}
	
	private String removeColor(String msg)
	{
		String a = msg.replaceAll("&0", "").replaceAll("&1", "").replaceAll("&2", "").replaceAll("&3", "").replaceAll("&4", "").replaceAll("&5", "")
				.replaceAll("&6", "").replaceAll("&7", "").replaceAll("&8", "").replaceAll("&9", "")
				.replaceAll("&a", "").replaceAll("&b", "").replaceAll("&c", "").replaceAll("&d", "").replaceAll("&e", "").replaceAll("&f", "")
				.replaceAll("&k", "").replaceAll("&l", "").replaceAll("&m", "").replaceAll("&n", "").replaceAll("&o", "").replaceAll("&r", "")
				.replaceAll("&A", "").replaceAll("&B", "").replaceAll("&C", "").replaceAll("&D", "").replaceAll("&E", "").replaceAll("&F", "")
				.replaceAll("&K", "").replaceAll("&L", "").replaceAll("&M", "").replaceAll("&N", "").replaceAll("&O", "").replaceAll("&R", "");
		return a;
	}
}
