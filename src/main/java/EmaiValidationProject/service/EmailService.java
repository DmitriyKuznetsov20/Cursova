package EmaiValidationProject.service;

import com.Cursova.EmaiValidationProject.config.ValidationConfiguration;
import com.Cursova.EmaiValidationProject.entity.EmailModelDTO;
import com.Cursova.EmaiValidationProject.exception.ApiRequestException;
import com.Cursova.EmaiValidationProject.helper.CodeGeneratorHelper;
import com.Cursova.EmaiValidationProject.helper.HtmlTemplatingHelper;
import com.Cursova.EmaiValidationProject.helper.JavaMailSenderHelper;
import com.Cursova.EmaiValidationProject.repository.EmailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    private final EmailRepository repository;
    private final CodeGeneratorHelper codeGeneratorHelper;
    private final JavaMailSenderHelper mailSenderHelper;
    private final ValidationConfiguration codeConfig;
    private final ObjectMapper objectMapper;
    private final HtmlTemplatingHelper htmlTemplatingHelper;


    @Autowired
    public EmailService(EmailRepository repository, CodeGeneratorHelper codeGeneratorHelper, JavaMailSenderHelper mailSenderHelper, ValidationConfiguration codeConfig, ObjectMapper objectMapper, HtmlTemplatingHelper htmlTemplatingHelper) {
        this.repository = repository;
        this.codeGeneratorHelper = codeGeneratorHelper;
        this.mailSenderHelper = mailSenderHelper;
        this.codeConfig = codeConfig;
        this.objectMapper = objectMapper;
        this.htmlTemplatingHelper = htmlTemplatingHelper;
    }

    public List<EmailModelDTO> getAll() {
        return repository.findAll();
    }

    public EmailModelDTO sendCode(String email) {
        String code = codeGeneratorHelper.getCode();

        try {
            mailSenderHelper.sendEmailWithAttachment(email, "Test", htmlTemplatingHelper.addCodeToHtml(code));
            LocalDateTime time = LocalDateTime.now();
            EmailModelDTO emailModel = new EmailModelDTO();
            emailModel.setAddress(email);
            emailModel.setCode(code);
            emailModel.setSendTime(time);
            emailModel.setValidTime(time.plusMinutes(codeConfig.getValidForMinutes()));
            return repository.save(emailModel);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public Optional<EmailModelDTO> getEmail(UUID id) {
        return repository.findById(id);
    }

    public ObjectNode isValid(String email, String code) {
        ObjectNode objectNode = objectMapper.createObjectNode();

        EmailModelDTO emailModel = repository.findFirstByAddressOrderBySendTimeDesc(email);

        if (emailModel == null)
            throw new ApiRequestException("Email not found!");

        boolean valid = emailModel.getCode().equals(code) && LocalDateTime.now().isBefore(emailModel.getValidTime());

        objectNode.put("valid", valid);
        return objectNode;
    }
}
