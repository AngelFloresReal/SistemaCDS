package utez.edu.mx.SistemaCDS.modules.task.DTO;

import java.util.ArrayList;
import java.util.List;

// Esta clase determina en qué fase se está y devuelve las tareas pendientes de esa fase
public class UnfinishedTasksListDTO {

    // ----------------- ATRIBUTOS DE LA CLASE --------------------------
    // Atributos normales de la clase
    private int id_phase;
    private String phase;
    private List<TaskDTO> tasks;


    // --------------------- CONSTRUCTORES -------------------------------
    public UnfinishedTasksListDTO() {
    }

    public UnfinishedTasksListDTO(int id_phase, String phase, List<TaskDTO> tasks) {
        this.id_phase = id_phase;
        this.phase = phase;
        List<TaskDTO> tasksFiltred = new ArrayList<TaskDTO>();

        // Se hace el filtrado de tareas que corresponden a esa fase
        for (TaskDTO task : tasks) {
            if (task.getPhase().getName().equals(this.phase)) {
                tasksFiltred.add(task);
            }
        }

        this.tasks = tasksFiltred;
    }

    // ------------------- GETTERS AND SETTERS ------------------------
    public int getId_phase() {
        return id_phase;
    }

    public void setId_phase(int id_phase) {
        this.id_phase = id_phase;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }
}
