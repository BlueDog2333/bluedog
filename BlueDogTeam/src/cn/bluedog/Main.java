package cn.bluedog;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import cn.bluedog.listener.onJoin;
import cn.bluedog.players.Leader;

public class Main extends JavaPlugin{
	public static Map<Player,Team> pt=new HashMap<Player,Team>();
	public static Map<Player,Player> pp=new HashMap<Player,Player>();
	public static Plugin plugin;
	public static Scoreboard  s;
	public static String pre;
	public int i;
	@Override
	   public void onLoad()
{
plugin = this;
}
	
	public void onEnable() {
		System.out.println("蓝狗组队开启");
		System.out.println(getConfig().getKeys(false));
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new onJoin(), this);
		try {
			pt=SLAPI.load(getDataFolder().getPath()+"\\data");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
//	    pt=(Map<Player, Team>) plugin.getConfig().get("data.save");
		pre="§6§l[§7蓝狗组队§6§l]§7>>>§8§l";
		s= Bukkit.getServer().getScoreboardManager().getMainScoreboard();
	}
	public void onDisble() {
		System.out.println("蓝狗组队已卸载");
		for(Player pl:pt.keySet()) {
			plugin.getConfig().set(pl.getName()+".name",pl.getName() );
			plugin.getConfig().set(pl.getName()+".team",pt.get(pl).getName() );
			saveConfig();
		}
		
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player p=(Player) sender;
		if(label.equalsIgnoreCase("bluedogteam")||label.equalsIgnoreCase("bdt")) {
			try {
			if(args[0].equalsIgnoreCase("create") ) {
				if(!p.hasPermission("bdteam.create")) {
					p.sendMessage(pre+"您需要bdteam.create权限");
					return false;
				}
				if(args[1]==null||args[2]==null) {
					p.sendMessage(pre+"抱歉，请填写清楚队伍名与显示名");
					return false;
				}
				for(Team team:s.getTeams()) {
					if(team.getName().equalsIgnoreCase(args[1])) {
						p.sendMessage(pre+"抱歉，已经存在该队伍，请另外起名");
						return false;
					}
					if(team.hasPlayer(p)) {
						p.sendMessage(pre+"抱歉，您已加入其他队伍，请退出该队伍后重新创建");
						return false;
					}
				}
				s.registerNewTeam(args[1]);
				s.getTeam(args[1]).setDisplayName(args[2]);
				s.getTeam(args[1]).addPlayer(p);
				pt.put(p, s.getTeam(args[1]));
				getConfig().set(p.getName()+".name",p.getName());
				getConfig().set(p.getName()+".team",args[1]);
				saveConfig();
				p.sendMessage(pre+"恭喜您创建队伍成功！");
				
				try {
					SLAPI.save(pt,getDataFolder().getPath()+"\\data");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("invite")) {
				if(!Leader.isLeader(p)) {
					p.sendMessage(pre+"抱歉，您不是队伍领导者");
					return false;
				}
				if(args[1]==null||Bukkit.getPlayer(args[1])==null) {
					p.sendMessage(pre+"抱歉，请填写邀请玩家名");
					return false;
				}
				Player offp=Bukkit.getPlayer(args[1]);
					if(offp==null) {
					p.sendMessage(pre+"请邀请在线的玩家");
					return false;
				}
				if(offp.equals(p)) {
					p.sendMessage(pre+"您不能邀请自己");
					return false;
				}
				pp.put(offp, p);
				offp.sendMessage(pre+p.getName()+"邀请您加入他的战队，输入/bdt accept同意,若您是领导者，接受邀请将会解散原来队伍");
				p.sendMessage(pre+"邀请信息已发送");
				return true;
			}
			if(args[0].equalsIgnoreCase("accept")) {
				Team team=pt.get(pp.get(p));
				if(Leader.isLeader(p)) {
					pt.remove(p);
					plugin.getConfig().set(p.getName()+".team", "bluedog");;
					plugin.getConfig().set(p.getName()+".name", "bluedog");;
				}
				for(Team t:s.getTeams()) {
					if(t.hasPlayer(p)) {
						t.removePlayer(p);
					}
					if(Leader.isLeader(p)) {
						pt.remove(p);
					}
				}
				team.addPlayer(p);
				p.sendMessage(pre+"成功加入该队伍！");
				return true;
			}
			if(args[0].equalsIgnoreCase("friendfire")) {
				if(!Leader.isLeader(p)) {
					p.sendMessage(pre+"抱歉，您不是队伍领导者");
					return false;
				}
				if(args[1]==null) {
					p.sendMessage(pre+"抱歉,请填写清楚");
					return false;
				}
				Team team=pt.get(p);
				if(args[1].equalsIgnoreCase("off")) {
					Leader.dallowFire(team);
					p.sendMessage(pre+"队伍开火已关闭");
					return true;
				}
				if(args[1].equalsIgnoreCase("on")) {
					Leader.allowFire(team);
					p.sendMessage(pre+"队伍开火已开启");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("kick")) {
				if(!Leader.isLeader(p)) {
					p.sendMessage(pre+"抱歉，您不是队伍领导者");
					return false;
				}
				Team team=pt.get(p);
				if(Bukkit.getPlayer(args[1]).equals(p)) {
					p.sendMessage(pre+"您不能踢自己");
					return false;
				}
				Leader.kick(team,Bukkit.getPlayer(args[1]));
				p.sendMessage(pre+"踢出成功");
				return true;
			}
			if(args[0].equalsIgnoreCase("list")) {
				for(Player pl:pt.keySet()) {
					p.sendMessage("§7§l"+pl.getName()+"§9:§8§l"+pt.get(pl).getDisplayName());
					
				}
				return true;
			} 
			if(args[0].equalsIgnoreCase("prefix") ) {
				if(!p.hasPermission("bdteam.prefix")) {
					p.sendMessage(pre+"您需要bdteam.prefix权限");
					return false;
				}
				
				if(args[1]==null) {
					p.sendMessage(pre+"请填写内容 ");
					return false;
				}
				if(!Leader.isLeader(p)) {
					p.sendMessage(pre+"只有领导者才能修改前缀");
					return false;
				}
				Team team=pt.get(p);
				team.setPrefix(args[1].replace("&", "§"));
				    p.sendMessage(pre+"成功开启前缀");
				return true;
			}
			if(args[0].equalsIgnoreCase("")) {
			p.sendMessage("§6§l==================");
			p.sendMessage("§7/bluedogteam create <队伍名>  <队伍显示名> 创建一个新的队伍");
			p.sendMessage("§7/bluedogteam friendfire <on/off> 打开/关闭 允许队伍开火");
			p.sendMessage("§7/bluedogteam invite <玩家> 邀请玩家加入您的队伍");
			p.sendMessage("§7/bluedogteam kick <玩家> 将玩家踢出自己队伍");
			p.sendMessage("§7/bluedogteam list  查看所有队伍");
			p.sendMessage("§7/bluedogteam prefix <前缀> 为队伍增加前缀");
			p.sendMessage("§6§l==================");
			return false;
			}
			}catch(Exception e){
				p.sendMessage("§6§l==================");
				p.sendMessage("§7/bluedogteam create <队伍名>  <队伍显示名> 创建一个新的队伍");
				p.sendMessage("§7/bluedogteam friendfire <on/off> 打开/关闭 允许队伍开火");
				p.sendMessage("§7/bluedogteam invite <玩家> 邀请玩家加入您的队伍");
				p.sendMessage("§7/bluedogteam kick <玩家> 将玩家踢出自己队伍");
				p.sendMessage("§7/bluedogteam list  查看所有队伍");
				p.sendMessage("§7/bluedogteam prefix <前缀> 为队伍增加前缀");
				p.sendMessage("§6§l==================");
			}
		}
		return false;
		
	}
}
