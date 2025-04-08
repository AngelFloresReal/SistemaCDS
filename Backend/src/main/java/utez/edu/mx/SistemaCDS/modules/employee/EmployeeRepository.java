package utez.edu.mx.SistemaCDS.modules.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAll();
    Employee findById(long id);
    Employee save(Employee employee);
    void deleteById(long id);

    // Buscar empleado por nombre de usuario y contraseña
    @Query(value = "SELECT e.*, r.name AS name_rol FROM employee e INNER JOIN rol r ON e.id_rol = r.id " +
            "WHERE e.password = :password " +
            "AND e.username = :username;",
            nativeQuery = true)
    Employee findByPasswordAndUsername(
            @Param("password") String password,
            @Param("username") String username
    );

    // Buscar empleado por nombre de usuario
    @Query(value = "SELECT * FROM employee WHERE username = :username", nativeQuery = true)
    Employee findByUsername(@Param("username") String username);

    // Buscar los RAPEs, asi como los RDs y los APs que no tienen asignado ningun proyecto
    @Query(value = "SELECT e.*, r.name AS name_rol FROM employee e LEFT JOIN project_has_employees pe ON e.id = pe.id_employee \n" +
            "INNER JOIN rol r ON e.id_rol = r.id\n" +
            "WHERE (pe.id_employee IS NULL && (e.id_rol = 3 || e.id_rol = 4)) || (e.id_rol = 2);", nativeQuery = true)
    List<Employee> findRAPEsRDsAndAPSavailables();

    @Query(value = "SELECT count(*) FROM project_has_employees WHERE id_employee = :idEmployee", nativeQuery = true)
    int getNumberActiveProjectsByEmployee(@Param("idEmployee") long idEmployee);

    // Invierte el estado del empleado
    @Modifying
    @Query(value = "UPDATE employee SET status = :estado WHERE id = :id", nativeQuery = true)
    void changeStatusEmployee(@Param("id") long id, @Param("estado") int estado);

}
