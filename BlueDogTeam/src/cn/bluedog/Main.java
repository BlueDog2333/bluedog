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
		System.out.println("������ӿ���");
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
		pre="��6��l[��7������ӡ�6��l]��7>>>��8��l";
		s= Bukkit.getServer().getScoreboardManager().getMainScoreboard();
	}
	public void onDisble() {
		System.out.println("���������ж��");
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
					p.sendMessage(pre+"����Ҫbdteam.createȨ��");
					return false;
				}
				if(args[1]==null||args[2]==null) {
					p.sendMessage(pre+"��Ǹ������д�������������ʾ��");
					return false;
				}
				for(Team team:s.getTeams()) {
					if(team.getName().equalsIgnoreCase(args[1])) {
						p.sendMessage(pre+"��Ǹ���Ѿ����ڸö��飬����������");
						return false;
					}
					if(team.hasPlayer(p)) {
						p.sendMessage(pre+"��Ǹ�����Ѽ����������飬���˳��ö�������´���");
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
				p.sendMessage(pre+"��ϲ����������ɹ���");
				
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
					p.sendMessage(pre+"��Ǹ�������Ƕ����쵼��");
					return false;
				}
				if(args[1]==null||Bukkit.getPlayer(args[1])==null) {
					p.sendMessage(pre+"��Ǹ������д���������");
					return false;
				}
				Player offp=Bukkit.getPlayer(args[1]);
					if(offp==null) {
					p.sendMessage(pre+"���������ߵ����");
					return false;
				}
				if(offp.equals(p)) {
					p.sendMessage(pre+"�����������Լ�");
					return false;
				}
				pp.put(offp, p);
				offp.sendMessage(pre+p.getName()+"��������������ս�ӣ�����/bdt acceptͬ��,�������쵼�ߣ��������뽫���ɢԭ������");
				p.sendMessage(pre+"������Ϣ�ѷ���");
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
				p.sendMessage(pre+"�ɹ�����ö��飡");
				return true;
			}
			if(args[0].equalsIgnoreCase("friendfire")) {
				if(!Leader.isLeader(p)) {
					p.sendMessage(pre+"��Ǹ�������Ƕ����쵼��");
					return false;
				}
				if(args[1]==null) {
					p.sendMessage(pre+"��Ǹ,����д���");
					return false;
				}
				Team team=pt.get(p);
				if(args[1].equalsIgnoreCase("off")) {
					Leader.dallowFire(team);
					p.sendMessage(pre+"���鿪���ѹر�");
					return true;
				}
				if(args[1].equalsIgnoreCase("on")) {
					Leader.allowFire(team);
					p.sendMessage(pre+"���鿪���ѿ���");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("kick")) {
				if(!Leader.isLeader(p)) {
					p.sendMessage(pre+"��Ǹ�������Ƕ����쵼��");
					return false;
				}
				Team team=pt.get(p);
				if(Bukkit.getPlayer(args[1]).equals(p)) {
					p.sendMessage(pre+"���������Լ�");
					return false;
				}
				Leader.kick(team,Bukkit.getPlayer(args[1]));
				p.sendMessage(pre+"�߳��ɹ�");
				return true;
			}
			if(args[0].equalsIgnoreCase("list")) {
				for(Player pl:pt.keySet()) {
					p.sendMessage("��7��l"+pl.getName()+"��9:��8��l"+pt.get(pl).getDisplayName());
					
				}
				return true;
			} 
			if(args[0].equalsIgnoreCase("prefix") ) {
				if(!p.hasPermission("bdteam.prefix")) {
					p.sendMessage(pre+"����Ҫbdteam.prefixȨ��");
					return false;
				}
				
				if(args[1]==null) {
					p.sendMessage(pre+"����д���� ");
					return false;
				}
				if(!Leader.isLeader(p)) {
					p.sendMessage(pre+"ֻ���쵼�߲����޸�ǰ׺");
					return false;
				}
				Team team=pt.get(p);
				team.setPrefix(args[1].replace("&", "��"));
				    p.sendMessage(pre+"�ɹ�����ǰ׺");
				return true;
			}
			if(args[0].equalsIgnoreCase("")) {
			p.sendMessage("��6��l==================");
			p.sendMessage("��7/bluedogteam create <������>  <������ʾ��> ����һ���µĶ���");
			p.sendMessage("��7/bluedogteam friendfire <on/off> ��/�ر� ������鿪��");
			p.sendMessage("��7/bluedogteam invite <���> ������Ҽ������Ķ���");
			p.sendMessage("��7/bluedogteam kick <���> ������߳��Լ�����");
			p.sendMessage("��7/bluedogteam list  �鿴���ж���");
			p.sendMessage("��7/bluedogteam prefix <ǰ׺> Ϊ��������ǰ׺");
			p.sendMessage("��6��l==================");
			return false;
			}
			}catch(Exception e){
				p.sendMessage("��6��l==================");
				p.sendMessage("��7/bluedogteam create <������>  <������ʾ��> ����һ���µĶ���");
				p.sendMessage("��7/bluedogteam friendfire <on/off> ��/�ر� ������鿪��");
				p.sendMessage("��7/bluedogteam invite <���> ������Ҽ������Ķ���");
				p.sendMessage("��7/bluedogteam kick <���> ������߳��Լ�����");
				p.sendMessage("��7/bluedogteam list  �鿴���ж���");
				p.sendMessage("��7/bluedogteam prefix <ǰ׺> Ϊ��������ǰ׺");
				p.sendMessage("��6��l==================");
			}
		}
		return false;
		
	}
}
