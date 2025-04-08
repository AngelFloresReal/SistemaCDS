const URL = 'http://localhost:8080';
let tasks = [];

// Función genérica para manejar peticiones fetch
const fetchData = async (endpoint, method = 'GET', body = null) => {
    const config = {
        method,
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        }
    };
    if (body) config.body = JSON.stringify(body);
    try {
        const response = await fetch(`${URL}${endpoint}`, config);
        return await response.json();
    } catch (error) {
        console.log('Error:', error);
    }
};

const findAllTasks = async () => {
    const response = await fetchData('/api/task');
    if (response) {
        console.log(response);
        tasks = response.data;
    }
};

const loadTaskTable = async () => {
    await findAllTasks();
    const tbody = document.getElementById('taskBody');
    tbody.innerHTML = tasks.map((item, index) => `
        <tr>
            <th scope="row">${index + 1}</th>
            <td>${item.name}</td>
            <td>${item.status ? 'Completada' : 'Pendiente'}</td>
            <td class="text-center">
                <button class="btn btn-outline-danger" data-bs-target="#deleteModal" data-bs-toggle="modal" onclick="findTaskById(${item.id})">Eliminar</button>
                <button class="btn btn-outline-primary" data-bs-target="#updateModal" data-bs-toggle="modal" onclick="setTaskDataOnForm(${item.id})">Editar</button>
            </td>
        </tr>`).join('');
};

const saveTask = async () => {
    const form = document.getElementById('saveTaskForm');
    const task = {
        name: document.getElementById('taskName').value,
        status: document.getElementById('taskStatus').checked // Asume que es un checkbox
    };
    const response = await fetchData('/api/task', 'POST', task);
    if (response) {
        console.log(response);
        form.reset();
        await loadTaskTable();
    }
};

const findTaskById = async id => {
    const response = await fetchData(`/api/task/${id}`);
    if (response) {
        console.log(response);
        task = response.data;
    }
};

const setTaskDataOnForm = async id => {
    await findTaskById(id);
    document.getElementById('u_taskName').value = task.name;
    document.getElementById('u_taskStatus').checked = task.status;
};

const updateTask = async () => {
    const form = document.getElementById('updateTaskForm');
    const updated = {
        name: document.getElementById('u_taskName').value,
        status: document.getElementById('u_taskStatus').checked
    };
    const response = await fetchData(`/api/task/${task.id}`, 'PUT', updated);
    if (response) {
        console.log(response);
        form.reset();
        await loadTaskTable();
    }
};

const removeTask = async () => {
    const response = await fetchData(`/api/task/${task.id}`, 'DELETE');
    if (response) {
        console.log(response);
        await loadTaskTable();
    }
};

// Función anónima autoejecutable
(async () => {
    await loadTaskTable();
})();

// Funcion para cerrar sesion
const cerrarSesion = () => {
    // Se limpian las demás variables
    tasks = [];

    // Se limpia todo el localStorage y se redirige al login
    localStorage.clear();
    window.location.href = "./../../../index.html";
}