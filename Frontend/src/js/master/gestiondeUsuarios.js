const URL = 'http://localhost:8080';
let employees = [];
let employee = {};
let numberProjectsEmployee = 0;

const getNumberProjectsByEmployee = async (id) => {
    // Bloque de configuracion
    await fetch(`${URL}/api/employee/countprojects/${id}`, {
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
            numberProjectsEmployee = response.data;
        })
        // Bloque de errores
        .catch(console.log);
}

const saveNewEmployee = async () => {
    let form = document.getElementById('formRegistroUsuario');

    // Evita el envio del formulario si tiene elementos vacios
    form.onsubmit = async function (event) {


        // Se construye el objeto
        employee = {
            name: document.getElementById('userName').value,
            surname: document.getElementById('userSurname').value,
            lastname: document.getElementById('userLastname').value,
            email: document.getElementById('userEmail').value,
            status: document.getElementById('userStatus').value,
            rol: {
                id: document.getElementById('userRole').value,
            },
            username: document.getElementById('userUsername').value,
            password: document.getElementById('userPassword').value
        };

        await fetch(`${URL}/api/employee`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("authToken")}`
            },
            body: JSON.stringify(employee)
        }).then(response => response.json()).then(async response => {
            console.log(response);
            employee = {};
            form.reset();
            await loadTable();

        }).catch(console.log);

    }

}

const findAllEmployees = async () => {
    // Bloque de configuracion
    await fetch(`${URL}/api/employee`, {
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

// Funcion para desactivar empleado
const updateStatusEmployeeOff = async () => {
    let form = document.getElementById('formConfirmarDesactivarEmpleado');
    let idClient = document.getElementById('u_idEmployeeOff').value;

    await fetch(`${URL}/api/employee/status/${idClient}`, {
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

//Funcion para activar empleado
const updateStatusEmployeeOn = async () => {
    let form = document.getElementById('formConfirmarActivarEmpleado');
    let idClient = document.getElementById('u_idEmployeeOn').value;

    await fetch(`${URL}/api/employee/status/${idClient}`, {
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
const openModalStatusEmployee = (id, flag) => {
    // Seleccionar el modal por su id
    if(flag){
        const myModal = document.getElementById('modalConfirmarAccionActivar');
        document.getElementById('u_idEmployeeOn').value = id;

        // Abrir el modal
        myModal.focus();
    }
    else{
        const myModal = document.getElementById('modalConfirmarAccionDesactivar');
        document.getElementById('u_idEmployeeOff').value = id;

        console.log(myModal.id);

        // Abrir el modal
        myModal.focus();
    }

}

const loadTable = async () => {
    await findAllEmployees();
    let tbody = document.getElementById('tbody-table-employees');
    let content = '';

    console.log(employees);

    for (const [indexE, itemE] of employees.entries()) {
        // No imprime al master
        if (itemE.rol.name === "ROLE_MASTER") {
            continue; // Salta esta iteración
        }

        // Se obtiene el rol del empleado
        let rolE = itemE.rol.name;

        // Se invoca el número de proyectos en los que participa el empleado
        await getNumberProjectsByEmployee(itemE.id);

        content += `<tr>
                        <th scope="row">${indexE}</th>
                        <td>${itemE.name}</td>
                        <td>${itemE.email}</td>
                        <td>${rolE.slice(5)}</td>
                        <td class="text-center">${numberProjectsEmployee}</td>
                        <td><span class="badge ${itemE.status ? "bg-success" : "bg-danger"}">${itemE.status ? "Activo" : "Inactivo"}</span></td>
                        <td class="text-center">
                            ${itemE.status ?
                `<button class="btn btn-danger btn-sm" data-bs-toggle="modal"
                                data-bs-target="#modalConfirmarAccionDesactivar" onclick="openModalStatusEmployee(${itemE.id}, false)">Desactivar</button>` :
                `<button class="btn btn-success btn-sm" data-bs-toggle="modal"
                                data-bs-target="#modalConfirmarAccionActivar" onclick="openModalStatusEmployee(${itemE.id}, true)">Activar</button>` }
                        </td>
                    </tr>`;
    }

    tbody.innerHTML = content;
};


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
    employees = [];
    employee = {};
    numberProjectsEmployee = 0;

    // Se limpia todo el localStorage y se redirige al login
    localStorage.clear();
    window.location.href = "./../../../index.html";
}