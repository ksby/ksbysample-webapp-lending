package ksbysample.webapp.lending.webapi.weather;

import ksbysample.webapp.lending.service.openweathermapapi.CityData;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Map;

@Data
public class CityResponse {

    private String name;

    private Map<String, String> coord;

    private String country;
    
    CityResponse(CityData cityData) {
        BeanUtils.copyProperties(cityData, this);
    }
    
}
