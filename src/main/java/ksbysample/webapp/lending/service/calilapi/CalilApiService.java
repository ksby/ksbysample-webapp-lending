package ksbysample.webapp.lending.service.calilapi;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        // 接続タイムアウト、受信タイムアウトを 5秒に設定する
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT);
        factory.setReadTimeout(READ_TIMEOUT);
        return factory;
    }
    
}
