package cn.bluedog.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Team;

import cn.bluedog.Main;

public class onJoin implements Listener{
	@EventHandler
	public void whenJoin(PlayerJoinEvent e) {
		Player p=e.getPlayer();
		System.out.println(Main.plugin.getConfig().getString(p.getName()+".team"));
		for(String a:Main.plugin.getConfig().getKeys(false)) {
			System.out.println(a);
			if(p.getName().equals(a)) {
				System.out.println(Main.plugin.getConfig().getString(a+".team"));
				Team t=Main.s.getTeam(Main.plugin.getConfig().getString(a+".team"));
				
				
				System.out.println(t);
				Main.pt.put(p, t);
				System.out.println("true");
			}
		}
	}
}
