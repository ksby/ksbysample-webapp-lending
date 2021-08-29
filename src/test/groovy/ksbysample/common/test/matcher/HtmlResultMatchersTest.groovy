package ksbysample.common.test.matcher

import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.DefaultMvcResult
import spock.lang.Specification

class HtmlResultMatchersTest extends Specification {

    def TEST_HTML = '''\
        <html>
            <body>
                <div id="title">メニュー一覧</div>
                <div>
                    <ul class="menu">
                        <li class="item">メニュー１</li>
                        <li class="item">メニュー２</li>
                    </ul>
                </div>
            </body>
        </html>
    '''

    def mvcResult

    def setup() {
        def mockRequest = new MockHttpServletRequest()
        def mockResponse = Mock(MockHttpServletResponse)
        mockResponse.getContentAsString() >> TEST_HTML
        this.mvcResult = new DefaultMvcResult(mockRequest, mockResponse)
    }

    def "html(#cssQuery).text(#text)_要素がありテキストが一致する場合"() {
        expect:
        def htmlResultMatchers = HtmlResultMatchers.html(cssQuery)
        def resultMatcher = htmlResultMatchers.text(text)
        resultMatcher.match(this.mvcResult)

        where:
        cssQuery                || text
        "#title"                || "メニュー一覧"
        "ul.menu li.item:eq(0)" || "メニュー１"
    }

    def "html().text()_要素はあるがテキストが一致しない場合 AssertionError が throw される"() {
        when:
        def htmlResultMatchers = HtmlResultMatchers.html("#title")
        def resultMatcher = htmlResultMatchers.text("メニュー")
        resultMatcher.match(this.mvcResult)

        then:
        AssertionError e = thrown()
        e.getMessage() contains ""
    }

    def "html().text()_要素がない場合 AssertionError が throw される"() {
        when:
        def htmlResultMatchers = HtmlResultMatchers.html("#titlex")
        def resultMatcher = htmlResultMatchers.text("メニュー一覧")
        resultMatcher.match(this.mvcResult)

        then:
        AssertionError e = thrown()
        e.getMessage() contains "指定された cssQuery の Element は存在しません ( cssQuery = #titlex )"
    }

    def "html(#cssQuery).count(#count)"() {
        expect:
        def htmlResultMatchers = HtmlResultMatchers.html(cssQuery)
        def resultMatcher = htmlResultMatchers.count(count)
        resultMatcher.match(this.mvcResult)

        where:
        cssQuery     || count
        "#title"     || 1
        "#titlex"    || 0
        "ul.menu li" || 2
    }

    def "html().exists()_要素がある場合 AssertionError は throw されない"() {
        when:
        def htmlResultMatchers = HtmlResultMatchers.html("#title")
        def resultMatcher = htmlResultMatchers.exists()
        resultMatcher.match(this.mvcResult)

        then:
        notThrown(AssertionError)
    }

    def "html().exists()_要素がない場合 AssertionError が throw される"() {
        when:
        def htmlResultMatchers = HtmlResultMatchers.html("#titlex")
        def resultMatcher = htmlResultMatchers.exists()
        resultMatcher.match(this.mvcResult)

        then:
        AssertionError e = thrown()
        e.getMessage() contains "cssQuery '#titlex' does not exist"
    }

    def "html().notExists()_要素がない場合 AssertionError は throw されない"() {
        when:
        def htmlResultMatchers = HtmlResultMatchers.html("#titlex")
        def resultMatcher = htmlResultMatchers.notExists()
        resultMatcher.match(this.mvcResult)

        then:
        notThrown(AssertionError)
    }

    def "html().notExists()_要素がある場合 AssertionError は throw される"() {
        when:
        def htmlResultMatchers = HtmlResultMatchers.html("#title")
        def resultMatcher = htmlResultMatchers.notExists()
        resultMatcher.match(this.mvcResult)

        then:
        AssertionError e = thrown()
        e.getMessage() contains "cssQuery '#title' exists"
    }

}
