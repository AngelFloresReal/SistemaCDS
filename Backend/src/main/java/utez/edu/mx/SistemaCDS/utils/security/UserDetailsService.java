package utez.edu.mx.SistemaCDS.utils.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utez.edu.mx.SistemaCDS.modules.employee.Employee;
import utez.edu.mx.SistemaCDS.modules.employee.EmployeeRepository;


import java.util.Collections;

// 5.- Crear UserDetailsService para el manejo de autorizaciones
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario por nombre de usuario
        Employee employee = employeeRepository.findByUsername(username);
        if(employee == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        //Convertir el rol a GrantedAuthority
        GrantedAuthority authority = new SimpleGrantedAuthority(employee.getRol().getName());

        return new org.springframework.security.core.userdetails.User(
                employee.getUsername(),
                employee.getPassword(),
                Collections.singleton(authority)
        );
    }
}