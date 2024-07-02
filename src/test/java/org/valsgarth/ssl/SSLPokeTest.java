package org.valsgarth.ssl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErr;
import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.assertj.core.api.Assertions.assertThat;

class SSLPokeTest {

    @ParameterizedTest
    @ValueSource(strings = {"google.com", "dh2048.badssl.com"})
    void givenUsuallyOkSite_whenInvokeMain_thenConnectionSuccessfully(String host) throws Exception {
        String text = tapSystemOut(() -> SSLPoke.main(new String[]{host, "443"}));

        assertThat(text.trim()).isEqualTo("Successfully connected");
    }

    @ParameterizedTest
    @ValueSource(strings = {"self-signed.badssl.com", "untrusted-root.badssl.com"})
    void givenBadCertSite_whenInvokeMain_thenOutputContainsException(String host) throws Exception {
        String text = null;
        try {
            text = tapSystemErr(() -> SSLPoke.main(new String[]{host, "443"}));
        } catch (Exception ignored) {
        }

        assertThat(text)
                .contains("javax.net.ssl.SSLHandshakeException")
                .contains("sun.security.provider.certpath.SunCertPathBuilderException");
    }

    @ParameterizedTest
    @ValueSource(strings = {"dh480.badssl.com"
                            , "dh512.badssl.com"
                            //,"dh1024.badssl.com"
    })
    void givenShortSignCertSite_whenInvokeMain_thenOutputContainsDHServerKeyExchangeException(String host) throws Exception {
        String text = null;
        try {
            text = tapSystemErr(() -> SSLPoke.main(new String[]{host, "443"}));
        } catch (Exception ignored) {
        }

        assertThat(text)
                .contains("javax.net.ssl.SSLHandshakeException")
                .contains("DH ServerKeyExchange does not comply to algorithm constraints");
    }
}
