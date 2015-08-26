package ksbysample.webapp.lending.webapi.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import ksbysample.webapp.lending.service.openweathermapapi.ForecastData;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ForecastResponse {

    private BigDecimal dt;

    private List<Map<String, String>> weather;

    private Map<String, String> wind;

    @XmlElement(name = "dt_txt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dtTxt;
    
    ForecastResponse(ForecastData forecastData) {
        BeanUtils.copyProperties(forecastData, this);
    }
    
}
