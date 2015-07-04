package ksbysample.common.test;

import org.hamcrest.Matcher;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.ModelResultMatchers;
import org.springframework.ui.ModelMap;

import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

public class CustomModelResultMatchers extends ModelResultMatchers {

    public static CustomModelResultMatchers modelEx() {
        return new CustomModelResultMatchers();
    }

    @SuppressWarnings("unchecked")
    public <T> ResultMatcher property(final String nameAndProperty, final Matcher<T> matcher) {
        return mvcResult -> {
            // <インスタンス名>.<プロパティ名> ( 例: page.number ) の形式の文字列を
            // インスタンス名とプロパティ名に分割する
            Pattern p = Pattern.compile("^(\\S+?)\\.(\\S+)$");
            java.util.regex.Matcher m = p.matcher(nameAndProperty);
            assertThat(m.find(), is(true));
            String name = m.group(1);
            String property = m.group(2);

            // プロパティの値を取得してチェックする
            ModelMap modelMap = mvcResult.getModelAndView().getModelMap();
            Object object = modelMap.get(name);
            assertThat(object, is(notNullValue()));
            EvaluationContext context = new StandardEvaluationContext(object);
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(property);
            Object value = exp.getValue(context);
            assertThat((T) value, matcher);
        };
    }

}
