package ksbysample.webapp.lending.service.calilapi;

import com.google.common.base.Joiner;
import ksbysample.webapp.lending.service.calilapi.response.Book;
import ksbysample.webapp.lending.service.calilapi.response.CheckApiResponse;
import ksbysample.webapp.lending.service.calilapi.response.Libraries;
import ksbysample.webapp.lending.service.calilapi.response.LibrariesForJackson2Xml;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@PropertySource("classpath:calilapi.properties")
public class CalilApiService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int RETRY_MAX_CNT = 5;
    private static final long RETRY_SLEEP_MILLS = 3000;

    private static final String URL_CALILAPI_ROOT = "http://api.calil.jp";
    private static final String URL_CALILAPI_LIBRALY
            = URL_CALILAPI_ROOT + "/library?appkey={appkey}&pref={pref}";
    private static final String URL_CALILAPI_CHECK
            = URL_CALILAPI_ROOT + "/check?appkey={appkey}&systemid={systemid}&isbn={isbn}&format=xml";
    private static final String URL_CALILAPI_CHECK_FOR_RETRY
            = URL_CALILAPI_ROOT + "/check?session={session}&format=xml";

    @Value("${calil.apikey}")
    private String calilApiKey;

    private final RestTemplate restTemplateForCalilApi;

    private final RestTemplate restTemplateForCalilApiByXml;

    private final RetryTemplate simpleRetryTemplate;

    /**
     * @param restTemplateForCalilApi      ???
     * @param restTemplateForCalilApiByXml ???
     * @param simpleRetryTemplate          ???
     */
    public CalilApiService(@Qualifier("restTemplateForCalilApi") RestTemplate restTemplateForCalilApi
            , @Qualifier("restTemplateForCalilApiByXml") RestTemplate restTemplateForCalilApiByXml
            , @Qualifier("simpleRetryTemplate") RetryTemplate simpleRetryTemplate) {
        this.restTemplateForCalilApi = restTemplateForCalilApi;
        this.restTemplateForCalilApiByXml = restTemplateForCalilApiByXml;
        this.simpleRetryTemplate = simpleRetryTemplate;
    }

    /**
     * @param pref ???
     * @return ???
     * @throws Exception
     */
    public Libraries getLibraryList(String pref) throws Exception {
        // 図書館データベースAPIを呼び出して XMLレスポンスを受信する
        ResponseEntity<String> response = getForEntityWithRetry(this.restTemplateForCalilApi
                , URL_CALILAPI_LIBRALY, String.class, this.calilApiKey, pref);

        // 受信した XMLレスポンスを Javaオブジェクトに変換する
        Serializer serializer = new Persister();
        Libraries libraries = serializer.read(Libraries.class, response.getBody());

        return libraries;
    }

    /**
     * @param pref ???
     * @return ???
     * @throws Exception
     */
    public LibrariesForJackson2Xml getLibraryListByJackson2Xml(String pref) throws Exception {
        // 図書館データベースAPIを呼び出して XMLレスポンスを受信する
        ResponseEntity<LibrariesForJackson2Xml> response = getForEntityWithRetry(this.restTemplateForCalilApiByXml
                , URL_CALILAPI_LIBRALY, LibrariesForJackson2Xml.class, this.calilApiKey, pref);
        return response.getBody();
    }

    /**
     * @param systemid ???
     * @param isbnList ???
     * @return ???
     */
    public List<Book> check(String systemid, List<String> isbnList) {
        Map<String, String> vars = new HashMap<>();
        vars.put("appkey", this.calilApiKey);
        vars.put("systemid", systemid);
        vars.put("isbn", Joiner.on(",").join(isbnList));

        ResponseEntity<CheckApiResponse> response = null;
        String url = URL_CALILAPI_CHECK;
        for (int retry = 0; retry < RETRY_MAX_CNT; retry++) {
            // 蔵書検索APIを呼び出して蔵書の有無と貸出状況を取得する
            response = getForEntityWithRetry(this.restTemplateForCalilApiByXml, url, CheckApiResponse.class, vars);
            logger.info("カーリルの蔵書検索API を呼び出し、レスポンスを取得しました。{}", response.getBody().toString());
            if (response.getBody().getContinueValue() == 0) {
                break;
            }

            // continue の値が 0 でない場合には２秒以上待機した後、URLパラメータを session に変更して再度リクエストを送信する
            try {
                Thread.sleep(RETRY_SLEEP_MILLS);
            } catch (InterruptedException e) {
                logger.warn("カーリルの蔵書検索APIのsleep中にInterruptedExceptionが発生しましたが、処理は継続します。", e);
            }
            url = URL_CALILAPI_CHECK_FOR_RETRY;
            vars.clear();
            vars.put("session", response.getBody().getSession());
        }

        return response.getBody().getBookList();
    }

    private <T> ResponseEntity<T> getForEntityWithRetry(RestTemplate restTemplate, String url
            , Class<T> responseType, Object... uriVariables) {
        ResponseEntity<T> response = this.simpleRetryTemplate.execute(context -> {
            if (context.getRetryCount() > 0) {
                logger.info("★★★ リトライ回数 = " + context.getRetryCount());
            }
            ResponseEntity<T> innerResponse = restTemplate.getForEntity(url, responseType, uriVariables);
            return innerResponse;
        });

        return response;
    }

    private <T> ResponseEntity<T> getForEntityWithRetry(RestTemplate restTemplate, String url
            , Class<T> responseType, Map<String, ?> uriVariables) {
        ResponseEntity<T> response = this.simpleRetryTemplate.execute(context -> {
            if (context.getRetryCount() > 0) {
                logger.info("★★★ リトライ回数 = " + context.getRetryCount());
            }
            ResponseEntity<T> innerResponse = restTemplate.getForEntity(url, responseType, uriVariables);
            return innerResponse;
        });

        return response;
    }

    @Configuration
    public static class CalilApiConfig {

        private static int CONNECT_TIMEOUT = 5000;
        private static int READ_TIMEOUT = 5000;

        private final RestTemplateBuilder restTemplateBuilder;

        private final MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter;

        /**
         * コンストラクタ
         *
         * @param restTemplateBuilder                    restTemplateBuilder Bean
         * @param mappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter Bean
         */
        public CalilApiConfig(RestTemplateBuilder restTemplateBuilder
                , MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter) {
            this.restTemplateBuilder = restTemplateBuilder;
            this.mappingJackson2XmlHttpMessageConverter = mappingJackson2XmlHttpMessageConverter;
        }

        /**
         * カーリルの図書館API呼び出し用 RestTemplate
         * JSON フォーマットで結果を受信する
         *
         * @return RestTemplate オブジェクト
         */
        @Bean
        public RestTemplate restTemplateForCalilApi() {
            return this.restTemplateBuilder
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .setReadTimeout(READ_TIMEOUT)
                    .rootUri(URL_CALILAPI_ROOT)
                    .build();
        }

        /**
         * カーリルの図書館API呼び出し用 RestTemplate
         * XML フォーマットで結果を受信する
         *
         * @return RestTemplate オブジェクト
         */
        @Bean
        public RestTemplate restTemplateForCalilApiByXml() {
            return this.restTemplateBuilder
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .setReadTimeout(READ_TIMEOUT)
                    .rootUri(URL_CALILAPI_ROOT)
                    .messageConverters(this.mappingJackson2XmlHttpMessageConverter)
                    .build();
        }

    }

}
