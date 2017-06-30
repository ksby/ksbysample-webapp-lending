package ksbysample.webapp.lending.service.openweathermapapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ???
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class ForecastData {

    private BigDecimal dt;

    private Map<String, String> main;

    private List<Map<String, String>> weather;

    private Map<String, String> clouds;

    private Map<String, String> wind;

    private Map<String, String> rain;

    @JsonProperty("dt_txt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dtTxt;

}
