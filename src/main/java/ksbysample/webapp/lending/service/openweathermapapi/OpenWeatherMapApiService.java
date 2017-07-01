package ksbysample.webapp.lending.service.openweathermapapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ???
 */
@Service
public class OpenWeatherMapApiService {

    private static int CONNECT_TIMEOUT = 5000;
    private static int READ_TIMEOUT = 5000;

    private static final String URL_WEATHERAPI_5DAY3HOURFORECAST = "http://api.openweathermap.org/data/2.5/forecast?q={cityname}";

    /**
     * @param cityname ???
     * @return ???
     */
    public FiveDayThreeHourForecastData getFiveDayThreeHourForecast(String cityname) {
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        ResponseEntity<FiveDayThreeHourForecastData> response
                = restTemplate.getForEntity(URL_WEATHERAPI_5DAY3HOURFORECAST
                , FiveDayThreeHourForecastData.class, cityname);
        return response.getBody();
    }

    /**
     * @param cityname ???
     * @param callback ???
     * @return ???
     * @throws IOException
     */
    public FiveDayThreeHourForecastData getFiveDayThreeHourForecastByJsonp(String cityname, String callback)
            throws IOException {
        assert (StringUtils.isNotBlank(callback));

        // callback パラメータを付加して WebAPI を呼び出し、レスポンスを String で受け取る
        // ※今回は RestTemplate に Map でパラメータを渡す
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        String url = URL_WEATHERAPI_5DAY3HOURFORECAST + "&callback={callback}";
        Map<String, String> vars = new HashMap<>();
        vars.put("cityname", cityname);
        vars.put("callback", callback);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, vars);

        // レスポンスの文字列から "callback関数名(" + ");" の部分を削除して JSON 文字列を抽出する
        String body = response.getBody();
        StringBuilder json = new StringBuilder(body.length() - callback.length() - 3);
        json.append(body.substring(callback.length() + 1, body.length() - 3));

        // JSON 文字列から Java オブジェクトへ変換する
        // ※自分で ObjectMapper オブジェクトを生成した時には findAndRegisterModules メソッドを呼び出さないと
        //   jackson-datatype-jsr310 による LocalDateTime 変換が行われない
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper.readValue(json.toString(), FiveDayThreeHourForecastData.class);
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        // 接続タイムアウト、受信タイムアウトを 5秒に設定する
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT);
        factory.setReadTimeout(READ_TIMEOUT);
        return factory;
    }

}
