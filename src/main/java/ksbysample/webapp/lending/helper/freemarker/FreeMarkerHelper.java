package ksbysample.webapp.lending.helper.freemarker;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * ???
 */
@Component
public class FreeMarkerHelper {

    private final Configuration freeMarkerConfiguration;

    /**
     * ???
     *
     * @param freeMarkerConfiguration ???
     */
    public FreeMarkerHelper(Configuration freeMarkerConfiguration) {
        this.freeMarkerConfiguration = freeMarkerConfiguration;
    }

    /**
     * ???
     *
     * @param templateLocation ???
     * @param model ???
     * @return ???
     */
    public String merge(String templateLocation, Map<String, Object> model) {
        Template template = getTemplate(templateLocation);
        return process(template, model);
    }

    private Template getTemplate(String templateLocation) {
        try {
            return this.freeMarkerConfiguration.getTemplate(templateLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressFBWarnings("TEMPLATE_INJECTION_FREEMARKER")
    private String process(Template template, Map<String, Object> model) {
        try {
            StringWriter sw = new StringWriter();
            template.process(model, sw);
            return sw.toString();
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
