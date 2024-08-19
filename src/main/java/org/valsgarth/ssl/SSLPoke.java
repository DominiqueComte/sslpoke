package org.valsgarth.ssl;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.OutputStream;

/**
 * Establish an SSL connection to a host and port, write a byte and print the response.
 * See <a href="https://confluence.atlassian.com/display/JIRA/Connecting+to+SSL+services">Connecting to SSL services</a> on Atlassian website.
 */
@SuppressWarnings({"java:S2629"})
public class SSLPoke {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.printf("Usage: %s <host> <port>%n", SSLPoke.class.getName());
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
				 //kept to be able to get some debug output, see below
				 // InputStream in = sslsocket.getInputStream();
				 OutputStream out = sslsocket.getOutputStream()
			) {
				// Write a test byte to get a reaction :)
				out.write(1);

				//some debug output, prints the actual server response
				// System.out.printf("%s%n".formatted(new String(in.readAllBytes())));
				System.out.println("Successfully connected");
			}
		} catch (Exception exception) {
			System.err.printf("Exception received while connecting: %s", exception);
			rc = 1;
		}
		return rc;
	}
}
