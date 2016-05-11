package ksbysample.webapp.lending.helper.download;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface DataDownloadHelper {

    public void setFileNameToResponse(HttpServletResponse response);

    public void writeDataToResponse(HttpServletResponse response) throws IOException;

}
