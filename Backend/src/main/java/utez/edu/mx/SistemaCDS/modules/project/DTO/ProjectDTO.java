package utez.edu.mx.SistemaCDS.modules.project.DTO;


import utez.edu.mx.SistemaCDS.modules.employee.DTO.EmployeeProjectDTO;
import utez.edu.mx.SistemaCDS.modules.phase.Phase;
import utez.edu.mx.SistemaCDS.modules.task.Task;

import java.util.List;

// Plantilla para mandar los mismos atributos, pero el de relacion con Employee convertido a DTO
public class ProjectDTO {
    // ----------------- ATRIBUTOS DE LA CLASE --------------------------
    // Atributos normales de la clase Project
    private long id;
    private String name, identifier, startDate, endDate;
    private boolean status;

    //DTO de employee, para no mostrar usuario y contraseña
    private List<EmployeeProjectDTO> employees;
    private List<Task> tasks;
    private Phase phase;

    // --------------------- CONSTRUCTORES -------------------------------
    public ProjectDTO() {}

    public ProjectDTO(long id, String name, String identifier, String startDate, String endDate,
                      boolean status, List<EmployeeProjectDTO> employees, List<Task> tasks, Phase phase) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.employees = employees;
        this.tasks = tasks;
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

    public List<EmployeeProjectDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeProjectDTO> employees) {
        this.employees = employees;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
