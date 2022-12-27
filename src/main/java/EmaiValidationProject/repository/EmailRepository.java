package EmaiValidationProject.repository;

import com.Cursova.EmaiValidationProject.entity.EmailModelDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<EmailModelDTO, UUID> {
    EmailModelDTO findFirstByAddressOrderBySendTimeDesc(String address);
}
