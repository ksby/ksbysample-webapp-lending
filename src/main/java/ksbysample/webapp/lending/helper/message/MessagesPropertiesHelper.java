package ksbysample.webapp.lending.helper.message;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * ???
 */
@Component
public class MessagesPropertiesHelper {

    private final MessageSource messageSource;

    /**
     * @param messageSource ???
     */
    public MessagesPropertiesHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * @param code ???
     * @param args ???
     * @return ???
     */
    @SuppressWarnings({"PMD.UseVarargs"})
    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
