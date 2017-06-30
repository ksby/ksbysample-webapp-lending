package ksbysample.webapp.lending.helper.download;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ???
 */
public interface DataDownloadHelper {

    /**
     * @param response ???
     */
    public void setFileNameToResponse(HttpServletResponse response);

    /**
     * @param response ???
     * @throws IOException
     */
    public void writeDataToResponse(HttpServletResponse response) throws IOException;

}
