import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrAndOut;
import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This tests only the poke method, not the main method.
 * Do not try to catch the System.exit() calls, with JDK21 it does not work anymore.
 *
 * @see <a href="https://openjdk.org/jeps/411">JEP-411</a> and related issues
 */
class SSLPokeTest {

    private SSLPoke instance;

    @BeforeEach
    void setup() {
        instance = new SSLPoke();
    }

    @ParameterizedTest
    @ValueSource(strings = {"google.com", "dh2048.badssl.com"})
    void givenUsuallyOkSite_whenInvokeMain_thenConnectionSuccessfully(String host) throws Exception {
        String text = tapSystemOut(() -> SSLPoke.main(new String[]{host, "443"}));

        assertThat(text.trim()).isEqualTo("Successfully connected");
    }

    @ParameterizedTest
    @ValueSource(strings = {"self-signed.badssl.com", "untrusted-root.badssl.com"})
    void givenBadCertSite_whenInvokeMain_thenOutputContainsException(String host) throws Exception {
        final String[] text = {null};
        final int[] rc = {0};
        instance = new SSLPoke();
        text[0] = tapSystemErrAndOut(() -> rc[0] = instance.poke(host, 443));
        assertThat(rc[0]).isEqualTo(1);
        assertThat(text[0]).contains("javax.net.ssl.SSLHandshakeException");
    }

    @ParameterizedTest
    @ValueSource(strings = {"dh480.badssl.com"
                            , "dh512.badssl.com"
                            //,"dh1024.badssl.com"
    })
    void givenShortSignCertSite_whenInvokeMain_thenOutputContainsDHServerKeyExchangeException(String host) throws Exception {
        final String[] text = {null};
        final int[] rc = {0};
        instance = new SSLPoke();
        text[0] = tapSystemErrAndOut(() -> rc[0] = instance.poke(host, 443));
        assertThat(rc[0]).isEqualTo(1);
        assertThat(text[0])
                .contains("javax.net.ssl.SSLHandshakeException")
                .contains("DH ServerKeyExchange does not comply to algorithm constraints");
    }
}
