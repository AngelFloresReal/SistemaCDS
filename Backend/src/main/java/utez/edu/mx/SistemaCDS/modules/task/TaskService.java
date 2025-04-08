package utez.edu.mx.SistemaCDS.modules.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.SistemaCDS.modules.project.ProjectService;
import utez.edu.mx.SistemaCDS.modules.task.DTO.TaskDTO;
import utez.edu.mx.SistemaCDS.modules.task.DTO.UnfinishedTasksListDTO;
import utez.edu.mx.SistemaCDS.utils.CustomResponseEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CustomResponseEntity customResponseEntity;

    // ------------------ METODOS DEL SERVICIO ---------------------------
    public TaskDTO transformTasksToTasksDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getName(),
                task.isStatus(),
                projectService.transformProjectToDTOForTask(task.getProject()),
                task.getPhase()
        );
    }

    // ------------------ METODOS DEL SERVICIO ---------------------------
    public UnfinishedTasksListDTO transformTasksToUnfinishedTasksDTO(List<TaskDTO> tasks) {
        return new UnfinishedTasksListDTO(
                tasks.get(0).getPhase().getId(),
                tasks.get(0).getPhase().getName(),
                tasks
        );
    }

    // --------------------- REGLAS DE NEGOCIO --------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<?> findAll() {
        List<Task> list = taskRepository.findAll();
        List<TaskDTO> taskDTOList = new ArrayList<>();

        // Se convierte la lista de proyectos a una de DTO sin tanta informacion
        for (Task task : list) {
            taskDTOList.add(transformTasksToTasksDTO(task));
        }

        return customResponseEntity.getOkResponse(list.isEmpty() ? "Aun no hay registros" : "Operacion exitosa", taskDTOList);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findById(long id) {
        Task found = taskRepository.findById(id);

        // Si no se encontró la tarea
        if(found != null){
            return customResponseEntity.getOkResponse("Operacion exitosa", found);

        } else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }

    }

    // Trae todas las tareas pendientes por hacer de una fase
    @Transactional(readOnly = true)
    public ResponseEntity<?> findUnfinishedTasks(long idProject) {
        List<Task> list = taskRepository.findTaskDontFinishedProject(idProject);
        UnfinishedTasksListDTO unfinishedTasksDTO= null; // Listas de tareas no terminadas
        List<TaskDTO> taskDTOList = new ArrayList<>(); // Lista de tareas como DTO

        if(!list.isEmpty()){
            // Se transforman todas las tareas a DTO
            for (Task task : list) {
                taskDTOList.add(transformTasksToTasksDTO(task));
            }

            //Se transforman las tareas a una lista DTO de tareas pendientes
            unfinishedTasksDTO = transformTasksToUnfinishedTasksDTO(taskDTOList);

            return customResponseEntity.getOkResponse("Operacion completada exitosamente", unfinishedTasksDTO);
        }
        else{
            return customResponseEntity.getOkResponse("Este proyecto tiene todas sus tareas terminadas", null);
        }

    }

    // Trae todas las tareas pendientes por hacer de una fase
    @Transactional(readOnly = true)
    public ResponseEntity<?> findTaskByPhaseProject(Task taskDetails) {
        List<Task> list = taskRepository.findTaskByPhaseProject(taskDetails.getProject().getId(), taskDetails.getPhase().getId());
        UnfinishedTasksListDTO taskBtPhaseProject = null; // Listas de tareas no terminadas
        List<TaskDTO> taskDTOList = new ArrayList<>(); // Lista de tareas como DTO

        if(!list.isEmpty()){
            // Se transforman todas las tareas a DTO
            for (Task task : list) {
                taskDTOList.add(transformTasksToTasksDTO(task));
            }

            //Se transforman las tareas a una lista DTO de tareas pendientes
            taskBtPhaseProject = transformTasksToUnfinishedTasksDTO(taskDTOList);

            return customResponseEntity.getOkResponse("Operacion completada exitosamente", taskBtPhaseProject);
        }
        else{
            return customResponseEntity.getOkResponse("Este proyecto no tiene tareas actualmente", null);
        }

    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> save(Task task) {
        Task saved = null;

        // Se guarda el registro en la base de datos
        try{
            task.setStatus(false);
            saved = taskRepository.save(task);
            return customResponseEntity.get201Response("Registro exitoso", null);

        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return customResponseEntity.get400Response("Error al realizar la operacion");
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> update(Task task) {
        Task found = taskRepository.findById(task.getId());

        // Primero se busca por id para realizar la actualizacion
        if(found != null){
            try {
                task.setStatus(found.isStatus()); // Se hace la actualizacion sin cambiar su estatus
                taskRepository.save(task);
                return customResponseEntity.getOkResponse("Actualizacion exitosa", null);

            } catch (Exception e){
                e.printStackTrace();
                System.out.println(e.getMessage());
                return customResponseEntity.get400Response("Error al realizar la actualizacion");
            }
        }
        else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }

    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> changeStatus(long id) {
        Task found = taskRepository.findById(id);
        String message = "";

        // Si no se encontró la tarea
        if(found != null){

            // Se hace la actualizacion del estatus
            if(found.isStatus()){
                taskRepository.updateChangesStatus(id, 0);
                message = "Tarea renovada exitosamente";
            }
            else{
                taskRepository.updateChangesStatus(id, 1);
                message = "Tarea finalizada exitosamente";
            }

            return customResponseEntity.getOkResponse(message, null);

        } else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> deleteById(long id){

        // Primero se busca por id para realizar la actualizacion
        if(taskRepository.findById(id) != null){
            try {
                taskRepository.deleteById(id);
                return customResponseEntity.getOkResponse("Registro eliminado correctamente", null);

            }catch (Exception e){
                e.printStackTrace();
                System.out.println(e.getMessage());
                return customResponseEntity.get400Response("Error al eliminar el registro");
            }
        }
        else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }
    }
}
