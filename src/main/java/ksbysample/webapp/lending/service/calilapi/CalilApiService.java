package ksbysample.webapp.lending.service.calilapi;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Joiner;
import ksbysample.webapp.lending.service.calilapi.response.Book;
import ksbysample.webapp.lending.service.calilapi.response.CheckApiResponse;
import ksbysample.webapp.lending.service.calilapi.response.Libraries;
import ksbysample.webapp.lending.service.calilapi.response.LibrariesForJackson2Xml;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@PropertySource("classpath:calilapi.properties")
public class CalilApiService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private int CONNECT_TIMEOUT = 5000;
    private int READ_TIMEOUT = 5000;

    private final int RETRY_MAX_CNT = 5;
    private final long RETRY_SLEEP_MILLS = 3000;
    
    private final String URL_CALILAPI_LIBRALY = "http://api.calil.jp/library?appkey={appkey}&pref={pref}";
    private final String URL_CALILAPI_CHECK = "http://api.calil.jp/check?appkey={appkey}&systemid={systemid}&isbn={isbn}&format=xml";
    private final String URL_CALILAPI_CHECK_FOR_RETRY = "http://api.calil.jp/check?session={session}&format=xml";

    @Value("${calil.apikey}")
    private String calilApiKey;

    public Libraries getLibraryList(String pref) throws Exception {
        // 図書館データベースAPIを呼び出して XMLレスポンスを受信する
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        ResponseEntity<String> response
                = restTemplate.getForEntity(URL_CALILAPI_LIBRALY, String.class, this.calilApiKey, pref);
        
        // 受信した XMLレスポンスを Javaオブジェクトに変換する
        Serializer serializer = new Persister();
        Libraries libraries = serializer.read(Libraries.class, response.getBody());
        
        return libraries;
    }

    public LibrariesForJackson2Xml getLibraryListByJackson2Xml(String pref) throws Exception {
        // 図書館データベースAPIを呼び出して XMLレスポンスを受信する
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        restTemplate.setMessageConverters(getMessageConvertersforJackson2XML());
        ResponseEntity<LibrariesForJackson2Xml> response
                = restTemplate.getForEntity(URL_CALILAPI_LIBRALY, LibrariesForJackson2Xml.class, this.calilApiKey, pref);
        return response.getBody();
    }

    public  List<Book> check(String systemid, List<String> isbnList) {
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        restTemplate.setMessageConverters(getMessageConvertersforJackson2XML());

        Map<String, String> vars = new HashMap<>();
        vars.put("appkey", this.calilApiKey);
        vars.put("systemid", systemid);
        vars.put("isbn", Joiner.on(",").join(isbnList));

        ResponseEntity<CheckApiResponse> response = null;
        String url = URL_CALILAPI_CHECK;
        for (int retry = 0; retry < RETRY_MAX_CNT; retry++) {
            // 蔵書検索APIを呼び出して蔵書の有無と貸出状況を取得する
            response = restTemplate.getForEntity(url, CheckApiResponse.class, vars);
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
    
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        // 接続タイムアウト、受信タイムアウトを 5秒に設定する
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT);
        factory.setReadTimeout(READ_TIMEOUT);
        return factory;
    }

    private List<HttpMessageConverter<?>> getMessageConvertersforJackson2XML() {
        // build.gralde に compile("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:...") を記述して jackson-dataformat-xml
        // が使用できるようになっていない場合にはエラーにする
        assert(ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", RestTemplate.class.getClassLoader()));

        MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter
                = new MappingJackson2XmlHttpMessageConverter();
        // findAndRegisterModules メソッドを呼び出して jackson-dataformat-xml が機能するようにする
        mappingJackson2XmlHttpMessageConverter.setObjectMapper(new XmlMapper().findAndRegisterModules());

        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_XML);
        mediaTypes.add(MediaType.TEXT_XML);
        mappingJackson2XmlHttpMessageConverter.setSupportedMediaTypes(mediaTypes);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(mappingJackson2XmlHttpMessageConverter);
        return messageConverters;
    }
    
}
