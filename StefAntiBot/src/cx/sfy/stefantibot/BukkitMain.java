package cx.sfy.stefantibot;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMain extends JavaPlugin implements Listener {

	public static Plugin p;
	
	@Override
	public void onEnable() {
		p = this;
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		String ip = e.getPlayer().getAddress().getHostName();
		Bukkit.getScheduler().runTaskAsynchronously(p, new Runnable() {
			@Override
			public void run() {
				
				if (BlacklistMGR.isProxy(ip)) {
					System.out.println(ip);
					BlacklistMGR.blacklistIP(ip);
				}
				
			}
		});
	}
	
	
	
}
