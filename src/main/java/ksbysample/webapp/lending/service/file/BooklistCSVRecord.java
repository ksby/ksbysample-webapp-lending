package ksbysample.webapp.lending.service.file;

import com.univocity.parsers.annotations.Parsed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooklistCSVRecord {

    @Parsed
    private String isbn;

    @Parsed(field = "書名")
    private String bookName;

}
