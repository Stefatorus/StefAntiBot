package cx.sfy.stefantibot;

import java.net.InetSocketAddress;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeMain extends Plugin implements Listener {

	public static Plugin p;
	
	@Override
	public void onEnable() {
		p = this;
		ProxyServer.getInstance().getPluginManager().registerListener(this, this);
	}
	
	@Override
	public void onDisable() {
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PreLoginEvent e) {
		InetSocketAddress adr = e.getConnection().getAddress();
		String ip = adr.getHostName();
//		AWFUL PERFORMANCE. DO NOT USE.
//		e.setCancelReason(new TextComponent("The server is currently unavailable"));
//		e.setCancelled(AlternateBlackList.isAllowed(adr));
		ProxyServer.getInstance().getScheduler().runAsync(p, new Runnable() {
			@Override
			public void run() {
				
				if (BlacklistMGR.isProxy(ip)) {
					BlacklistMGR.blacklistIP(ip);
				}
				
			}
		});
	}
	
}
