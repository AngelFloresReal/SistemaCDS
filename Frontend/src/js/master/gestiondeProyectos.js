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

const findAllEmployeesAvalaibles = async () => {
    // Bloque de configuracion
    await fetch(`${URL}/api/employee/availables`, {
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
            employees = response.data;
        })
        // Bloque de errores
        .catch(console.log);
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
                            <button class="btn btn-primary btn-sm me-1" data-bs-toggle="modal" data-bs-target="#modalActualizarProyecto" onclick="setDataOnFormEditProject(${itemP.id})">Editar</button>
                            ${itemP.employees.length === 0 ? `<button class="btn btn-success btn-sm me-1" data-bs-toggle="modal" data-bs-target="#modalAsignarProyecto" onclick="setDataOnFormAssifnEmployees(${itemP.id})">Asignar</button>` : ""}
                            <button class="btn btn-info btn-sm me-1" data-bs-toggle="modal" data-bs-target="#modalMoreInfoProject" onclick="openModalInfoProject(${itemP.id})">Ver más</button>
                        </td>
                    </tr>`;

        employee = {}; // Se limpia el dueño
    });

    employee = {}; // Se limpia ese uuario
    tbody.innerHTML = content;
}

const save = async () => {
    let form = document.getElementById('formRegistroProyecto');

    // Evita el envio del formulario si tiene elementos vacios
    form.onsubmit = async function (event) {

        project = {
            name: document.getElementById('nombreProyecto').value,
            identifier: document.getElementById('identificador').value,
        };

        await fetch(`${URL}/api/project`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("authToken")}`
            },
            body: JSON.stringify(project)
        }).then(response => response.json()).then(async response => {
            console.log(response);
            project = {};
            form.reset();
            await loadTable();

        }).catch(console.log);

    }

}

const asignarProyecto = async () => {
    let form = document.getElementById('formAsignarEmpleados');

    // Obtener las opciones seleccionadas del select multiple
    const multiSelect = document.getElementById("apAsignar");
    const selectedOptions = Array.from(multiSelect.options)
        .filter(option => option.selected);

    // Si se seleccionaron las 4 opciones
    if (selectedOptions.length === 4) {

        // Evita el envio del formulario si tiene elementos vacios
        form.onsubmit = async function (event) {

            // Se toman los valores del select
            const selectedValues = Array.from(multiSelect.options)
                .filter(option => option.selected)
                .map(option => option.value);

            // Se recorren los valores seleccionados para guardarlos
            let asignados = [];
            let contador = 0;
            selectedValues.forEach(value => {
                asignados.push(value);
                contador = contador + 1;
            });

            // Se construye el cuerpo del objeto
            projectWithEmployees = {
                id: document.getElementById('u_idProjectWithEmployees').value,
                employees: [
                    {
                        id: document.getElementById('rapeAsignar').value   //RAPE
                    },
                    {
                        id: document.getElementById('rdAsignar').value   //RD
                    },
                    {
                        id: asignados[0]   //AD
                    },
                    {
                        id: asignados[1]   //AD
                    },
                    {
                        id: asignados[2]   //AD
                    },
                    {
                        id: asignados[3]   //AD
                    }

                ]
            };

            await fetch(`${URL}/api/project/employees`, {
                method: 'PUT',
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("authToken")}`
                },
                body: JSON.stringify(projectWithEmployees)
            }).then(response => response.json()).then(async response => {
                console.log(response);
                project = {};
                form.reset();
                await loadTable();

            }).catch(console.log);

        }
    }

}

// Abre el modal dependiendo si se quiere activar o desactivar
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

            if (item.rol === 'ROLE_RAPE') {
                contentEmployeesAssignated += `     • RAPE: ${item.name} ${item.surname} ${item.lastname}<br>`;
            }
            else if (item.rol === 'ROLE_RD') {
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


const setDataOnFormEditProject = async id => {
    await findById(id);

    document.getElementById('u_idProject').value = project.id;
    document.getElementById('u_nombreProyecto').value = project.name;
    document.getElementById('u_identificador').value = project.identifier;
}

const setDataOnFormAssifnEmployees = async id => {
    await findById(id);
    await loadDataAssignEmployees();

    document.getElementById('nameProjectAssignEmployees').innerText = project.name;
    document.getElementById('u_idProjectWithEmployees').value = project.id;
}

const loadDataAssignEmployees = async () => {
    await findAllEmployeesAvalaibles(); // Armando la lista de employees
    let selectRAPEs = document.getElementById("rapeAsignar"); // Recuperando el select de RAPEs
    let selectRDs = document.getElementById("rdAsignar"); // Recuperando el select de RD
    let selectADs = document.getElementById("apAsignar"); // Recuperando el select de AD

    // Variables de concatenacion
    let contentRAPEs = '';
    let contentRDs = '';
    let contentADs = '';

    // Concatenando las opciones como contenido
    employees.forEach(item => {

        if (item.rol.name === 'ROLE_RAPE') {
            contentRAPEs += `<option value="${item.id}">${item.name} ${item.surname} ${item.lastname}</option>`;
        }
        else if (item.rol.name === 'ROLE_RD') {
            contentRDs += `<option value="${item.id}">${item.name} ${item.surname} ${item.lastname}</option>`;
        }
        else if (item.rol.name === 'ROLE_AP') {
            contentADs += `<option value="${item.id}">${item.name} ${item.surname} ${item.lastname}</option>`;
        }

    });

    // Añadir los contenidos a los selects
    selectRAPEs.innerHTML = contentRAPEs;
    selectRDs.innerHTML = contentRDs;
    selectADs.innerHTML = contentADs;

}

const update = async () => {
    let form = document.getElementById('formActualizacionProyecto');

    // Evita el envio del formulario si tiene elementos vacios
    form.onsubmit = async function (event) {

        let updated = {
            id: document.getElementById('u_idProject').value,
            name: nameProject = document.getElementById('u_nombreProyecto').value,
            identifier: identifierProject = document.getElementById('u_identificador').value
        }

        await fetch(`${URL}/api/project`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("authToken")}`
            },
            body: JSON.stringify(updated)
        }).then(response => response.json()).then(async response => {
            console.log(response);
            project = {};
            form.reset();
            await loadTable();

        }).catch(console.log);
    }

}

// (Funcion anonima) Esta funcion se ejecuta una sola vez al cargar el javascript
(async () => {
    if (!localStorage.getItem("authToken") || localStorage.getItem("rolEmployee") !== 'MASTER') {
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