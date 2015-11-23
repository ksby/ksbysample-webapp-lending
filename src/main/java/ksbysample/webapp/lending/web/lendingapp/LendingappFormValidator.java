package ksbysample.webapp.lending.web.lendingapp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static ksbysample.webapp.lending.values.LendingBookLendingAppFlgValues.APPLY;

@Component
public class LendingappFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(LendingappForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LendingappForm lendingappForm = (LendingappForm) target;

        // 以下の点をチェックする
        // ・最低１つ「申請する」が選択されているか
        // ・「申請する」が選択されている場合に申請理由が入力されているか
        boolean existApply = false;
        boolean rejectEmptyReason = false;
        int i = 0;
        for (LendingBookDto lendingBookDto : lendingappForm.getLendingBookDtoList()) {
            if (StringUtils.equals(lendingBookDto.getLendingAppFlg(), APPLY.getValue())) {
                existApply = true;

                if (StringUtils.isBlank(lendingBookDto.getLendingAppReason())) {
                    errors.rejectValue(String.format("lendingBookDtoList[%d].lendingAppReason", i), null);
                    if (!rejectEmptyReason) {
                        errors.reject("LendingappForm.lendingBookDtoList.emptyReason", null);
                        rejectEmptyReason = true;
                    }
                }
            }
            i++;
        }

        if (!existApply) {
            i = 0;
            for (LendingBookDto lendingBookDto : lendingappForm.getLendingBookDtoList()) {
                errors.rejectValue(String.format("lendingBookDtoList[%d].lendingAppFlg", i), null);
                i++;
            }
            errors.reject("LendingappForm.lendingBookDtoList.notExistApply", null);
        }
    }

}
