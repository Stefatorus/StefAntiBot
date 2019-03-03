package cx.sfy.stefantibot.blacklist;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class BlacklistMGR {

	// Command to use on linux. Should be changed when needed.
	// Person still needs to create his firewall system via cmds.

	private static final String blackcmd = "ipset -A proxies %ip%";

	private static final Runtime rt = Runtime.getRuntime();

	public static void blacklistIP(String ip) {
		String cmd = blackcmd.replace("%ip%", ip);
		try {
			rt.exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isProxy(String ip) {
		try {
			URL u = new URL("http://proxycheck.io/v2/" + ip + "&vpn=1");
			return containsYes(u);
		} catch (MalformedURLException e) {
			return false;
		}

	}

	public static String readInputStreamAsString(InputStream in) throws IOException {
		try (BufferedInputStream bis = new BufferedInputStream(in); ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
			int result = bis.read();
			while (result != -1) {
				byte b = (byte) result;
				buf.write(b);
				result = bis.read();
			}
			return buf.toString();
		}
	}

	public static boolean containsYes(URL u) {
		try (InputStream conn = u.openStream()) {
			return readInputStreamAsString(conn).contains("\"yes\"");
		} catch (IOException e) {
			return false;
		}
	}

}
