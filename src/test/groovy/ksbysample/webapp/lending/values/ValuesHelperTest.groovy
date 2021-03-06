package ksbysample.webapp.lending.values

import ksbysample.webapp.lending.values.lendingapp.LendingAppStatusValues
import ksbysample.webapp.lending.values.lendingbook.LendingBookLendingAppFlgValues
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class ValuesHelperTest extends Specification {

    @Autowired
    ValuesHelper vh;

    @Unroll
    def "GetValue(#classSimpleName, #valueName) --> #result"() {
        expect:
        vh.getValue(classSimpleName, valueName) == result

        where:
        classSimpleName                  | valueName        || result
        "LendingAppStatusValues"         | "TENPORARY_SAVE" || "1"
        "LendingAppStatusValues"         | "PENDING"        || "3"
        "LendingBookLendingAppFlgValues" | "NOT_APPLY"      || ""
    }

    @Unroll
    def "GetText(#classSimpleName, #value) --> #result"() {
        expect:
        vh.getText(classSimpleName, value) == result

        where:
        classSimpleName                  | value || result
        "LendingAppStatusValues"         | "1"   || "一時保存"
        "LendingAppStatusValues"         | "3"   || "申請中"
        "LendingBookLendingAppFlgValues" | ""    || "しない"
    }

    @Unroll
    def "Values(#classSimpleName)"() {
        expect:
        vh.values(classSimpleName) == result

        where:
        classSimpleName                  || result
        "LendingAppStatusValues"         || LendingAppStatusValues.values()
        "LendingBookLendingAppFlgValues" || LendingBookLendingAppFlgValues.values()
    }
}
