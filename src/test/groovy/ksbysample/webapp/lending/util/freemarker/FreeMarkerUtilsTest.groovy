package ksbysample.webapp.lending.util.freemarker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class FreeMarkerUtilsTest extends Specification {

    @Autowired
    FreeMarkerUtils freeMarkerUtils

    class TestUser {
        String name
        Integer age
        String address
    }

    def "テンプレートファイルから文字列を生成する_変数のみの場合"() {
        setup:
        def model = [username: "田中　太郎"]

        expect:
        freeMarkerUtils.merge("mail/FreeMarkerUtilsTest-001.ftl", model) == "田中　太郎"
    }

    /**
     * 変数が null の場合にエラーにならないようにするには、テンプレートファイルの変数の最後に ! を付けること。
     * ${xxx} ではなく ${xxx!} のように書く。
     * null の時にはスペースで埋めたい時には ${xxx!?left_pad(3)} のように書ける
     *
     * ただし、この記述にすると model に必要なデータがない時に例外が throw されないので注意すること
     */
    def "テンプレートファイルから文字列を生成する_変数がnullの場合"() {
        setup:
        def model = [username: null]

        expect:
        freeMarkerUtils.merge("mail/FreeMarkerUtilsTest-001.ftl", model) == ""
    }

    def "テンプレートファイルから文字列を生成する_変数＋クラスの場合"() {
        setup:
        def model = [
                username: "田中　太郎"
                , user  : new TestUser(age: 25, address: "東京都千代田区")
        ]
        def result = new File("src/test/resources/templates/mail/FreeMarkerUtilsTest-002-result.txt").text

        expect:
        freeMarkerUtils.merge("mail/FreeMarkerUtilsTest-002.ftl", model) == result
    }

    def "テンプレートファイルから文字列を生成する_リストの場合"() {
        setup:
        def userList = [
                new TestUser(name: "田中　太郎", age: 25, address: "東京都千代田区")
                , new TestUser(name: "鈴木　花子", age: 8, address: "神奈川県横浜市")
                , new TestUser(name: "高橋　孝", age: 100, address: "埼玉県大宮市")
        ]
        def model = [userList: userList]
        def result = new File("src/test/resources/templates/mail/FreeMarkerUtilsTest-003-result.txt").text

        expect:
        freeMarkerUtils.merge("mail/FreeMarkerUtilsTest-003.ftl", model) == result
    }

    def "テンプレートファイルから文字列を生成する_リストでnameの一部がnullの場合"() {
        setup:
        def userList = [
                new TestUser(name: "田中　太郎", age: 25, address: "東京都千代田区")
                , new TestUser(name: null, age: 8, address: "神奈川県横浜市")
                , new TestUser(name: "高橋　孝", age: 100, address: "埼玉県大宮市")
        ]
        def model = [userList: userList]
        def result = new File("src/test/resources/templates/mail/FreeMarkerUtilsTest-003-result2.txt").text

        expect:
        freeMarkerUtils.merge("mail/FreeMarkerUtilsTest-003.ftl", model) == result
    }

    def "テンプレートファイルが存在しない場合はエラーになる"() {
        given:
        def model = [username: "田中　太郎"]

        when:
        freeMarkerUtils.merge("mail/FreeMarkerUtilsTest-notFound.ftl", model)

        then:
        RuntimeException e = thrown()
        e.getMessage() contains "Template not found"
    }

}
