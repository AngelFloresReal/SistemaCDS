package utez.edu.mx.SistemaCDS.modules.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import utez.edu.mx.SistemaCDS.modules.employee.Employee;
import utez.edu.mx.SistemaCDS.modules.phase.Phase;
import utez.edu.mx.SistemaCDS.modules.task.Task;

import java.util.List;

@Entity
@Table(name = "project")
public class Project {


    // --------------- ATRIBUTOS PROPIOS DE LA CLASE ---------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "identifier", nullable = false)
    private String identifier;

    @Column(name = "start_date", nullable = false)
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    // Terminado = 1, en proceso = 0
    @Column(name = "status", nullable = false)
    private boolean status;


    // ------------------ ATRIBUTOS DE RELACION -------------------------

    @ManyToMany
    @JoinTable(
            name = "project_has_employees",
            joinColumns = @JoinColumn(name = "id_project"),
            inverseJoinColumns = @JoinColumn(name = "id_employee")
    )
    private List<Employee> employees;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(
            name = "project_has_phases",
            joinColumns = @JoinColumn(name = "id_project"),
            inverseJoinColumns = @JoinColumn(name = "id_phase")
    )
    private List<Phase> phases;


    // ---------------------------CONSTRUCTORES-----------------------

    public Project() {
    }

    public Project(String name, String identifier, String startDate, String endDate, boolean status) {
        this.name = name;
        this.identifier = identifier;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Project(long id, String name, String identifier, String startDate, String endDate, boolean status) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Project(String name, String identifier, String startDate, String endDate, boolean status, List<Employee> employees, List<Task> tasks, List<Phase> phases) {
        this.name = name;
        this.identifier = identifier;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.employees = employees;
        this.tasks = tasks;
        this.phases = phases;
    }

    public Project(long id, String name, String identifier, String startDate, String endDate, boolean status, List<Employee> employees, List<Task> tasks, List<Phase> phases) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.employees = employees;
        this.tasks = tasks;
        this.phases = phases;
    }


    // ------------------------ GETTERS AND SETTERS ----------------

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }
}
