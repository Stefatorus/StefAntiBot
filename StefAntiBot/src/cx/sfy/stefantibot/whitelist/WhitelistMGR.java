package cx.sfy.stefantibot.whitelist;

import java.util.HashSet;
import java.util.Set;

public class WhitelistMGR {
	
	/*
	 * The whitelist system uses the whitelist cache to have a copy
	 * of the ip whitelist server-side. It will also automatically
	 * clean up the ipset each restart.
	 */
	
	
	private static final String flushcmd = "ipset -F tempwhitelist";
	private static final String whitecmd = "ipset -A tempwhitelist %ip%";
	
	private static Set<String> whitelistcache = new HashSet<String>();
	
	
}
