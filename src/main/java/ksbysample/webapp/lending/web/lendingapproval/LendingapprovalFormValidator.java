package ksbysample.webapp.lending.web.lendingapproval;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static ksbysample.webapp.lending.values.lendingbook.LendingBookApprovalResultValues.REJECT;

@Component
public class LendingapprovalFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(LendingapprovalForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LendingapprovalForm lendingapprovalForm = (LendingapprovalForm) target;

        // 以下の点をチェックする
        // ・全ての書籍で承認か却下が選択されているか
        // ・却下が選択された書籍で却下理由が入力されているか
        boolean approvalResultAllChecked = true;
        int i = 0;
        for (ApplyingBookForm applyingBookForm : lendingapprovalForm.getApplyingBookFormList()) {
            if (StringUtils.isBlank(applyingBookForm.getApprovalResult())) {
                approvalResultAllChecked = false;
            }

            if (StringUtils.equals(applyingBookForm.getApprovalResult(), REJECT.getValue())
                    && StringUtils.isBlank(applyingBookForm.getApprovalReason())) {
                errors.rejectValue(String.format("applyingBookFormList[%d].approvalReason", i), null);
            }

            i++;
        }

        if (!approvalResultAllChecked) {
            errors.reject("LendingapprovalForm.applyingBookFormList.approvalResult.notAllCheckedErr");
        }
    }

}
