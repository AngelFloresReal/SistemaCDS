package utez.edu.mx.SistemaCDS.modules.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import utez.edu.mx.SistemaCDS.modules.project.Project;
import utez.edu.mx.SistemaCDS.modules.rol.Rol;

import java.util.List;

@Entity
@Table(name = "employee")
public class Employee {

    // --------------- ATRIBUTOS PROPIOS DE LA CLASE ---------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;


    // ------------------ ATRIBUTOS DE RELACION -------------------------

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @ManyToMany(mappedBy = "employees")
    @JsonIgnore
    private List<Project> projects;


    // -----------------------CONSTRUCTORES------------------------------

    public Employee() {
    }

    public Employee(String name, String surname, String lastname, String email, boolean status, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.email = email;
        this.status = status;
        this.username = username;
        this.password = password;
    }

    public Employee(long id, String name, String surname, String lastname, String email, boolean status, String username, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.email = email;
        this.status = status;
        this.username = username;
        this.password = password;
    }

    public Employee(String name, String surname, String lastname, String email, boolean status, String username, String password, Rol rol, List<Project> projects) {
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.email = email;
        this.status = status;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.projects = projects;
    }

    public Employee(long id, String name, String surname, String lastname, String email, boolean status, String username, String password, Rol rol, List<Project> projects) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.email = email;
        this.status = status;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.projects = projects;
    }

    // ------------------- GETTERS AND SETTERS --------------------------

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

    public void setSurname(String surName) {
        this.surname = surName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

}
