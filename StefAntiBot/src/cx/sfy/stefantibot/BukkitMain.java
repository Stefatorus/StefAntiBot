package cx.sfy.stefantibot;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import cx.sfy.stefantibot.blacklist.BlacklistMGR;

public class BukkitMain extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent e) {
		String ip = e.getAddress().getHostName();
		Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
			if (BlacklistMGR.isProxy(ip)) {
				BlacklistMGR.blacklistIP(ip);
			}
		});
	}
}
