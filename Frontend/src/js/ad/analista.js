const URL = 'http://localhost:8080';
let tasks = [];
let task = {};
let project = {};
let employee = {};

// Determina a que proyecto esta unido
const findProjectByEmployeeId = async (idEmployee) => {
    // Bloque de configuracion
    await fetch(`${URL}/api/project/employee/${idEmployee}`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("authToken")}`
        }
    })
    // Bloque de transformacion
    .then(response => response.json())
    // Bloque de manipulacion
    .then(response => {
        console.log(response);
        project = response.data;

    })
    // Bloque de errores
    .catch(console.log);
}

const updateTaskStatus = async (index) => {

    // Si el estatus seleccionado no coincide con el de la base de datos
    if(document.getElementById(`task-${index}`).checked !== tasks[index].status){

        await fetch(`${URL}/api/task/changesstatus/${tasks[index].id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("authToken")}`
            },
        }).then(response => response.json()).then(async response => {
            console.log(response);
            await loadTableTask();
        }).catch(console.log);

    }
    
}

const findAllTasks = async(idProject, idPhase) => {

    console.log("id project =" + idProject);
    console.log("id phase =" + idPhase);

    taskRequest = {
        phase: {
            id: idPhase
        },
        project:{
            id: idProject
        }
    };

    // Bloque de configuracion
    await fetch(`${URL}/api/task/byphase`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("authToken")}`
        },
        body: JSON.stringify(taskRequest)
    })
    // Bloque de transformacion
    .then(response => response.json())
    // Bloque de manipulacion
    .then(response => {
        console.log(response);
        tasks = response.data.tasks;
    })
    // Bloque de errores
    .catch(console.log);
}

const loadTableTask = async() => {
    
    await findProjectByEmployeeId(localStorage.getItem("idEmployee"));
    await findAllTasks(project.id, project.phase.id);
    let tbody = document.getElementById('list-task-project');
    let content = '';

    tasks.forEach((item, index) => {
        content += `<li class="list-group-item">
                        <input class="form-check-input me-3" type="checkbox" value="" id="task-${index}" ${item.status ? 'checked' : ''} ${item.status ? 'disabled' : `onclick="updateTaskStatus(${index})"`}>
                        <label class="form-check-label" for="firstCheckbox" id="task-${index + 1}">${item.name}</label>
                    </li>`;
    });
    tbody.innerHTML = content;

    // Se carga ahora la informacion de los datos del proyecto
    document.getElementById('nameProyectoDatos').innerHTML = project.name;
    document.getElementById('nameFaseDatos').innerHTML = project.phase.name;
}

// (Funcion anonima) Esta funcion se ejecuta una sola vez al cargar el javascript
(async () => {
    if(!localStorage.getItem("authToken") || localStorage.getItem("rolEmployee") !== 'AP'){
        window.location.href = "./../../view/error401.html";
    } 
    else {
        await loadTableTask();
    }
})();

// Funcion para cerrar sesion
const cerrarSesion = () => {
    localStorage.clear(); // Se limpia el localStorage

    // Se limpian las demas variable
    tasks = [];
    task = {};
    project = {};
    employee = {};

    // Se redirige a la nueva ventana
    window.location.href = "./../../../index.html";

}
