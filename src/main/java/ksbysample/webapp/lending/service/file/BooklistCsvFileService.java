package ksbysample.webapp.lending.service.file;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class BooklistCsvFileService {

    private static final Pattern ISBN_FORMAT_PATTERN = Pattern.compile("^[0-9\\-]+$");

    private final MessagesPropertiesHelper mph;

    /**
     * @param mph ???
     */
    public BooklistCsvFileService(MessagesPropertiesHelper mph) {
        this.mph = mph;
    }

    /**
     * @param multipartFile ???
     * @param errors        ???
     */
    public void validateUploadFile(MultipartFile multipartFile, Errors errors) {
        try (
                InputStream is = multipartFile.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "Windows-31J");
        ) {
            CsvParserSettings csvParserSettings = createCsvParserSettings();
            CsvParser parser = new CsvParser(csvParserSettings);
            List<String[]> allRows = parser.parseAll(isr);

            String isbn;
            Multiset<String> isbnList = HashMultiset.create();
            int line = 1;
            for (String[] csvdata : allRows) {
                line++;

                // 項目数が 2 でない
                if (csvdata.length != 2) {
                    errors.reject("UploadBooklistForm.fileupload.lengtherr"
                            , new Object[]{line, csvdata.length}, null);
                    continue;
                }

                // ISBN のデータに数字、ハイフン以外の文字が使用されている
                if (!ISBN_FORMAT_PATTERN.matcher(csvdata[0]).matches()) {
                    errors.reject("UploadBooklistForm.fileupload.isbn.patternerr"
                            , new Object[]{line, csvdata[0]}, null);
                }

                // ISBN のデータの文字数が 17 文字以内でない
                if (csvdata[0].length() > 17) {
                    errors.reject("UploadBooklistForm.fileupload.isbn.lengtherr"
                            , new Object[]{line, csvdata[0]}, null);
                }

                // ISBN のデータからハイフンを取り除いた文字数が 10 or 13 文字でない
                isbn = csvdata[0].replaceAll("-", "");
                if ((isbn.length() != 10) && (isbn.length() != 13)) {
                    errors.reject("UploadBooklistForm.fileupload.isbn.numlengtherr"
                            , new Object[]{line, isbn}, null);
                }

                // 書名のデータの文字数が 128 文字以内でない
                if (csvdata[1].length() > 128) {
                    errors.reject("UploadBooklistForm.fileupload.bookname.lengtherr"
                            , new Object[]{line, csvdata[1]}, null);
                }

                // ISBN を重複チェック用リストに追加する
                isbnList.add(csvdata[0]);
            }

            // 重複している ISBN があればエラーメッセージをセットする
            isbnList.stream()
                    .filter(str -> isbnList.count(str) >= 2)
                    .distinct()
                    .forEach(str -> errors.reject("UploadBooklistForm.fileupload.isbn.duplicateerr"
                            , new Object[]{str, isbnList.count(str)}, null));
        } catch (IOException e) {
            throw new WebApplicationRuntimeException(
                    mph.getMessage("UploadBooklistForm.fileupload.openerr", null));
        }
    }

    /**
     * @param multipartFile ???
     * @return ???
     */
    public List<BooklistCsvRecord> convertFileToList(MultipartFile multipartFile) {
        List<BooklistCsvRecord> booklistCsvRecordList;

        try (
                InputStream is = multipartFile.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "Windows-31J");
        ) {
            CsvParserSettings csvParserSettings = createCsvParserSettings();

            // JavaBean に変換するための Processor クラスを生成して設定する
            BeanListProcessor<BooklistCsvRecord> rowProcessor = new BeanListProcessor<>(BooklistCsvRecord.class);
            csvParserSettings.setProcessor(rowProcessor);

            // CSVファイルを解析する
            CsvParser parser = new CsvParser(csvParserSettings);
            parser.parse(isr);

            // 変換した JavaBean のリストを取得する
            booklistCsvRecordList = rowProcessor.getBeans();
        } catch (IOException e) {
            throw new WebApplicationRuntimeException(
                    mph.getMessage("UploadBooklistForm.fileupload.openerr", null));
        }

        return booklistCsvRecordList;
    }

    private CsvParserSettings createCsvParserSettings() {
        CsvParserSettings csvParserSettings = new CsvParserSettings();
        csvParserSettings.setHeaderExtractionEnabled(true);
        csvParserSettings.getFormat().setLineSeparator("\r\n");
        return csvParserSettings;
    }

}
