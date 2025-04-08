package utez.edu.mx.SistemaCDS.modules.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.SistemaCDS.modules.auth.DTO.AuthLoginDTO;
import utez.edu.mx.SistemaCDS.modules.employee.Employee;
import utez.edu.mx.SistemaCDS.modules.employee.EmployeeDetailsImpl;
import utez.edu.mx.SistemaCDS.modules.employee.EmployeeRepository;
import utez.edu.mx.SistemaCDS.utils.CustomResponseEntity;
import utez.edu.mx.SistemaCDS.utils.security.JWTUtil;
import utez.edu.mx.SistemaCDS.utils.security.UserDetailsService;


// 9.- Crear servicio de Auth para el login
@Service
public class AuthService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomResponseEntity customResponseEntity;

    @Autowired
    private JWTUtil jwtUtil;

    @Transactional(readOnly = true)
    public ResponseEntity<?> login(AuthLoginDTO authLoginDTO) {
        Employee found = employeeRepository.findByPasswordAndUsername(
                authLoginDTO.getPassword(),
                authLoginDTO.getUser()
        );
        if(found == null) {
            return customResponseEntity.get404Response("No se pudo iniciar sesion");
        } else {
            try {
                UserDetails userDetails = new EmployeeDetailsImpl(found);
                return customResponseEntity.getOkLoginResponse(
                        "Inicio de sesión exitoso",
                        found.getId(),
                        found.getRol().getName().substring(5), // Se envia el rol en la respuesta para validar del otro lado
                        jwtUtil.generateToken(userDetails)
                );
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return customResponseEntity.get400Response("Error al iniciar sesion");
            }
        }
    }
}
