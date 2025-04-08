// (Funcion anonima) Esta funcion se ejecuta una sola vez al cargar el javascript
(async () => {
    if(!localStorage.getItem("authToken") || localStorage.getItem("rolEmployee") !== 'MASTER'){
        window.location.href = "./../../view/error401.html";
    }
})();

// Funcion para cerrar sesion
const cerrarSesion = () => {
    // Se limpia todo el localStorage y se redirige al login
    localStorage.clear();
    window.location.href = "./../../../index.html";
}