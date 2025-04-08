package utez.edu.mx.SistemaCDS.modules.employee.DTO;

// Plantilla para no mostrar mas datos al momento de cargar los proyectos
public class EmployeeProjectDTO {


    // ----------------- ATRIBUTOS DE LA CLASE --------------------------
    private long id;
    private String name, surname, lastname;
    private int idRol;
    private String rol;

    // --------------------- CONSTRUCTORES -------------------------------
    public EmployeeProjectDTO() {}

    public EmployeeProjectDTO(long id, String name, String surname, String lastname, int idRol, String rol) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.idRol = idRol;
        this.rol = rol;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
