package EmaiValidationProject.controller;

import com.Cursova.EmaiValidationProject.entity.AuthenticationDTO;
import com.Cursova.EmaiValidationProject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationController {

    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationDTO data) {
        return ok(authService.signIn(data.getUsername(), data.getPassword()));
    }
}
