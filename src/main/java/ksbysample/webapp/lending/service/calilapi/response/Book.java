package ksbysample.webapp.lending.service.calilapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@AllArgsConstructor
public class Book {

    @XmlAttribute(name = "isbn")
    private String isbn;

    @XmlAttribute(name = "calilurl")
    private String calilurl;

    @XmlElement(name = "system")
    private SystemData system;

    public String getFirstLibkeyValue() {
        Optional<Libkey> libkey = getSystem().getLibkeyList().stream().findFirst();
        return libkey.map(Libkey::getValue).orElse(null);
    }
    
}
