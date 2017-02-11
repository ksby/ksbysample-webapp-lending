package ksbysample.test;

import com.google.common.base.Joiner;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.common.processor.ObjectRowListProcessor;
import com.univocity.parsers.conversions.Conversions;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import ksbysample.webapp.lending.Application;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Ignore("CSVライブラリuniVocity-parsersのテストで本機能とは関係ないのでテストを実行対象外にする")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UniVocityParsersTest {

    @Test
    public void uniVocityParsersTest_001() throws Exception {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\r\n");  // 改行コードは CR+LF
        settings.setHeaderExtractionEnabled(true);      // 1行目はヘッダ行としてスキップする

        CsvParser parser = new CsvParser(settings);

        try (
                BufferedReader br
                        = Files.newBufferedReader(Paths.get("C:/tmp/テストデータ.csv")
                        , Charset.forName("Windows-31J"));
        ) {
            List<String[]> allRows = parser.parseAll(br);
            allRows.stream().forEach(row -> System.out.println(Joiner.on(", ").join(row)));
        }
    }

    @Test
    public void uniVocityParsersTest_002() throws Exception {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\r\n");  // 改行コードは CR+LF
        settings.setHeaderExtractionEnabled(true);      // 1行目はヘッダ行としてスキップする

        CsvParser parser = new CsvParser(settings);

        try (
                BufferedReader br
                        = Files.newBufferedReader(Paths.get("C:/tmp/テストデータ.csv")
                        , Charset.forName("Windows-31J"));
        ) {
            parser.beginParsing(br);
            String[] row;
            while ((row = parser.parseNext()) != null) {
                System.out.println(Joiner.on(", ").join(row));
            }
            // ファイルの最後まで読みこめば自動的にクローズされるが、読み込まない場合には以下のメソッドを呼び出す
            // parser.stopParsing();
        }
    }

    @Test
    public void uniVocityParsersTest_003() throws Exception {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\r\n");  // 改行コードは CR+LF
        settings.setHeaderExtractionEnabled(true);      // 1行目はヘッダ行としてスキップする

        // 商品コードは必ず英大文字に変換する
        ObjectRowListProcessor rowProcessor = new ObjectRowListProcessor();
        rowProcessor.convertFields(Conversions.toUpperCase()).set("商品コード");
        settings.setProcessor(rowProcessor);

        CsvParser parser = new CsvParser(settings);

        try (
                BufferedReader br
                        = Files.newBufferedReader(Paths.get("C:/tmp/テストデータ.csv")
                        , Charset.forName("Windows-31J"));
        ) {
            parser.parse(br);
            String[] headers = rowProcessor.getHeaders();       // ヘッダー行を取得する
            List<Object[]> allRows = rowProcessor.getRows();    // データ行を取得する
            System.out.println(Joiner.on(", ").join(headers));
            allRows.stream().forEach(row -> System.out.println(Joiner.on(", ").join(row)));
        }
    }

    @Test
    public void uniVocityParsersTest_004() throws Exception {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\r\n");  // 改行コードは CR+LF
        settings.setHeaderExtractionEnabled(true);      // 1行目はヘッダ行としてスキップする

        BeanListProcessor<Item> rowProcessor = new BeanListProcessor<>(Item.class);
        settings.setProcessor(rowProcessor);

        CsvParser parser = new CsvParser(settings);

        try (
                BufferedReader br
                        = Files.newBufferedReader(Paths.get("C:/tmp/テストデータ.csv")
                        , Charset.forName("Windows-31J"));
        ) {
            parser.parse(br);
            List<Item> allRows = rowProcessor.getBeans();       // データ行を取得する
            allRows.stream().forEach(System.out::println);
        }
    }

    @Test
    public void uniVocityParsersTest_005() throws Exception {
        try (
                BufferedWriter bw
                        = Files.newBufferedWriter(Paths.get("C:/tmp/テストデータ２.csv")
                        , Charset.forName("Windows-31J")
                        , StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                ) {
            CsvWriter writer = new CsvWriter(bw, new CsvWriterSettings());
            writer.writeHeaders("商品コード", "商品名", "説明");
            writer.writeRow("SAMPLE-101", "サンプル１０１", "これはテストです");
            writer.writeRow("SAMPLE-102", "サンプル１０２", "途中に\"コメント\",\"コメント２\"を入れてみます");
            writer.close();
        }
    }

    @Test
    public void uniVocityParsersTest_006() throws Exception {
        List<Item> itemList = new ArrayList<>();
        // １件目
        Item item = new Item();
        item.setItemCode("SAMPLE-201");
        item.setItemName("サンプル２０１号");
        item.setPrice(new BigDecimal("1234567890"));
        item.setItemType("0001");
        item.setStock(false);
        item.setDescription("\"１行目\",\"コメントあり\"の商品です");
        item.setDateOfRegistration(LocalDateTime.of(2015, 9, 27, 10, 15, 0));
        itemList.add(item);
        // ２件目
        item = new Item();
        item.setItemCode("SAMPLE-305");
        item.setItemName("サンプル３０５号");
        item.setPrice(new BigDecimal("56000"));
        item.setItemType("9999");
        item.setStock(true);
        item.setDescription("２行目はテスト商品です");
        item.setDateOfRegistration(LocalDateTime.of(2015, 9, 28, 0, 0, 0));
        itemList.add(item);

        try (
                BufferedWriter bw
                        = Files.newBufferedWriter(Paths.get("C:/tmp/テストデータ２.csv")
                        , Charset.forName("Windows-31J")
                        , StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        ) {
            CsvWriterSettings settings = new CsvWriterSettings();
            settings.setHeaders("商品コード", "商品名", "価格", "種別", "在庫", "説明", "登録日");

            BeanWriterProcessor<Item> writerProcessor = new BeanWriterProcessor<>(Item.class);
            settings.setRowWriterProcessor(writerProcessor);

            CsvWriter writer = new CsvWriter(bw, settings);
            writer.writeHeaders();
            writer.processRecordsAndClose(itemList);
        }
    }

}
