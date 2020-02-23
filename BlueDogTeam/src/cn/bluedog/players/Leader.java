package cn.bluedog.players;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import cn.bluedog.Main;

public class Leader {

	public static boolean isLeader(Player p) {
		for(Team team:Main.s.getTeams()) {
			if(Main.pt.containsKey(p)) {
			if(Main.pt.get(p).equals(team)) {
				return true;
			}
			}
		}
		return false;
	}

	public static void allowFire(Team team) {
		team.setAllowFriendlyFire(true);
		
	}

	public static void dallowFire(Team team) {
		team.setAllowFriendlyFire(false);
		
	}

	public static void kick(Team team, Player player) {
		team.removePlayer(player);
		
	}

}
