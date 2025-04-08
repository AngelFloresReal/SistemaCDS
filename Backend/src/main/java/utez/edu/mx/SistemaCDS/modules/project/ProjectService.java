package utez.edu.mx.SistemaCDS.modules.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.SistemaCDS.modules.employee.DTO.EmployeeProjectDTO;
import utez.edu.mx.SistemaCDS.modules.employee.Employee;
import utez.edu.mx.SistemaCDS.modules.employee.EmployeeRepository;
import utez.edu.mx.SistemaCDS.modules.employee.EmployeeService;
import utez.edu.mx.SistemaCDS.modules.phase.Phase;
import utez.edu.mx.SistemaCDS.modules.project.DTO.ProjectDTO;
import utez.edu.mx.SistemaCDS.modules.project.DTO.ProjectTaskDTO;
import utez.edu.mx.SistemaCDS.modules.task.Task;
import utez.edu.mx.SistemaCDS.modules.task.TaskRepository;
import utez.edu.mx.SistemaCDS.utils.CustomResponseEntity;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomResponseEntity customResponseEntity;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TaskRepository taskRepository;

    // ------------------ METODOS DEL SERVICIO ---------------------------
    // Metodo de transformacion de la lista de usuarios en el proyecto, para que contenga menos informacion
    public ProjectDTO transformProjectToDto(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getIdentifier(),
                project.getStartDate(),
                project.getEndDate(),
                project.isStatus(),
                employeeService.transformEmployeeListToDTOForProject(project.getEmployees()),
                project.getTasks(),
                project.getPhases().get(project.getPhases().size() - 1)
        );
    }

    public ProjectTaskDTO transformProjectToDTOForTask(Project project) {
        return new ProjectTaskDTO(
                project.getId(),
                project.getName(),
                project.getIdentifier()
        );
    }

    // --------------------- REGLAS DE NEGOCIO --------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<?> findAll() {
        List<Project> list = projectRepository.findAll();
        List<ProjectDTO> projectDTOList = new ArrayList<>();

        // Se convierte la lista de proyectos a una de DTO sin tanta informacion
        for (Project project : list) {
            projectDTOList.add(transformProjectToDto(project));
        }

        return customResponseEntity.getOkResponse(projectDTOList.isEmpty() ? "Aun no hay registros" : "Operacion exitosa", projectDTOList);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findById(long id) {
        Project found = projectRepository.findById(id);

        // Si no se encontro el proyecto
        if (found != null) {
            ProjectDTO projectDTO = transformProjectToDto(found); // Se transforma a DTO

            return customResponseEntity.getOkResponse("Operacion exitosa", projectDTO);

        } else {

            return customResponseEntity.get404Response("Registro no encontrado");
        }

    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findByEmployeeId(long employeeId) {
        Project found = projectRepository.findProjectOfEmployee(employeeId);

        // Si no se encontro el proyecto
        if (found != null) {
            ProjectDTO projectDTO = transformProjectToDto(found); // Se transforma a DTO

            return customResponseEntity.getOkResponse("Operacion exitosa", projectDTO);

        } else {

            return customResponseEntity.get404Response("Registro no encontrado");
        }

    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> save(Project project) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", new Locale("es-MX"));
        Project saved = null;

        // Se guarda el registro en la base de datos
        try {
            // Se le agrega por defecto la fase de inicio
            List<Phase> phases = new ArrayList<>();
            phases.add(new Phase(1, "Inicio"));

            project.setPhases(phases);

            // Se le agrega por defecto que el proyecto esté en proceso
            project.setStatus(false);

            // Se pone la fecha de inicio
            Date currentDay = new Date();
            project.setStartDate(sdf.format(currentDay));

            // Se pone la fecha de finalizacion como null
            project.setEndDate(null);

            // Se hace la actualizacion
            saved = projectRepository.save(project);
            return customResponseEntity.get201Response("Registro exitoso", null);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return customResponseEntity.get400Response("Error al realizar la operacion");
        }
    }

    // Actualizar unicamente la informacion de un proyecto, sin sus empleados, fases o estado
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> update(Project project) {
        Project found = projectRepository.findById(project.getId());

        // Primero se busca por id para realizar la actualizacion
        if (found != null) {

            // Validacion para que no se cambie las relaciones con empleados
            if (found.getEmployees() != null && project.getEmployees() == null){
                project.setEmployees(found.getEmployees());
            }

            try {
                project.setStatus(found.isStatus()); // Se pone el estatus del proyecto por si acaso
                project.setPhases(found.getPhases()); // Se ponen las fases del proyecto otra vez por si acaso
                project.setEmployees(found.getEmployees()); // Se pone los empleados asignados otra vez por si acaso
                project.setStartDate(found.getStartDate()); // Se pone la fecha de inicio
                project.setEndDate(found.getEndDate()); // Se pone la fecha de finalizacion

                Project updated = projectRepository.save(project);
                return customResponseEntity.getOkResponse("Actualizacion exitosa", null);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return customResponseEntity.get400Response("Error al realizar la actualizacion");
            }
        } else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }
    }

    // Actualizar un proyecto cambiando de fase
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> updateWithPhases(Project project) {
        String[] fases = {"Inicio", "Planeacion", "Ejecucion", "Control", "Cierre"};

        Project found = projectRepository.findById(project.getId());
        System.out.println("id project = " + project.getId());

        // Primero se busca por id para realizar la actualizacion
        if (found != null) {
            int idPhaseNew = 0;

            // Se obtiene el ultimo id de la nueva fase que se quiere agregar
            for (Phase phase : project.getPhases()) {
                if(idPhaseNew < phase.getId()){
                    idPhaseNew = phase.getId();
                }
            }

            System.out.println("Nueva fase id = " + idPhaseNew);

            // Osea si se quiere agregar una nueva fase que no corresponda a la siguiente fase cronologicamente
            if(idPhaseNew < found.getPhases().size() || idPhaseNew == found.getPhases().size() || idPhaseNew > found.getPhases().size()+1 ){
                System.out.println("\n\nLa fase nueva que se quiere agregar es incorrecta, la fase actual es ");
                return customResponseEntity.get400Response("La fase nueva que se quiere agregar es incorrecta, la fase actual es "
                        + found.getPhases().get(found.getPhases().size() - 1).getName());

            }


            // VERIFICACION DE QUE TODAS LAS TAREAS DE ESA FASE ESTEN COMPLETADAS
            List<Task> tasksNotFinished = taskRepository.findTaskDontFinishedProject(project.getId());

            // Se recorre la lista de tareas no finalizadas para verificar si hay alguna que no este terminada en esta fase
            for (Task task : tasksNotFinished) {
                if (task.getPhase().getId() == found.getPhases().get(found.getPhases().size()-1).getId()){
                    System.out.println("\n\nError al realizar la actualizacion, existen tareas pendientes por terminar en la fase de " + task.getPhase().getName());
                    return customResponseEntity.get400Response("Error al realizar la actualizacion, existen tareas pendientes por terminar en la fase de" + task.getPhase().getName());
                }
            }

            try {
                project.setName(found.getName()); // Se pone el nombre
                project.setIdentifier(found.getIdentifier()); // Se pone el identificador
                project.setStatus(found.isStatus()); // Se pone el estatus del proyecto por si acaso
                project.setEmployees(found.getEmployees()); // Se pone los empleados asinados otra vez por si acaso
                project.setStartDate(found.getStartDate()); // Se pone la fecha de inicio
                project.setEndDate(found.getEndDate()); // Se pone la fecha de finalizacion

                found.getPhases().add(new Phase(idPhaseNew, fases[idPhaseNew - 1]));
                project.setPhases(found.getPhases()); // Se le ponen las anteriores fases con la que ya esta incluida
                Project updated = projectRepository.save(project);

                System.out.println("\n\nActualizacion exitosa");
                return customResponseEntity.getOkResponse("Actualizacion exitosa", null);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());

                System.out.println("\n\nError al realizar la actualizacion");
                return customResponseEntity.get400Response("Error al realizar la actualizacion");
            }
        } else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }
    }


    // Actualizar un proyecto agregandole los empleados
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> updateWithEmployees(Project project) {
        Project found = projectRepository.findById(project.getId());

        // Numero de personas que se necesitan para un proyecto
        final int RESPONSABLES_PROYECTO = 1, RESPONSABLES_DESARROLLO = 1, ANALISTAS_PROGRAMADORES = 4;
        int contRAPE = 0, contRD = 0, contAP = 0;
        String message = "";
        boolean error = false;

        // Se busca por id para realizar la actualizacion
        if (found != null) {
            List<Employee> employeesProject = new ArrayList<>();

            // Primero se busca la informacion completa de todos los usuarios
            for (Employee employee : project.getEmployees()){
                employeesProject.add(employeeRepository.findById(employee.getId()));
            }

            // Luego se verifica que todos los empleados tengan sus proyectos terminados
            for (Employee employee : employeesProject){

                List<Project> projectsOfEmployee = projectRepository.findProjectsByEmployeeId(employee.getId());

                // Si no esta vacia, quiere decir que tiene proyectos pendientes
                if(!projectsOfEmployee.isEmpty()){
                    message = employee.getName() + " tiene un proyecto sin terminar: " + projectsOfEmployee.get(0).getName();
                    error = true;
                }
            }

            if(!error){

                // Se recorre la lista para verificar que no existan mas empleados de los requeridos
                for (Employee employee : employeesProject) {
                    // Se completa el employee para comparar su rol
                    String rolName = employeeRepository.findById(employee.getId()).getRol().getName();

                    if (rolName.equalsIgnoreCase("ROLE_RAPE")) {
                        contRAPE++;
                        if (contRAPE > RESPONSABLES_PROYECTO) {
                            message = "Error de actualizacion, Ya se ha asignado un responsable de proyecto";
                            error = true;
                            break;
                        }
                    } else if (rolName.equalsIgnoreCase("ROLE_RD")) {
                        contRD++;
                        if (contRD > RESPONSABLES_DESARROLLO) {
                            message = "Error de actualizacion, Ya se ha asignado un responsable de desarrollo";
                            error = true;
                            break;
                        }
                    } else if (rolName.equalsIgnoreCase("ROLE_AP")) {
                        contAP++;
                        if (contAP > ANALISTAS_PROGRAMADORES) {
                            message = "Error de actualizacion, Ya se han asignado los analistas programadores necesarios";
                            error = true;
                            break;
                        }
                    }
                }
            }

            // Si no hubo algun error, se realiza la actualizacion
            if (!error) {
                try {
                    project.setStatus(found.isStatus()); // Se pone el estatus del proyecto por si acaso
                    project.setIdentifier(found.getIdentifier()); // Se pone el identificador
                    project.setName(found.getName()); // Se pone el nombre
                    project.setPhases(found.getPhases()); // Se ponen las fases del proyecto otra ves por si acaso
                    project.setStartDate(found.getStartDate()); // Se pone la fecha de inicio
                    project.setEndDate(found.getEndDate()); // Se pone la fecha de finalizacion
                    Project updated = projectRepository.save(project);
                    return customResponseEntity.getOkResponse("Actualizacion exitosa", null);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    return customResponseEntity.get400Response("Error al realizar la actualizacion");
                }

            } else {
                return customResponseEntity.get400Response(message);
            }

        } else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }
    }

    // Actualizar el estatus de un proyecto, de en proceso a terminado (unicamente cambia el estatus)
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> updateChangesStatus(long id) {
        Project found = projectRepository.findById(id); // Se obtiene el proyecto que se va a cambiar su estatus
        String message = "";
        // Formato para fechas
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", new Locale("es-MX"));
        Date currentDay = new Date();

        // Primero se busca por id para realizar la actualizacion
        if (found != null) {

            List<Task> tasksProject = taskRepository.findTasksByProject(id); // Lista de tareas de un proyecto
            List<Task> taskFinishedProject = taskRepository.findTasksFinishedByProject(id); // Lista de tareas finalizadas por proyecto


            // VERIFICACION DE QUE TODAS LAS TAREAS DE ESA FASE ESTEN COMPLETADAS
            List<Task> tasksNotFinished = taskRepository.findTaskDontFinishedProject(id);

            // Se recorre la lista de tareas no finalizadas para verificar si hay alguna que no este terminada en esta fase
            for (Task task : tasksNotFinished) {
                if (task.getPhase().getId() == found.getPhases().get(found.getPhases().size()-1).getId()){
                    return customResponseEntity.get400Response("Error al realizar la actualizacion, existen tareas pendientes por terminar en la fase de");
                }
            }

            System.out.println("Tamaño de tareas del proyecto = " + tasksProject.size());
            System.out.println("Tamaño de tareas del finalizadas proyecto = " + taskFinishedProject.size());

            // Se verifica que en la ultima fase no existan tareas sin terminar
            if(found.isStatus() == false &&
                    (tasksProject.size() != taskFinishedProject.size())){
                return customResponseEntity.get400Response("Error, existen proyectos en la fase de Cierre sin terminar");
            }

            // Se cambia el status dependiendo de como esté y la fecha de finalizacion
            if(!found.isStatus()){
                found.setStatus(true);
                found.setEndDate(sdf.format(currentDay));
                message = "Proyecto finalizado correctamente";
            }
            else{
                found.setStatus(false);
                found.setEndDate(null);
                message = "Proyecto reabierto correctamente";
            }

            // Se hace la actualizacion del estado
            try {
                found.setEmployees(null); // Se liberan a los empleados
                projectRepository.save(found); // Se actualiza el estatus del proyecto y los empleados

                return customResponseEntity.getOkResponse(message, null);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return customResponseEntity.get400Response("Error al realizar la actualizacion del estado del proyecto");
            }
        } else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> deleteById(long id) {

        if (projectRepository.findById(id) != null) {
            try {
                projectRepository.deleteById(id);
                return customResponseEntity.getOkResponse("Registro eliminado correctamente", null);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return customResponseEntity.get400Response("Error al eliminar el registro");
            }
        } else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }
    }
}