package utez.edu.mx.SistemaCDS.modules.employee.DTO;

import utez.edu.mx.SistemaCDS.modules.rol.Rol;

// Plantilla para no mostrar el usuario y contraseña
public class EmployeeDTO {

    // ----------------- ATRIBUTOS DE LA CLASE --------------------------
    private long id;
    private String name, surname, lastname, email;
    private boolean status;
    private Rol rol;

    // --------------------- CONSTRUCTORES -------------------------------
    public EmployeeDTO() {}

    public EmployeeDTO(long id, String name, String surname, String lastname, String email, boolean status, Rol rol) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.email = email;
        this.status = status;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
