package ksbysample.webapp.lending.service.calilapi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * ???
 */
@Data
@Root(name = "Library", strict = false)
public class Library {

    @Element
    private String systemid;

    @Element
    private String systemname;

    @Element(name = "formal")
    @JsonProperty("formal")
    private String formalName;

    @Element
    private String address;

}
