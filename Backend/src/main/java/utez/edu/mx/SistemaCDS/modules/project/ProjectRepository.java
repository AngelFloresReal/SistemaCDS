package utez.edu.mx.SistemaCDS.modules.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utez.edu.mx.SistemaCDS.modules.employee.Employee;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAll();
    Project findById(long id);
    Project save(Project project);
    void deleteById(long id);

    // Trae una lista de proyectos del empleado que no esten terminados
    @Query(value = "SELECT p.* FROM employee e INNER JOIN project_has_employees " +
            "pe ON e.id = pe.id_employee INNER JOIN project p " +
            "ON pe.id_project = p.id WHERE e.id = :idEmployee AND p.status = 0", nativeQuery = true)
    List<Project> findProjectsByEmployeeId(@Param("idEmployee") long idEmployee);

    // Trae el proyecto de un empleado
    @Query(value = "SELECT p.* FROM project p INNER JOIN project_has_employees pe ON p.id = pe.id_project\n" +
            "WHERE pe.id_employee = :idEmployee", nativeQuery = true)
    Project findProjectOfEmployee(@Param("idEmployee") long idEmployee);


    @Modifying
    @Query(value = "UPDATE project SET status = :estado WHERE id = :idPRoject", nativeQuery = true)
    void updateChangesStatus(@Param("idPRoject") long idPRoject, @Param("estado") int estado);
}
