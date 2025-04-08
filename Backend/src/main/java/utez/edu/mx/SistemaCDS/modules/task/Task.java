package utez.edu.mx.SistemaCDS.modules.task;

import jakarta.persistence.*;
import utez.edu.mx.SistemaCDS.modules.phase.Phase;
import utez.edu.mx.SistemaCDS.modules.project.Project;

import java.util.List;

@Entity
@Table(name = "task")
public class Task {

    // --------------- ATRIBUTOS PROPIOS DE LA CLASE ---------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    // Terminado = 1, En proceso = 0
    @Column(name = "status", nullable = false)
    private boolean status;


    // ------------------ ATRIBUTOS DE RELACION -------------------------

    @ManyToOne
    @JoinColumn(name = "id_project")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "id_phase", nullable = false)
    private Phase phase;


    // -----------------------CONSTRUCTORES------------------------------

    public Task() {
    }

    public Task(long id, String name, boolean status, Project project, Phase phase) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.project = project;
        this.phase = phase;
    }


    // ------------------- GETTERS AND SETTERS --------------------------

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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
