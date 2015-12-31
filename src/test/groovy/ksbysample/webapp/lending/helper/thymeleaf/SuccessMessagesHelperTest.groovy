package ksbysample.webapp.lending.helper.thymeleaf

import org.springframework.ui.ExtendedModelMap
import spock.lang.Specification

class SuccessMessagesHelperTest extends Specification {

    def "SuccessMessagesHelperTestのテスト_コンストラクタを使用する場合"() {
        setup:
        def model = new ExtendedModelMap()

        expect:
        def successMessagesHelper = new SuccessMessagesHelper(str);
        successMessagesHelper.setToModel(model)
        model.get("successMessages") == result

        where:
        str                           || result
        "コンストラクタで追加する" || ["コンストラクタで追加する"]
    }

    def "SuccessMessagesHelperTestのテスト_addMsgを使用する場合"() {
        setup:
        def successMessagesHelper = new SuccessMessagesHelper();
        def model = new ExtendedModelMap()

        expect:
        for (str in list) {
            successMessagesHelper.addMsg(str)
        }
        successMessagesHelper.setToModel(model)
        model.get("successMessages") == result

        where:
        list                                                 || result
        ["１つ追加する"]                                    || ["１つ追加する"]
        ["２つ追加する(１つ目)", "２つ追加する(２つ目)"] || ["２つ追加する(１つ目)", "２つ追加する(２つ目)"]
    }

}
