package ksbysample.webapp.lending.webapi.weather;

import ksbysample.webapp.lending.service.openweathermapapi.FiveDayThreeHourForecastData;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ???
 */
@XmlRootElement(name = "FiveDayThreeHourForecastData")
@Data
public class FiveDayThreeHourForecastResponse {

    private CityResponse city;

    private int cnt;

    private List<ForecastResponse> list;

    FiveDayThreeHourForecastResponse(FiveDayThreeHourForecastData fiveDayThreeHourForecastData) {
        BeanUtils.copyProperties(fiveDayThreeHourForecastData, this, "city", "list");
        city = new CityResponse(fiveDayThreeHourForecastData.getCity());
        list = fiveDayThreeHourForecastData.getList().stream()
                .map(ForecastResponse::new)
                .collect(Collectors.toList());
    }

}
