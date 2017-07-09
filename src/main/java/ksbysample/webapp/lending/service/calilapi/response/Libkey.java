package ksbysample.webapp.lending.service.calilapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * ???
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Libkey {

    @XmlAttribute(name = "name")
    private String name;

    @XmlValue
    private String value;

}
