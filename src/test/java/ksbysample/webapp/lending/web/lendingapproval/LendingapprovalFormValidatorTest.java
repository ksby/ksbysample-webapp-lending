package ksbysample.webapp.lending.web.lendingapproval;

import ksbysample.common.test.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.yaml.snakeyaml.Yaml;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LendingapprovalFormValidatorTest {

    // テストデータ
    private LendingapprovalForm lendingapprovalForm_001
            = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_001.yaml"));
    private LendingapprovalForm lendingapprovalForm_002
            = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_002.yaml"));
    private LendingapprovalForm lendingapprovalForm_003
            = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_003.yaml"));
    private LendingapprovalForm lendingapprovalForm_004
            = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_004.yaml"));
    private LendingapprovalForm lendingapprovalForm_005
            = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_005.yaml"));

    @Autowired
    private LendingapprovalFormValidator lendingapprovalFormValidator;

    @Test
    void testValidate_全ての書籍で承認も却下も選択されていない場合はエラーになる() {
        Errors errors = TestHelper.createErrors();
        lendingapprovalFormValidator.validate(lendingapprovalForm_001, errors);
        assertThat(errors.hasGlobalErrors()).isTrue();
        assertThat(errors.getGlobalErrorCount()).isEqualTo(1);
        assertThat(errors.getGlobalErrors())
                .extracting(ObjectError::getCode)
                .containsOnly("LendingapprovalForm.applyingBookFormList.approvalResult.notAllCheckedErr");
        assertThat(errors.hasFieldErrors()).isFalse();
    }

    @Test
    void testValidate_全ての書籍で却下が選択されいるが却下理由が入力されていない場合はエラーになる() {
        Errors errors = TestHelper.createErrors();
        lendingapprovalFormValidator.validate(lendingapprovalForm_002, errors);
        assertThat(errors.hasGlobalErrors()).isFalse();
        assertThat(errors.hasFieldErrors()).isTrue();
        assertThat(errors.getFieldErrorCount()).isEqualTo(3);
        assertThat(errors.getFieldErrors())
                .extracting(FieldError::getField)
                .containsOnly("applyingBookFormList[0].approvalReason"
                        , "applyingBookFormList[1].approvalReason"
                        , "applyingBookFormList[2].approvalReason");
    }

    @Test
    void testValidate_一部の書籍は承認却下未選択で一部の書籍は却下理由未入力の場合はエラーになる() {
        Errors errors = TestHelper.createErrors();
        lendingapprovalFormValidator.validate(lendingapprovalForm_003, errors);
        assertThat(errors.hasGlobalErrors()).isTrue();
        assertThat(errors.getGlobalErrorCount()).isEqualTo(1);
        assertThat(errors.getGlobalErrors())
                .extracting(ObjectError::getCode)
                .containsOnly("LendingapprovalForm.applyingBookFormList.approvalResult.notAllCheckedErr");
        assertThat(errors.hasFieldErrors()).isTrue();
        assertThat(errors.getFieldErrorCount()).isEqualTo(1);
        assertThat(errors.getFieldErrors())
                .extracting(FieldError::getField)
                .containsOnly("applyingBookFormList[2].approvalReason");
    }

    @Test
    void testValidate_全ての書籍で承認が選択されている場合はエラーにならない() {
        Errors errors = TestHelper.createErrors();
        lendingapprovalFormValidator.validate(lendingapprovalForm_004, errors);
        assertThat(errors.hasGlobalErrors()).isFalse();
        assertThat(errors.hasFieldErrors()).isFalse();
    }

    @Test
    void testValidate_全ての書籍で却下が選択され却下理由も入力されている場合はエラーにならない() {
        Errors errors = TestHelper.createErrors();
        lendingapprovalFormValidator.validate(lendingapprovalForm_005, errors);
        assertThat(errors.hasGlobalErrors()).isFalse();
        assertThat(errors.hasFieldErrors()).isFalse();
    }

}
