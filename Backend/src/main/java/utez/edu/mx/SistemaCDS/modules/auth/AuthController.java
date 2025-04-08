package utez.edu.mx.SistemaCDS.modules.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.SistemaCDS.modules.auth.DTO.AuthLoginDTO;

// 10.- Crear el controller para Auth
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"*"})
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("")
    public ResponseEntity<?> login(@RequestBody AuthLoginDTO authLoginDTO) {
        return authService.login(authLoginDTO);
    }
}
