package utez.edu.mx.SistemaCDS.modules.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAll();
    Task findById(long id);
    Task save(Task task);
    void deleteById(long id);

    // SELECCIONA LAS TAREAS DE EN UN PROJECTO
    @Query(value = "SELECT * FROM task WHERE id_project = :idProject ORDER BY id_phase ASC", nativeQuery = true)
    List<Task> findTasksByProject(@Param("idProject") long idProject);

    // SELECCIONA LAS TAREAS QUE ESTEN TERMINADAS EN UN PROJECTO
    @Query(value = "SELECT * FROM task WHERE id_project = :idProject AND status = 1", nativeQuery = true)
    List<Task> findTasksFinishedByProject(@Param("idProject") long idProject);

    // SELECCIONA LAS TAREAS QUE NO ESTEN TERMINADAS EN UN PROJECTO
    @Query(value = "SELECT * FROM task WHERE id_project = :idProject AND status = 0" +
            " ORDER BY id_phase ASC", nativeQuery = true)
    List<Task> findTaskDontFinishedProject(@Param("idProject") long idProject);

    // SELECCIONA LAS TAREAS QUE ESTAN TERMINADAS EN UN PROJECTO
    @Query(value = "SELECT * FROM task WHERE id_project = :idProject AND status = 0" +
            " ORDER BY id_phase DESC", nativeQuery = true)
    List<Task> findTaskFinishedProject(@Param("idProject") long idProject);

    // SELECCIONA LAS TAREAS QUE NO ESTEN TERMINADAS EN UN PROJECTO
    @Query(value = "SELECT * FROM task WHERE id_project = :idProject AND id_phase = :idPhase;", nativeQuery = true)
    List<Task> findTaskByPhaseProject(@Param("idProject") long idProject, @Param("idPhase") long idPhase);

    @Modifying
    @Query(value = "UPDATE task SET status = :estado WHERE id = :idTask", nativeQuery = true)
    void updateChangesStatus(@Param("idTask") long idTask, @Param("estado") int estado);

}
