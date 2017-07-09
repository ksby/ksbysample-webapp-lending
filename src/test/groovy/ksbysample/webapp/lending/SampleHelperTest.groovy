package ksbysample.webapp.lending

import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import spock.lang.Specification
import spock.lang.Unroll

import javax.crypto.NoSuchPaddingException

import static org.assertj.core.api.Assertions.assertThatExceptionOfType

@RunWith(Enclosed)
class SampleHelperTest {

    @SpringBootTest
    static class 正常処理のテスト extends Specification {

        @Autowired
        private SampleHelper sampleHelper

        @Unroll
        def "SampleHelper.encrypt(#str) --> #result"() {
            expect:
            sampleHelper.encrypt(str) == result

            where:
            str        || result
            "test"     || "bjMKKlhqu/c="
            "xxxxxxxx" || "ERkXmOeArBKwGbCh+M6aHw=="
        }

    }

    @RunWith(PowerMockRunner)
    @PowerMockRunnerDelegate(SpringRunner)
    @SpringBootTest
    @PrepareForTest(BrowfishUtils)
    @PowerMockIgnore("javax.management.*")
    static class 異常処理のテスト {

        @Autowired
        private SampleHelper sampleHelper

        @Test
        void "SampleHelper_encryptを呼ぶとRuntimeExceptionをthrowする"() {
            // setup:
            PowerMockito.mockStatic(BrowfishUtils)
            PowerMockito.when(BrowfishUtils.encrypt(Mockito.any()))
                    .thenThrow(new NoSuchPaddingException())

            // expect:
            assertThatExceptionOfType(RuntimeException).isThrownBy({
                sampleHelper.encrypt("test")
            })
        }

    }

}
