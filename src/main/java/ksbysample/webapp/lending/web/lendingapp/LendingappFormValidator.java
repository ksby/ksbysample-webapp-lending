package ksbysample.webapp.lending.web.lendingapp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static ksbysample.webapp.lending.values.lendingbook.LendingBookLendingAppFlgValues.APPLY;

/**
 * ???
 */
@Component
public class LendingappFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(LendingappForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Assert.notNull(target, "target must not be null");
        LendingappForm lendingappForm = (LendingappForm) target;

        // 「一時保存」ボタンが押された時は validate メソッドの入力チェックは実行しない
        if (StringUtils.equals(lendingappForm.getBtn(), "temporarySave")) {
            return;
        }

        // 以下の点をチェックする
        // ・最低１つ「申請する」が選択されているか
        // ・「申請する」が選択されている場合に申請理由が入力されているか
        boolean existApply = false;
        boolean rejectEmptyReason = false;
        int i = 0;
        for (LendingBookDto lendingBookDto : lendingappForm.getLendingBookDtoList()) {
            if (StringUtils.equals(lendingBookDto.getLendingAppFlg(), APPLY.getValue())) {
                existApply = true;
                rejectEmptyReason = errorsRejectWhenLendingAppReasonIsBlank(errors, lendingBookDto, i
                        , rejectEmptyReason);
            }
            i++;
        }

        if (!existApply) {
            for (i = 0; i < lendingappForm.getLendingBookDtoList().size(); i++) {
                errors.rejectValue(String.format("lendingBookDtoList[%d].lendingAppFlg", i)
                        , "LendingappForm.lendingBookDtoList.notExistApply");
            }
            errors.reject("LendingappForm.lendingBookDtoList.notExistApply");
        }
    }

    private boolean errorsRejectWhenLendingAppReasonIsBlank(Errors errors, LendingBookDto lendingBookDto
            , int index, boolean rejectEmptyReason) {
        boolean calledReject = false;
        if (StringUtils.isBlank(lendingBookDto.getLendingAppReason())) {
            errors.rejectValue(String.format("lendingBookDtoList[%d].lendingAppReason", index)
                    , "LendingappForm.lendingBookDtoList.emptyReason");
            if (!rejectEmptyReason) {
                errors.reject("LendingappForm.lendingBookDtoList.emptyReason");
                calledReject = true;
            }
        }
        return calledReject;
    }

}




















