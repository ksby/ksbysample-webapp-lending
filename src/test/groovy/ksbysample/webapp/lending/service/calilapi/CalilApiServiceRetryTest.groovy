package ksbysample.webapp.lending.service.calilapi

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequest
import org.springframework.http.client.ClientHttpResponse
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.ResponseCreator
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import spock.lang.Retry
import spock.lang.Specification

import static ksbysample.webapp.lending.service.calilapi.CalilApiServiceRetryTest.TimeoutResponseCreator.withTimeout
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

@Slf4j
@SpringBootTest
@DirtiesContext
class CalilApiServiceRetryTest extends Specification {

    /**
     * タイムアウト用 ResponseCreator
     * Spring Framework 5.1 になると
     * responseCreator = MockRestResponseCreators.withException(SocketTimeoutException).createResponse(null)
     * という書き方が出来るらしいので、おそらくこのようなクラスを定義する必要はない
     */
    static class TimeoutResponseCreator implements ResponseCreator {

        @Override
        ClientHttpResponse createResponse(ClientHttpRequest request) throws IOException {
            if (true) {
                throw new SocketTimeoutException("Testing timeout exception")
            }
            return null
        }

        static TimeoutResponseCreator withTimeout() {
            return new TimeoutResponseCreator()
        }

    }

    @Autowired
    RestTemplate restTemplateForCalilApi

    def responseJson = """
                        {
                            "key": "sample",
                            "value": 1
                        }
                    """.stripIndent()

    def retryCount

    def setup() {
        retryCount = 0
    }

    def "200OKでレスポンスが返る場合のテスト"() {
        given:
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplateForCalilApi).build()
        mockServer.expect(requestTo(CalilApiService.URL_CALILAPI_ROOT + "/sample/"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON_UTF8))

        when:
        def result = restTemplateForCalilApi.getForObject("/sample/", String)

        then:
        result == responseJson
    }

    def "タイムアウトする場合のテスト"() {
        given:
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplateForCalilApi).build()
        mockServer.expect(requestTo(CalilApiService.URL_CALILAPI_ROOT + "/sample/"))
                .andExpect(method(HttpMethod.GET))
                .andRespond { request -> withTimeout().createResponse(request) }

        when:
        def result = restTemplateForCalilApi.getForObject("/sample/", String)

        then:
        ResourceAccessException e = thrown()
        e.cause instanceof SocketTimeoutException
    }

    @Retry
    def "リトライして４回目で200OKのレスポンスが返る場合のテスト"() {
        given:
        retryCount++
        ResponseCreator responseCreator
        if (retryCount == 4) {
            responseCreator = withSuccess(responseJson, MediaType.APPLICATION_JSON_UTF8)
        } else {
            responseCreator = { request -> withTimeout().createResponse(request) }
        }

        // MockRestServiceServer.bindTo(...).build() が呼ばれると RestTemplate の requestFactory が変更されるらしい
        // つまり restTemplateForCalilApi のタイムアウトの設定も変更されるので、restTemplateForCalilApi　のタイムアウト
        // のテストにはならないようだ
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplateForCalilApi).build()
        mockServer.expect(requestTo(CalilApiService.URL_CALILAPI_ROOT + "/sample/"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(responseCreator)

        when:
        log.warn("calling...")
        def result = restTemplateForCalilApi.getForObject("/sample/", String)
        log.error("finished!!")

        then:
        notThrown ResourceAccessException
        result == responseJson
    }

}
