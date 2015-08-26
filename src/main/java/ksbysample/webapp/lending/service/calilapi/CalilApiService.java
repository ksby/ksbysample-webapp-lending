package ksbysample.webapp.lending.service.calilapi;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
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
import java.util.List;

@Service
@PropertySource("classpath:calilapi.properties")
public class CalilApiService {

    private int CONNECT_TIMEOUT = 5000;
    private int READ_TIMEOUT = 5000;
    
    private final String URL_CALILAPI_LIBRALY = "http://api.calil.jp/library?appkey={appkey}&pref={pref}";

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
