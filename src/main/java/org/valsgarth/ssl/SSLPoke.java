package org.valsgarth.ssl;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Establish an SSL connection to a host and port, write a byte and print the response.
 * See <a href="https://confluence.atlassian.com/display/JIRA/Connecting+to+SSL+services">Connecting to SSL services</a> on Atlassian website.
 */
@SuppressWarnings({"java:S2629"})
public class SSLPoke {
	private static final Logger LOG;
	static {
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT\t%4$-7s\t%5$s%n");
		LOG = Logger.getLogger(SSLPoke.class.getName());
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			LOG.info("Usage: %s <host> <port>".formatted(SSLPoke.class.getName()));
			System.exit(1);
		}

		SSLPoke sslPoke = new SSLPoke();
		if(sslPoke.poke(args[0], Integer.parseInt(args[1])) != 0) {
			System.exit(1);
		}
	}

	int poke(String host, int port) {
		int rc = 0;
		try {
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			try (SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(host, port);
				 InputStream in = sslsocket.getInputStream();
				 OutputStream out = sslsocket.getOutputStream()
			) {
				// Write a test byte to get a reaction :)
				out.write(1);

				LOG.fine("%s".formatted(new String(in.readAllBytes())));
				LOG.info("Successfully connected");
			}
		} catch (Exception exception) {
			LOG.log(Level.SEVERE, "Exception received while connecting", exception);
			rc = 1;
		}
		return rc;
	}
}
