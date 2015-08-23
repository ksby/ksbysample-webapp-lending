package ksbysample.webapp.lending.service.openweathermapapi;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenWeatherMapApiService {

    private int CONNECT_TIMEOUT = 5000;
    private int READ_TIMEOUT = 5000;

    private final String URL_WEATHERAPI_5DAY3HOURFORECAST = "http://api.openweathermap.org/data/2.5/forecast?q={cityname}";

    public FiveDayThreeHourForecastData getFiveDayThreeHourForecast(String cityname) {
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        ResponseEntity<FiveDayThreeHourForecastData> response
                = restTemplate.getForEntity(URL_WEATHERAPI_5DAY3HOURFORECAST, FiveDayThreeHourForecastData.class, cityname);
        return response.getBody();
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        // 接続タイムアウト、受信タイムアウトを 5秒に設定する
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT);
        factory.setReadTimeout(READ_TIMEOUT);
        return factory;
    }

}
