package ksbysample.webapp.lending.helper.download.booklistcsv;

import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import ksbysample.webapp.lending.helper.download.DataDownloadHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * ???
 */
public class BookListCsvDownloadHelper implements DataDownloadHelper {

    private static final String[] CSV_HEADER = {"ISBN", "書名", "申請理由", "承認／却下", "却下理由"};
    private static final String CSV_FILE_NAME_FORMAT = "booklist-%s.csv";

    private final Long lendingAppId;

    private final List<BookListCsvData> bookListCsvDataList;

    /**
     * @param lendingAppId        ???
     * @param bookListCsvDataList ???
     */
    public BookListCsvDownloadHelper(Long lendingAppId, List<BookListCsvData> bookListCsvDataList) {
        this.lendingAppId = lendingAppId;
        this.bookListCsvDataList = bookListCsvDataList;
    }

    /**
     * @return ???
     */
    public String getCsvFileName() {
        return String.format(CSV_FILE_NAME_FORMAT, this.lendingAppId);
    }

    @Override
    public void setFileNameToResponse(HttpServletResponse response) {
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", getCsvFileName()));
    }

    @Override
    public void writeDataToResponse(HttpServletResponse response) throws IOException {
        CsvWriterSettings settings = new CsvWriterSettings();
        settings.setHeaders(CSV_HEADER);
        BeanWriterProcessor<BookListCsvData> writerProcessor = new BeanWriterProcessor<>(BookListCsvData.class);
        settings.setRowWriterProcessor(writerProcessor);

        response.setCharacterEncoding("UTF-8");
        CsvWriter writer = new CsvWriter(response.getWriter(), settings);
        writer.writeHeaders();
        writer.processRecordsAndClose(bookListCsvDataList);
    }

}
