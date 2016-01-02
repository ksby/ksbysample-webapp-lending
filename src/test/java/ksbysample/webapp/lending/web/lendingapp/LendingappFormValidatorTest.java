package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class LendingappFormValidatorTest {

    // テストデータ
    private LendingappForm lendingappForm_001
            = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_001.yaml"));
    private LendingappForm lendingappForm_002
            = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_002.yaml"));
    private LendingappForm lendingappForm_003
            = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_003.yaml"));
    private LendingappForm lendingappForm_004
            = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_004.yaml"));

    @Autowired
    private LendingappFormValidator lendingappFormValidator;

    @Test
    public void testValidate_一時保存ボタン押下時は入力チェックは行われない() throws Exception {
        Errors errors = new MapBindingResult(new HashMap<String, String>(), "");
        lendingappFormValidator.validate(lendingappForm_001, errors);
        assertThat(errors.hasFieldErrors()).isFalse();
    }

    @Test
    public void testValidate_１つも申請するが選択されていない場合はエラーになる() throws Exception {
        Errors errors = new MapBindingResult(new HashMap<String, String>(), "");
        lendingappFormValidator.validate(lendingappForm_002, errors);
        assertThat(errors.hasFieldErrors()).isTrue();
        assertThat(errors.getFieldErrorCount()).isEqualTo(3);
        assertThat(errors.getFieldErrors())
                .extracting(FieldError::getField)
                .containsOnly("lendingBookDtoList[0].lendingAppFlg"
                        , "lendingBookDtoList[1].lendingAppFlg"
                        , "lendingBookDtoList[2].lendingAppFlg");
    }

    @Test
    public void testValidate_申請するを選択して申請理由を入力していない場合はエラーになる() throws Exception {
        Errors errors = new MapBindingResult(new HashMap<String, String>(), "");
        lendingappFormValidator.validate(lendingappForm_003, errors);
        assertThat(errors.hasFieldErrors()).isTrue();
        assertThat(errors.getFieldErrorCount()).isEqualTo(2);
        assertThat(errors.getFieldErrors())
                .extracting(FieldError::getField)
                .containsOnly("lendingBookDtoList[0].lendingAppReason"
                        , "lendingBookDtoList[2].lendingAppReason");
    }

    @Test
    public void testValidate_申請するを選択して申請理由も入力している場合はエラーにならない() throws Exception {
        Errors errors = new MapBindingResult(new HashMap<String, String>(), "");
        lendingappFormValidator.validate(lendingappForm_004, errors);
        assertThat(errors.hasFieldErrors()).isFalse();
    }

}