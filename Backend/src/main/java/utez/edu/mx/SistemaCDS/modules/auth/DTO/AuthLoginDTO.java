package utez.edu.mx.SistemaCDS.modules.auth.DTO;

// 8.- Crear el DTO para el login
public class AuthLoginDTO {
    private String password, user;

    public AuthLoginDTO() {}

    public AuthLoginDTO(String password, String user) {
        this.password = password;
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
