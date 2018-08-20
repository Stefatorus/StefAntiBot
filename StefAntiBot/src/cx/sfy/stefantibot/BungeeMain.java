package cx.sfy.stefantibot;

import java.net.InetSocketAddress;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeMain extends Plugin implements Listener {

	@Override
	public void onEnable() {
		ProxyServer.getInstance().getPluginManager().registerListener(this, this);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PreLoginEvent e) {
		InetSocketAddress adr = e.getConnection().getAddress();
		String ip = adr.getHostName();
		// https://www.spigotmc.org/wiki/common-development-pitfalls-bungeecord/#blocking-the-i-o-threads
		e.registerIntent(this);
		ProxyServer.getInstance().getScheduler().runAsync(this, () -> {
			if (BlacklistMGR.isProxy(ip)) {
				BlacklistMGR.blacklistIP(ip);
				e.setCancelReason(new TextComponent("The server is currently unavailable"));
				e.setCancelled(AlternateBlackList.isAllowed(adr));
				e.completeIntent(BungeeMain.this);
			}
		});
	}
}
