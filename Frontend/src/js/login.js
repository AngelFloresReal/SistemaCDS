
const URL = 'http://localhost:8080';
let employee = {};


// Metodo que sirve para iniciar sesion
const login = async () => {
    let form = document.getElementById('loginForm');
    employee = {
        user: document.getElementById('email').value,
        password: document.getElementById('password').value,
    };

    await fetch(`${URL}/auth`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(employee)
    }).then(response => response.json()).then(async response => {
        // Se guarda el token, el id del usuario y su rol
        localStorage.setItem("authToken", response.token);
        localStorage.setItem("idEmployee", response.id);
        localStorage.setItem("rolEmployee", response.rol);

        console.log(response);
        employee = {};
        form.reset();

        switch(response.rol){
            case 'MASTER':
                window.location.href = "./src/view/master/masterInicio.html";
                break;
            case 'RAPE':
                window.location.href = "./src/view/rape/terminarProyectos.html";
                break;
            case 'RD':
                window.location.href = "./src/view/rd/rdTareas.html";
                break;
            case 'AP':
                window.location.href = "./src/view/ad/analista.html";
                break;
            default:
                // Mostrar un mensaje de error usando de javascript
                const modalRegistro = document.querySelector("#modalPasswordIncorrect");
                const myModal = new bootstrap.Modal(modalRegistro);
                myModal.show();

            }
    }).catch(console.log);
}

// (Funcion anonima) Esta funcion se ejecuta una sola vez al cargar el javascript
(async () => {
    if(localStorage.getItem("authToken") || localStorage.getItem("rolEmployee") || localStorage.getItem("idEmployee")){
        
        switch(localStorage.getItem("rolEmployee")){
            case 'MASTER':
                window.location.href = "./src/view/master/masterInicio.html";
                break;
            case 'RAPE':
                window.location.href = "./src/view/rape/terminarProyectos.html";
                break;
            case 'RD':
                window.location.href = "./src/view/rd/rdTareas.html";
                break;
            case 'AP':
                window.location.href = "./src/view/ad/analista.html";
                break;
            
        }
    }
})();
