package org.valsgarth.ssl;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.InputStream;
import java.io.OutputStream;

public class SSLPoke {

	@SuppressWarnings({"java:S106"})
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: " + SSLPoke.class.getName() + " <host> <port>");
			System.exit(1);
		}

		try {
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			InputStream in;
			OutputStream out;
			try (SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(args[0], Integer.parseInt(args[1]))) {
				in = sslsocket.getInputStream();
				out = sslsocket.getOutputStream();
				// Write a test byte to get a reaction :)
				out.write(1);

				while (in.available() > 0) {
					System.out.print(in.read());
				}
				System.out.println("Successfully connected");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
