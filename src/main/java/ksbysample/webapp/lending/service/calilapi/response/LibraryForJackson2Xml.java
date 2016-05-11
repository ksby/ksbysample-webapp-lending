package ksbysample.webapp.lending.service.calilapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class LibraryForJackson2Xml {

    private String systemid;

    private String systemname;

    @XmlElement(name = "formal")
    private String formalName;

    private String address;
    
}
