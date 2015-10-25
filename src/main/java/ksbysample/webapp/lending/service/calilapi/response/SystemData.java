package ksbysample.webapp.lending.service.calilapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ksbysample.webapp.lending.service.calilapi.response.Libkey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
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
