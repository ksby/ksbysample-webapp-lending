package ksbysample.common.test.matcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Jsoup ( https://jsoup.org/ ) を利用して HTML のチェックをする
 * {@link org.springframework.test.web.servlet.ResultActions#andExpect(ResultMatcher)} で使用するためのクラス
 * <p>
 * <pre>{@code
 *      mockMvc.perform(get("/"))
 *          .andExpect(status().isOk())
 *          .andExpect(html("#id").text("ここに期待する文字列を記載する"));
 *          .andExpect(html("div > div:eq(1) > a:eq(1)").count(1));
 *          .andExpect(html(".css-selector").exists());
 * }</pre>
 */
public class HtmlResultMatchers {

    private String cssQuery;

    public HtmlResultMatchers(String cssQuery) {
        this.cssQuery = cssQuery;
    }

    /**
     * {@link HtmlResultMatchers} オブジェクト生成用の static メソッド
     *
     * @param cssQuery {@link org.jsoup.nodes.Element#select(String)} に指定する CSS セレクタ
     * @return {@link HtmlResultMatchers}
     */
    public static HtmlResultMatchers html(String cssQuery) {
        return new HtmlResultMatchers(cssQuery);
    }

    /**
     * HTML 内の cssQuery で指定された部分の文字列を抽出し、引数で渡された文字列と同じかチェックする
     *
     * @param expectedText 文字列の期待値
     * @return {@link ResultMatcher}
     */
    public ResultMatcher text(final String expectedText) {
        return mvcResult -> assertThat(selectFirst(mvcResult).text(), is(expectedText));
    }

    /**
     * HTML 内の cssQuery で指定された要素数を取得し、引数で渡された数と同じかチェックする
     *
     * @param expectedCount 要素数の期待値
     * @return {@link ResultMatcher}
     */
    public ResultMatcher count(final int expectedCount) {
        return mvcResult -> assertThat(select(mvcResult).size(), is(expectedCount));
    }

    /**
     * HTML 内の cssQuery で指定された要素が存在するかチェックする
     *
     * @return {@link ResultMatcher}
     */
    public ResultMatcher exists() {
        return mvcResult -> assertTrue("cssQuery '" + this.cssQuery + "' does not exist"
                , select(mvcResult).size() != 0);
    }

    /**
     * HTML 内の cssQuery で指定された要素が存在しないかチェックする
     *
     * @return {@link ResultMatcher}
     */
    public ResultMatcher notExists() {
        return mvcResult -> assertTrue("cssQuery '" + this.cssQuery + "' exists"
                , select(mvcResult).size() == 0);
    }

    private Document parseHtml(MvcResult mvcResult) throws UnsupportedEncodingException {
        return Jsoup.parse(mvcResult.getResponse().getContentAsString());
    }

    private Elements select(MvcResult mvcResult) throws UnsupportedEncodingException {
        return parseHtml(mvcResult).select(this.cssQuery);
    }

    private Element selectFirst(MvcResult mvcResult) throws UnsupportedEncodingException {
        Element element = select(mvcResult).first();
        if (element == null) {
            throw new AssertionError(
                    String.format("指定された cssQuery の Element は存在しません ( cssQuery = %s )", this.cssQuery));
        }
        return element;
    }

}
