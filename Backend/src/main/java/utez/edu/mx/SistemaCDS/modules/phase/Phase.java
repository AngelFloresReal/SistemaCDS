package utez.edu.mx.SistemaCDS.modules.phase;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import utez.edu.mx.SistemaCDS.modules.project.Project;
import utez.edu.mx.SistemaCDS.modules.task.Task;

import java.util.List;

@Entity
@Table(name = "phase")
public class Phase {

    // --------------- ATRIBUTOS PROPIOS DE LA CLASE ---------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;


    // ------------------ ATRIBUTOS DE RELACION -------------------------

    @OneToMany(mappedBy = "phase")
    @JsonIgnore
    private List<Task> tasks;

    @ManyToMany(mappedBy = "phases")
    @JsonIgnore
    private List<Project> projects;


    // -----------------------CONSTRUCTORES------------------------------

    public Phase() {
    }

    public Phase(String name) {
        this.name = name;
    }

    public Phase(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Phase(String name, List<Task> tasks, List<Project> projects) {
        this.name = name;
        this.tasks = tasks;
        this.projects = projects;
    }

    public Phase(int id, String name, List<Task> tasks, List<Project> projects) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
        this.projects = projects;
    }


    // ------------------- GETTERS AND SETTERS --------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

}
