# SSLPoke

Small program for testing the validity of certificates and trust stores.

## Building
Either build "normally":
```shell
./mvnw package
```
or do a native build (that needs a GraalVM JDK)
```shell
./mvnw package -Pnative
```

## Usage

### with the JAR file
```shell
java -Djavax.net.ssl.trustStore=[client-truststore-path] org.valsgarth.ssl.SSLPoke [target-hostname] [port]
```

### with the native program
```shell
sslpoke [target-hostname] [port]
```

### output

If the server certificate is trusted by the client-truststore it will print,

```console
Successfully connected
```

Otherwise, it will print an exception like this.
```console
javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed:
sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
```

### extra options
The same options can be passed to the Java program or the native program.

For example:
* the path to a trust store
  * `-Djavax.net.ssl.trustStore=[client-truststore-path]`
* enabling some debugging output
  * `-Djavax.net.debug=ssl:handshake`
* proxy settings
etc...


## source

This class or similar ones are a bit everywhere, I couldn't find which one was the first one.
Possibly, from [this file](https://confluence.atlassian.com/download/attachments/117455/SSLPoke.java) on the Atlassian website.
