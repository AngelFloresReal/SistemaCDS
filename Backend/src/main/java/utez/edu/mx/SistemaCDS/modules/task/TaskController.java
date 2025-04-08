package utez.edu.mx.SistemaCDS.modules.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = {"*"})
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("")
    @Secured({"ROLE_RD", "ROLE_AP"})
    public ResponseEntity<?> findAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_RD", "ROLE_AP"})
    public ResponseEntity<?> findById(@PathVariable("id") long id) {
        return taskService.findById(id);
    }

    @GetMapping("/unfinished/{idProject}")
    @Secured({"ROLE_RD", "ROLE_AP"})
    public ResponseEntity<?> findUnfinishedTasks(@PathVariable long idProject) {
        return taskService.findUnfinishedTasks(idProject);
    }

    @PostMapping("/byphase")
    @Secured({"ROLE_RD", "ROLE_AP"})
    public ResponseEntity<?> findTaskByPhaseProject(@RequestBody Task task) {
        return taskService.findTaskByPhaseProject(task);
    }

    @PostMapping("")
    @Secured("ROLE_RD")
    public ResponseEntity<?> save(@RequestBody Task task) {
        return taskService.save(task);
    }

    @PutMapping("")
    @Secured("ROLE_RD")
    public ResponseEntity<?> update(@RequestBody Task task) {
        return taskService.update(task);
    }

    @PutMapping("/changesstatus/{idTask}")
    @Secured("ROLE_AP")
    public ResponseEntity<?> changeStatus(@PathVariable long idTask) {
        return taskService.changeStatus(idTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") long id) {
        return taskService.deleteById(id);
    }
}
