package ksbysample.webapp.lending.service.calilapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ksbysample.webapp.lending.service.calilapi.response.Book;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "result")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class CheckApiResponse {

    @XmlElement(name = "session")
    private String session;
    
    @XmlElement(name = "continue")
    private Integer continueValue;

    @XmlElement(name = "books")
    private List<Book> bookList  = new ArrayList<>();
    
}
