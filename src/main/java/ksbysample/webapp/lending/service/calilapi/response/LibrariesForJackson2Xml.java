package ksbysample.webapp.lending.service.calilapi.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Libraries")
@Data
public class LibrariesForJackson2Xml {

    private List<LibraryForJackson2Xml> libraryList = new ArrayList<>();

    /**
     * @param library ???
     */
    @XmlElement(name = "Library")
    public void addLibraryList(LibraryForJackson2Xml library) {
        libraryList.add(library);
    }

}
