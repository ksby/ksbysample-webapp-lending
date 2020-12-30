package ksbysample.webapp.lending.helper.download;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ???
 */
public interface DataDownloadHelper {

    /**
     * ???
     *
     * @param response ???
     */
    void setFileNameToResponse(HttpServletResponse response);

    /**
     * ???
     *
     * @param response ???
     * @throws IOException ???
     */
    void writeDataToResponse(HttpServletResponse response) throws IOException;

}
