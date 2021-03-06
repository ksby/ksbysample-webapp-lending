package ksbysample.webapp.lending.webapi.library;

import ksbysample.webapp.lending.service.calilapi.CalilApiService;
import ksbysample.webapp.lending.service.calilapi.response.Libraries;
import ksbysample.webapp.lending.service.calilapi.response.Library;
import ksbysample.webapp.lending.webapi.common.CommonWebApiResponse;
import org.simpleframework.xml.core.ValueRequiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * ???
 */
@RestController
@RequestMapping("/webapi/library")
public class LibraryController {

    private final Logger logger = LoggerFactory.getLogger(LibraryController.class);

    private final CalilApiService calilApiService;

    /**
     * ???
     *
     * @param calilApiService ???
     */
    public LibraryController(CalilApiService calilApiService) {
        this.calilApiService = calilApiService;
    }

    /**
     * ???
     *
     * @param pref ???
     * @return ???
     */
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    @GetMapping(value = "/getLibraryList")
    public CommonWebApiResponse<List<Library>> getLibraryList(String pref) {
        CommonWebApiResponse<List<Library>> response = new CommonWebApiResponse<>();
        response.setContent(Collections.emptyList());

        try {
            Libraries libraries = calilApiService.getLibraryList(pref);
            response.setContent(libraries.getLibraryList());
        } catch (ValueRequiredException e) {
            response.setErrcode(-2);
            response.setErrmsg("都道府県名が正しくありません。");
        } catch (Exception e) {
            logger.error("システムエラーが発生しました。", e);
            response.setErrcode(-1);
            response.setErrmsg("システムエラーが発生しました。");
        }

        return response;
    }

}
