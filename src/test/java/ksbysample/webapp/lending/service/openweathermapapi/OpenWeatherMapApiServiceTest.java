package ksbysample.webapp.lending.service.openweathermapapi;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore("アクセスキーを要求されるようになったのでテストを実行対象外にする")
@RunWith(SpringRunner.class)
@SpringBootTest
public class OpenWeatherMapApiServiceTest {

    @Autowired
    private OpenWeatherMapApiService openWeatherMapApiService;

    @Test
    public void testGetFiveDayThreeHourForecast() throws Exception {
        FiveDayThreeHourForecastData fiveDayThreeHourForecastData
                = openWeatherMapApiService.getFiveDayThreeHourForecast("Tokyo");
        assertThat(fiveDayThreeHourForecastData.getCity().getName()).isEqualTo("Tokyo");
        assertThat(fiveDayThreeHourForecastData.getCity().getCountry()).isEqualTo("JP");
        assertThat(fiveDayThreeHourForecastData.getList().size()).isGreaterThan(0);
//        System.out.println(fiveDayThreeHourForecastData);
    }

    @Test
    public void testGetFiveDayThreeHourForecastByJSONP() throws Exception {
        FiveDayThreeHourForecastData fiveDayThreeHourForecastData
                = openWeatherMapApiService.getFiveDayThreeHourForecastByJsonp("Tokyo", "func");
        assertThat(fiveDayThreeHourForecastData.getCity().getName()).isEqualTo("Tokyo");
        assertThat(fiveDayThreeHourForecastData.getCity().getCountry()).isEqualTo("JP");
        assertThat(fiveDayThreeHourForecastData.getList().size()).isGreaterThan(0);
//        System.out.println(fiveDayThreeHourForecastData);
    }

}
