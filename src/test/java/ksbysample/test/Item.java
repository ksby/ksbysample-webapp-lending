package ksbysample.test;

import com.univocity.parsers.annotations.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ToString
public class Item {

    // @Trim を付けると前後の半角スペースが除去される
    @Trim
    // 英大文字に変換する
    @UpperCase
    // CSVファイルのカラムと関連付けるフィールドには @Parsed を付ける
    @Parsed(field = "商品コード")
    private String itemCode;

    @Trim
    @Parsed(field = "商品名")
    private String itemName;
    
    // "-" は null とみなす
    @NullString(nulls = { "-" })
    // null の場合には 0 にする ( 空の場合も 0 になる )
    @Parsed(field = "価格", defaultNullRead = "0")
    private BigDecimal price;

    @Trim
    @Convert(conversionClass = ItemTypeStringToCodeConversion.class)
    @Parsed(field = "種別")
    private String itemType;

    @BooleanString(falseStrings = { "無" }, trueStrings = { "有" })
    @Parsed(field = "在庫")
    private Boolean stock;

    @Trim
    // index ( 0～ )でも指定可能
    @Parsed(index = 5)
    private String description;

    @NullString(nulls = { "未登録", "-" })
    @Convert(conversionClass = StringToLocalDateTimeConversion.class)
    @Parsed(field = "登録日")
    private LocalDateTime dateOfRegistration;

}
