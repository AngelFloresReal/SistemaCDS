package utez.edu.mx.SistemaCDS.modules.project.DTO;

// Plantilla para mostrar los proyectos al seleccionar una tarea
public class ProjectTaskDTO {


    // ----------------- ATRIBUTOS DE LA CLASE --------------------------

    private long id;
    private String name, identifier;


    // --------------------- CONSTRUCTORES -------------------------------

    public ProjectTaskDTO() {
    }

    public ProjectTaskDTO(long id, String name, String identifier) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
