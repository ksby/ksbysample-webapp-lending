package ksbysample.webapp.lending.view;

import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import ksbysample.webapp.lending.helper.download.booklistcsv.BookListCsvData;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component(value = "BookListCsvView")
public class BookListCsvView extends AbstractView {

    private static final String[] CSV_HEADER = new String[]{"ISBN", "書名", "申請理由", "承認／却下", "却下理由"};
    private static final String CSV_FILE_NAME_FORMAT = "booklist-%s.csv";

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long lendingAppId = (Long) model.get("lendingAppId");
        List<BookListCsvData> bookListCsvDataList = (List<BookListCsvData>) model.get("bookListCsvDataList");

        response.setContentType("application/octet-stream; charset=Windows-31J;");
        response.setHeader("Content-Disposition"
                , String.format("attachment; filename=\"%s\"", String.format(CSV_FILE_NAME_FORMAT, lendingAppId)));

        CsvWriterSettings settings = new CsvWriterSettings();
        settings.setHeaders(CSV_HEADER);
        BeanWriterProcessor<BookListCsvData> writerProcessor = new BeanWriterProcessor<>(BookListCsvData.class);
        settings.setRowWriterProcessor(writerProcessor);

        CsvWriter writer = new CsvWriter(response.getWriter(), settings);
        writer.writeHeaders();
        writer.processRecordsAndClose(bookListCsvDataList);
    }

}
