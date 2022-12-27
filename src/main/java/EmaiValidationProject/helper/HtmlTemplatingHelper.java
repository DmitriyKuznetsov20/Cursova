package EmaiValidationProject.helper;


import com.Cursova.EmaiValidationProject.config.frontConfiguration;
import com.x5.template.Chunk;
import com.x5.template.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HtmlTemplatingHelper {

    private final frontConfiguration htmlTemplateConfig;

    @Autowired
    public HtmlTemplatingHelper(frontConfiguration htmlTemplateConfig) {
        this.htmlTemplateConfig = htmlTemplateConfig;
    }

    public String addCodeToHtml(String code) {
        String templatePath = htmlTemplateConfig.getPath();
        Theme theme = new Theme(templatePath, "");

        Chunk c = theme.makeChunk(htmlTemplateConfig.getFileName());
        c.set(htmlTemplateConfig.getVariableName(), code);

        return c.toString();
    }
}
