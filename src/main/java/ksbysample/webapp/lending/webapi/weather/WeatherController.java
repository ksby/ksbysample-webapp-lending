package ksbysample.webapp.lending.webapi.weather;

import ksbysample.webapp.lending.service.openweathermapapi.FiveDayThreeHourForecastData;
import ksbysample.webapp.lending.service.openweathermapapi.OpenWeatherMapApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webapi/weather")
public class WeatherController {

    @Autowired
    private OpenWeatherMapApiService openWeatherMapApiService;

    /**
     * @param cityname ???
     * @return ???
     */
    @RequestMapping("/getFiveDayThreeHourForecast")
    public FiveDayThreeHourForecastResponse getFiveDayThreeHourForecast(String cityname) {
        FiveDayThreeHourForecastData fiveDayThreeHourForecastData
                = openWeatherMapApiService.getFiveDayThreeHourForecast(cityname);
        FiveDayThreeHourForecastResponse fiveDayThreeHourForecastResponse
                = new FiveDayThreeHourForecastResponse(fiveDayThreeHourForecastData);
        return fiveDayThreeHourForecastResponse;
    }

}
