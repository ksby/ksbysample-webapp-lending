package ksbysample.webapp.lending.values

import ksbysample.webapp.lending.values.lendingapp.LendingAppStatusValues
import ksbysample.webapp.lending.values.lendingbook.LendingBookLendingAppFlgValues
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ValuesHelperTest extends Specification {

    @Autowired
    ValuesHelper vh;

    def "GetValue(#classSimpleName, #valueName) --> #result"() {
        expect:
        vh.getValue(classSimpleName, valueName) == result

        where:
        classSimpleName                  | valueName        || result
        "LendingAppStatusValues"         | "TENPORARY_SAVE" || "1"
        "LendingAppStatusValues"         | "PENDING"        || "3"
        "LendingBookLendingAppFlgValues" | "NOT_APPLY"      || ""
    }

    def "GetText(#classSimpleName, #value) --> #result"() {
        expect:
        vh.getText(classSimpleName, value) == result

        where:
        classSimpleName                  | value || result
        "LendingAppStatusValues"         | "1"   || "一時保存"
        "LendingAppStatusValues"         | "3"   || "申請中"
        "LendingBookLendingAppFlgValues" | ""    || "しない"
    }

    def "Values(#classSimpleName)"() {
        expect:
        vh.values(classSimpleName) == result

        where:
        classSimpleName                  || result
        "LendingAppStatusValues"         || LendingAppStatusValues.values()
        "LendingBookLendingAppFlgValues" || LendingBookLendingAppFlgValues.values()
    }
}
