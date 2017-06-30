package ksbysample.webapp.lending.service.openweathermapapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

/**
 * ???
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CityData {

    private String id;

    private String name;

    // こんなレスポンスであればクラスを作成せずに Map<String, String> で受け取ることも可能
    // "coord": {
    //     "lon": 139.691711,
    //     "lat": 35.689499
    // },
    private Map<String, String> coord;

    private String country;

    private int population;

    private Map<String, String> sys;

}
