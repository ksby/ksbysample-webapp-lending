package ksbysample.webapp.lending.util.velocity;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class VelocityUtils {

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${spring.velocity.charset}")
    private String charset;

    public String merge(String templateLocation, Map<String, Object> model) {
        // model 内の値が null のデータは空文字列に入れ替える
        Map<String, Object> modelForVelocity = new HashMap<>();
        for (Map.Entry<String, Object> e : model.entrySet()) {
            modelForVelocity.put(e.getKey(), (e.getValue() == null ? "" : e.getValue()));
        }

        return VelocityEngineUtils.mergeTemplateIntoString(this.velocityEngine, templateLocation, charset, modelForVelocity);
    }

}
