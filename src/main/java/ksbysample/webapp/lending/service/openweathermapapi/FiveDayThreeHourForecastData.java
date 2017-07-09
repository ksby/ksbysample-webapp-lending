package ksbysample.webapp.lending.service.openweathermapapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * ???
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class FiveDayThreeHourForecastData {

    private CityData city;

    private String cod;

    private BigDecimal message;

    private int cnt;

    private List<ForecastData> list;

}
