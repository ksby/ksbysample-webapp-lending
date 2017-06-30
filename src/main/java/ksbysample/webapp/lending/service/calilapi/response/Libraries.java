package ksbysample.webapp.lending.service.calilapi.response;

import lombok.Data;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * ???
 */
@Data
@Root(name = "Libraries")
public class Libraries {

    @ElementList(inline = true)
    private List<Library> libraryList;

}
