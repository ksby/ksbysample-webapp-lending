package ksbysample.webapp.lending.service.calilapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * ???
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SystemData {

    @XmlAttribute(name = "systemid")
    private String systemid;

    @XmlElement(name = "status")
    private String status;

    @XmlElement(name = "reserveurl")
    private String reserveurl;

    @XmlElement(name = "libkeys")
    private List<Libkey> libkeyList = new ArrayList<>();

}
