package ksbysample.webapp.lending


import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.crypto.NoSuchPaddingException

import static org.assertj.core.api.Assertions.assertThatThrownBy

class SampleHelperTest {

    @SpringBootTest
    static class 正常処理のテスト extends Specification {

        @Autowired
        private SampleHelper sampleHelper

        def "SampleHelper.encrypt(#str) --> #result"() {
            expect:
            sampleHelper.encrypt(str) == result

            where:
            str        || result
            "test"     || "bjMKKlhqu/c="
            "xxxxxxxx" || "ERkXmOeArBKwGbCh+M6aHw=="
        }

    }

    @SpringBootTest
    static class 異常処理のテスト {

        @Autowired
        private SampleHelper sampleHelper

        @Test
        void "SampleHelper_encryptを呼ぶとRuntimeExceptionをthrowする"() {
            // setup:
            Mockito.mockStatic(BrowfishUtils)
            Mockito.when(BrowfishUtils.encrypt(Mockito.any()))
                    .thenThrow(new NoSuchPaddingException())

            // expect:
            assertThatThrownBy(() -> {
                sampleHelper.encrypt("test")
            }).isInstanceOf(RuntimeException)
        }

    }

}
