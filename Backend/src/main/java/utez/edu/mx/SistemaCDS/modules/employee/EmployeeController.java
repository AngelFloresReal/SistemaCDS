package utez.edu.mx.SistemaCDS.modules.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = {"*"})
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("")
    @Secured("ROLE_MASTER")
    public ResponseEntity<?> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_MASTER", "ROLE_RD"})
    public ResponseEntity<?> findById(@PathVariable("id") long id) {
        return employeeService.findById(id);
    }

    @GetMapping("/availables")
    @Secured("ROLE_MASTER")
    public ResponseEntity<?> findEmployeesAvailables() {
        return employeeService.findEmployeesAvailables();
    }

    @GetMapping("/countprojects/{id}")
    @Secured({"ROLE_MASTER"})
    public ResponseEntity<?> getNumberActiveProjectsByEmployee(@PathVariable("id") long id) {
        return employeeService.getNumberActiveProjectsByEmployee(id);
    }

    @PostMapping("")
    @Secured("ROLE_MASTER")
    public ResponseEntity<?> save(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @PutMapping("")
    @Secured("ROLE_MASTER")
    public ResponseEntity<?> update(@RequestBody Employee employee) {
        return employeeService.update(employee);
    }

    @PutMapping("/status/{id}")
    @Secured("ROLE_MASTER")
    public ResponseEntity<?> changeStatusEmployee(@PathVariable("id") long id) {
        return employeeService.changeStatusEmployee(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") long id) {
        return employeeService.deleteById(id);
    }

}
