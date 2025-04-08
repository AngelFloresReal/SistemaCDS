const URL = 'http://localhost:8080';
let projects = [];
let employees = [];
let employee = {};
let project = {};

const findAllProjects = async () => {
    // Bloque de configuracion
    await fetch(`${URL}/api/project`, {
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
            projects = response.data;
        })
        // Bloque de errores
        .catch(console.log);
}

const findById = async (id) => {
    // Bloque de configuracion
    await fetch(`${URL}/api/project/${id}`, {
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

// Funcion para desactivar empleado
const updateStatusProject = async () => {
    let form = document.getElementById('formConfirmarActivarEmpleado');
    let idProject = document.getElementById('u_idProjectOff').value;

    await fetch(`${URL}/api/project/changestatus/${idProject}`, {
        method: 'PUT',
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("authToken")}`
        },
    }).then(response => response.json()).then(async response => {
        console.log(response);
        await loadTable();
        form.reset();

    }).catch(console.log);
}

// Abre el modal dependiendo si se quiere activar o desactivar
const openModalFinishedProject = (id) => {
    const myModal = document.getElementById('modalConfirmarAccionFinalizar');
    document.getElementById('u_idProjectOff').value = id;

    // Abrir el modal
    myModal.focus();

}

const openModalInfoProject = async (id) => {
    // Inserta el id del proyecto en el modal
    const myModal = document.getElementById('modalMoreInfoProject');
    const bodyModal = document.getElementById('body-info-project');

    // Se busca la informacion del proyecto
    await findById(id);

    // Variables de concatenacion
    let contentEmployeesAssignated = '<p>    ';
    let content = '';

    // Se buscan los empleados
    if (project.employees) {

        // Concatenando las opciones como contenido
        project.employees.forEach(item => {

            if (item.rol === 'ROLE_RD') {
                contentEmployeesAssignated += `     • RD: ${item.name} ${item.surname} ${item.lastname}<br>`;
            }
            else if (item.rol === 'ROLE_AP') {
                contentEmployeesAssignated += `     • AP: ${item.name} ${item.surname} ${item.lastname}<br>`;
            }

        });

        contentEmployeesAssignated += `</p>`
    }

    // Se añaden los primeros atributos
    content += `<p><strong>Nombre: </strong>${project.name}</p>
                <p><strong>Identificador: </strong>${project.identifier}</p>
                <p><strong>Estado: </strong>${project.status ? "Finalizado" : "En proceso"}</p>`;

    // Se añade la fase por si no esta terminado
    if (project.status == false) {
        content += `<p><strong>Fase: </strong>${project.phase.name}</p>`;
    }

    // Se determina si hay algun equipo de desarrollo para agregar
    if (project.employees.length !== 0) {
        content += `<p><strong>Equipo de desarrollo: </strong></p>${contentEmployeesAssignated}`;
    }
    else{
        content += `<p><strong>Equipo de desarrollo: </strong><span>Sin asignar</span></p>`;
    }

    // Se adjunta la fecha de inicio
    content += `<p><strong>Fecha de inicio: </strong>${project.startDate}</p>`;

    // Se agrega fecha de finalizacion por si existe una
    if (project.status) {
        content += `<p><strong>Fecha de finalización: </strong>${project.endDate}</p>`;
    }

    // Añadir el contenido al cuerpo del modal
    bodyModal.innerHTML = content;
    project = {};

    // Abrir el modal
    myModal.focus();

}


const loadTable = async () => {
    await findAllProjects();
    let tbody = document.getElementById('tbody-table-projects');
    let content = '';

    projects.forEach((itemP, indexP) => {
        // Primero se busca quien es el encargado
        itemP.employees.forEach((itemE, indexE) => {
            if (itemE.rol === "ROLE_RAPE") {
                employee = itemE;
            }
        });

        content += `<tr>
                        <th scope="row">${indexP + 1}</th>
                        <td>${itemP.name}</td>
                        <td>${itemP.identifier}</td>
                        <td><span class="badge ${itemP.status ? "bg-success" : "bg-warning text-dark"}">${itemP.status ? "Finalizado" : itemP.phase.name}</span></td>
                        <td>${Object.values(employee).length === 0 ? "No definido" : `${employee.name} ${employee.surname} ${employee.lastname}`} </td>
                        <td class="text-center">
                            ${!itemP.status && itemP.phase.name === "Cierre" ? `<button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#modalConfirmarAccionFinalizar" onclick="openModalFinishedProject(${itemP.id})">Finalizar</button>` : ""}
                            <button class="btn btn-info btn-sm me-1" data-bs-toggle="modal" data-bs-target="#modalMoreInfoProject" onclick="openModalInfoProject(${itemP.id})">Ver más</button>
                        </td>
                    </tr>`;

        employee = {}; // Se limpia el dueño
    });

    employee = {}; // Se limpia ese uuario
    tbody.innerHTML = content;
}

// (Funcion anonima) Esta funcion se ejecuta una sola vez al cargar el javascript
(async () => {
    if (!localStorage.getItem("authToken") || localStorage.getItem("rolEmployee") !== 'RAPE') {
        window.location.href = "./../../view/error401.html";
    }
    else {
        await loadTable();
    }
})();

// Funcion para cerrar sesion
const cerrarSesion = () => {
    // Se limpian las demás variables
    projects = [];
    employees = [];
    employee = {};
    project = {};
    
    // Se limpia todo el localStorage y se redirige al login
    localStorage.clear();
    window.location.href = "./../../../index.html";
}