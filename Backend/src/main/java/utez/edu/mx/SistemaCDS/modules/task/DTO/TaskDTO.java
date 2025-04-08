package utez.edu.mx.SistemaCDS.modules.task.DTO;

import utez.edu.mx.SistemaCDS.modules.phase.Phase;
import utez.edu.mx.SistemaCDS.modules.project.DTO.ProjectTaskDTO;

// Plantilla para mostrar menos datos en la lista de tareas
public class TaskDTO {


    // ----------------- ATRIBUTOS DE LA CLASE --------------------------
    // Atributos normales de la clase Task
    private long id;
    private String name;
    private boolean status;


    // Atributos de relacion
    private ProjectTaskDTO project;
    private Phase phase;


    // --------------------- CONSTRUCTORES -------------------------------

    public TaskDTO() {
    }

    public TaskDTO(long id, String name, boolean status, ProjectTaskDTO project, Phase phase) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.project = project;
        this.phase = phase;
    }


    // ------------------- GETTERS AND SETTERS ------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ProjectTaskDTO getProject() {
        return project;
    }

    public void setProject(ProjectTaskDTO project) {
        this.project = project;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
