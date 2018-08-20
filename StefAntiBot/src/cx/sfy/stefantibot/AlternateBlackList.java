package cx.sfy.stefantibot;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class AlternateBlackList {

	private static String[] illegalcannonics = {"ovh.net", "gov.tl", "accelerate.net"};
	
	public static boolean isAllowed(InetSocketAddress adr) {
		InetAddress ip = adr.getAddress();
		String cannonical = ip.getCanonicalHostName();
		for (String stg : illegalcannonics) {
			if (cannonical.contains(stg)) {
				return false;
			}
		}
		return true;
	}
	
}
