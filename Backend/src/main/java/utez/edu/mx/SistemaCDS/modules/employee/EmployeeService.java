package utez.edu.mx.SistemaCDS.modules.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.SistemaCDS.modules.employee.DTO.EmployeeDTO;
import utez.edu.mx.SistemaCDS.modules.employee.DTO.EmployeeProjectDTO;
import utez.edu.mx.SistemaCDS.utils.CustomResponseEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomResponseEntity customResponseEntity;



    // -------------------- METODOS DEL SERVICIO -----------------------------

    //Se transforma la lista de empleados para que no muestre usuario y contraseña al usuario
    public EmployeeDTO transformUserToDTO(Employee employee) {
        return new EmployeeDTO (
                employee.getId(),
                employee.getName(),
                employee.getSurname(),
                employee.getLastname(),
                employee.getEmail(),
                employee.isStatus(),
                employee.getRol()
        );
    }

    // Se transforma la lista de empleado para que muestre menos datos al mostrar un proyecto
    public List<EmployeeProjectDTO> transformEmployeeListToDTOForProject(List<Employee> employees) {
        List<EmployeeProjectDTO> employeeProjectDTOs = new ArrayList<>();
        for (Employee employee : employees) {
            employeeProjectDTOs.add( new EmployeeProjectDTO(
                    employee.getId(),
                    employee.getName(),
                    employee.getSurname(),
                    employee.getLastname(),
                    employee.getRol().getId(),
                    employee.getRol().getName()
            ) );
        }
        return employeeProjectDTOs;
    }

    // --------------------- REGLAS DE NEGOCIO --------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<?> findAll() {
        List<Employee> list = employeeRepository.findAll();
        List<EmployeeDTO> dtos = new ArrayList<>();
        String message = "";

        // Si la lista no esta vacia, se transforma a DTO
        if (!list.isEmpty() && !(list == null)) {
            for (Employee employee : list) {
                dtos.add(transformUserToDTO(employee));
            }
            message = "Operacion exitosa";
        } else {
            message = "Aun no hay registros";
        }

        return customResponseEntity.getOkResponse(message, dtos);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findById(long id) {
        Employee found = employeeRepository.findById(id);
        EmployeeDTO dto = null;

        // Si no se encontro un employee
        if(found != null){
            dto = transformUserToDTO(found);
            return customResponseEntity.getOkResponse("Operacion exitosa", dto);

        } else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }

    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findEmployeesAvailables() {
        List<Employee> list = employeeRepository.findRAPEsRDsAndAPSavailables();
        List<EmployeeDTO> dtos = new ArrayList<>();
        String message = "";

        // Si la lista no esta vacia, se transforma a DTO
        if (!list.isEmpty() && !(list == null)) {
            for (Employee employee : list) {
                dtos.add(transformUserToDTO(employee));
            }
            message = "Operacion exitosa";
        } else {
            message = "No existe personal disponible";
        }

        return customResponseEntity.getOkResponse(message, dtos);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getNumberActiveProjectsByEmployee(long id) {
        int numProjects = employeeRepository.getNumberActiveProjectsByEmployee(id);

        // Si el registro se encuentra, hace el conteo de proyectos en los que participa
        if (employeeRepository.findById(id) != null) {
            return customResponseEntity.getOkResponse("Operacion exitosa", numProjects);
        } else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }

    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> save(Employee employee) {
        Employee saved = null;

        // Se guarda el registro en la base de datos
        try{
            saved = employeeRepository.save(employee);
            return customResponseEntity.get201Response("Registro exitoso", null);

        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return customResponseEntity.get400Response("Error al realizar la operacion");
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> update(Employee employee) {
        Employee updated = null;

        // Primero se busca por id para realizar la actualizacion
        if(employeeRepository.findById(employee.getId()) != null){
            try {
                updated = employeeRepository.save(employee);
                return customResponseEntity.getOkResponse("Actualizacion exitosa", null);

            } catch (Exception e){
                e.printStackTrace();
                System.out.println(e.getMessage());
                return customResponseEntity.get400Response("Error al realizar la actualizacion");
            }
        }
        else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> changeStatusEmployee(long id){
        // Primero se busca el registro
        Employee found = employeeRepository.findById(id);
        if(found == null){
            return customResponseEntity.get404Response("Registro no encontrado");
        }
        else {
            try {
                String message = "";
                // Si se encontro el registro, cambia el status al registro contrario
                if(found.isStatus()){
                    employeeRepository.changeStatusEmployee(id, 0);
                    message = "Empleado deshabilitado correctamente";
                }
                else {
                    employeeRepository.changeStatusEmployee(id, 1);
                    message = "Empleado habilitado correctamente";
                }

                return  customResponseEntity.getOkResponse(message, null);
            } catch (Exception e){
                e.printStackTrace();
                System.out.println(e.getMessage());
                return customResponseEntity.get400Response("Error al realizar la actualizacion del estado");
            }
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> deleteById(long id){

        if(employeeRepository.findById(id) != null){
            try {
                employeeRepository.deleteById(id);
                return customResponseEntity.getOkResponse("Registro eliminado correctamente", null);

            }catch (Exception e){
                e.printStackTrace();
                System.out.println(e.getMessage());
                return customResponseEntity.get400Response("Error al eliminar el registro");
            }
        }
        else {
            return customResponseEntity.get404Response("Registro no encontrado");
        }
    }

}
