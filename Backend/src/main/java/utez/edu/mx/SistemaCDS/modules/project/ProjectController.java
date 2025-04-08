package utez.edu.mx.SistemaCDS.modules.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins = {"*"})
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("")
    @Secured({"ROLE_MASTER", "ROLE_RAPE"})
    public ResponseEntity<?> findAll() {
        return projectService.findAll();
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_RAPE","ROLE_MASTER", "ROLE_AP"})
    public ResponseEntity<?> findById(@PathVariable("id") long id) {
        return projectService.findById(id);
    }

    @GetMapping("/employee/{id}")
    @Secured({"ROLE_RD", "ROLE_AP"})
    public ResponseEntity<?> findByEmployeeId(@PathVariable("id") long employeeId) {
        return projectService.findByEmployeeId(employeeId);
    }

    @PostMapping("")
    @Secured("ROLE_MASTER")
    public ResponseEntity<?> save(@RequestBody Project project) {
        return projectService.save(project);
    }

    @PutMapping("")
    @Secured("ROLE_MASTER")
    public ResponseEntity<?> update(@RequestBody Project project) {
        return projectService.update(project);
    }

    @PutMapping("/phases")
    @Secured({"ROLE_MASTER", "ROLE_RD"})
    public ResponseEntity<?> updateWithPhases(@RequestBody Project project) {
        return projectService.updateWithPhases(project);
    }

    @PutMapping("/employees")
    @Secured("ROLE_MASTER")
    public ResponseEntity<?> updateWithEmployees(@RequestBody Project project) {
        return projectService.updateWithEmployees(project);
    }

    @PutMapping("/changestatus/{id}")
    @Secured("ROLE_RAPE")
    public ResponseEntity<?> updateChangesStatus(@PathVariable("id") long id) {
        return projectService.updateChangesStatus(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") long id) {
        return projectService.deleteById(id);
    }
}
