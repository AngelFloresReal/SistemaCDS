const URL = 'http://localhost:8080';
let tasks = [];
let task = {};
let project = {};
let employee = {};


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

const findEmployeeById = async (id) => {
    // Bloque de configuracion
    await fetch(`${URL}/api/employee/${id}`, {
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
        employee = response.data;
    })
    // Bloque de errores
    .catch(console.log);
}

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

const findTaskById = async (id) => {
    // Bloque de configuracion
    await fetch(`${URL}/api/task/${id}`, {
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
        task = response.data;
    })
    // Bloque de errores
    .catch(console.log);
}

const loadTableTask = async() => {
    await findProjectByEmployeeId(localStorage.getItem("idEmployee"));
    await findAllTasks(project.id, project.phase.id);
    let tbody = document.getElementById('taskBody');
    let content = '';

    tasks.forEach((item, index) => {
        content += `<tr>
                        <th scope="row">${index + 1}</th>
                        <td>${item.name}</td>
                        <td>${item.status ? "Terminada" : "En proceso"}</td>
                        <td class="text-center">
                            <button class="btn btn-primary btn-sm me-1" data-bs-toggle="modal" data-bs-target="#updateModal" onclick="setDataOnForm(${item.id})">Editar</button>
                        </td>
                    </tr>`;
    });
    tbody.innerHTML = content;

    // Se carga ahora la informacion de los datos del proyecto
    document.getElementById('nameProyectoDatos').innerHTML = project.name;
    document.getElementById('nameFaseDatos').innerHTML = project.phase.name;
}


const setDataOnForm = async id => {
    await findTaskById(id);

    document.getElementById("u_taskName").value = task.name;
    document.getElementById("u_taskStatus").checked = task.status;
}

const save = async () => {
    let form = document.getElementById('saveTaskForm');

    // Evita el envio del formulario si tiene elementos vacios
    form.onsubmit = async function (event) {
        
        task = {
            name: document.getElementById('taskName').value,
            phase: {
                id: project.phase.id
            },
            project:{
                id: project.id
            }
        };

        await fetch(`${URL}/api/task`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("authToken")}`
            },
            body: JSON.stringify(task)
        }).then(response => response.json()).then(async response => {
            console.log(response);
            task = {};
            form.reset();
            await loadTableTask();
    
        }).catch(console.log);

    }
    
}

const updateTask = async () => {
    let form = document.getElementById('updateTaskForm');

    // Evita el envio del formulario si tiene elementos vacios
    form.onsubmit = async function (event) {
        
        // Se hace la consulta de actualizacion del nombre
        taskNew = {
            id: task.id,
            name: document.getElementById('u_taskName').value,
            phase: {
                id: task.phase.id
            },
            project:{
                id: task.project.id
            }
        };

        await fetch(`${URL}/api/task`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("authToken")}`
            },
            body: JSON.stringify(taskNew)
        }).then(response => response.json()).then(async response => {
            console.log(response);
            taskNewk = {};
            task = {};
            form.reset();
            await loadTableTask();
    
        }).catch(console.log);

    }
    
}

const updateChangePhaseProject = async () => {
    
    // Se hace la consulta de actualizacion del nombre
    projectNewPhase = {
        id: project.id,
        phases:[
            {
                id: (project.phase.id + 1)
            }
        ]
    };

    await fetch(`${URL}/api/project/phases`, {
        method: 'PUT',
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("authToken")}`
        },
        body: JSON.stringify(projectNewPhase)
    }).then(response => response.json()).then(async response => {
        console.log(response);
        project = {};
        tasks = {};
        form.reset();
        await loadTableTask();

    }).catch(console.log);
    
}

// (Funcion anonima) Esta funcion se ejecuta una sola vez al cargar el javascript
(async () => {
    if(!localStorage.getItem("authToken") || localStorage.getItem("rolEmployee") !== 'RD'){
        window.location.href = "./../../view/error401.html";
    } 
    else {
        await loadTableTask();

        let tareasTerminadas = true;

        tasks.forEach((item, index) => {
            if(!item.status){
                tareasTerminadas = false;
            }
        });

        // Se determina que pasará con el boton de pasar a siguiente fase
        let botonSiguienteFase = document.getElementById('buttonNextPhaseProject');
        if(tasks.length === 0 || !tareasTerminadas){
            botonSiguienteFase.disabled = true;
        }
        else if(project.phase.name === "Cierre"){
            botonSiguienteFase.disabled = true;
        }
        else{
            botonSiguienteFase.disabled = false;
        }
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