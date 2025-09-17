<script setup>
import {onMounted, ref} from "vue";
import taskService from "@/services/tasksService.js";
import MyButton from "@/components/UI/MyButton.vue";
import TaskForm from "@/components/tasks/TaskForm.vue";

const tasks = ref([])
const loading = ref(false)
const error = ref(null)
const showTaskForm = ref(false)
const editingTask = ref(null)
const addNewTask = () => {
  editingTask.value = null
  showTaskForm.value = true
}
const fetchTasks = async () => {
  loading.value = true
  error.value = null
  try {
    const result = await taskService.getAllTasks()
    if (result.success) {
      tasks.value = result.data
    } else {
      error.value = result.error
    }
  } catch (err) {
    console.log(err)
  } finally {
    loading.value = false
  }
}
const handleTaskSubmit = async (taskData) => {
  try {
    let result

    if (editingTask.value && editingTask.value.id) {
      result = await taskService.editTask(editingTask.value.id, taskData)

      if (result.success) {
        const taskIndex = tasks.value.findIndex(t => t.id === editingTask.value.id)
        if (taskIndex !== -1) {
          tasks.value[taskIndex] = { ...tasks.value[taskIndex], ...result.data }
        }
        showTaskForm.value = false
        editingTask.value = null
      } else {
        error.value = result.error
      }
    } else {
      result = await taskService.createTask(taskData)

      if (result.success) {
        tasks.value.push(result.data)
        showTaskForm.value = false
      } else {
        error.value = result.error
      }
    }
  } catch (err) {
    console.error('Failed to save task:', err)
    error.value = 'Failed to save task'
  }
}
const createTask = handleTaskSubmit

// const toggleTaskStatus = async (taskId) => {
//   console.log("All tasks:", tasks.value);
//   console.log("Task IDs:", tasks.value.map(t => t.id));
//   console.log("Toggling task with id:", taskId);
//   loading.value = true
//   error.value = null
//   try {
//     console.log("Toggling task with id:", taskId);
//     const result = await taskService.toggleTask(taskId)
//     if (result.success) {
//       console.log(tasks.value.map(t => t.id))
//       const taskIndex = tasks.value.findIndex((t) => t.id === taskId);
//       if (taskIndex !== -1) {
//         tasks.value[taskIndex] = {...tasks.value[taskIndex], ...result.data};
//       }
//     } else {
//       error.value = result.error
//     }
//   } catch (err) {
//     console.log(err)
//   } finally {
//     loading.value = false
//   }
// }
const deleteTask = async (taskId) => {
  if (!confirm('Are you sure you want to delete this task?')) return

  try {
    const res = await taskService.deleteTask(taskId)
    if (res.success) {
      tasks.value = tasks.value.filter(t => t.id !== taskId)
    } else {
      error.value = res.error
    }
  } catch (err) {
    console.error(err)
  }
}
const editTask = (taskId) => {
  console.log('Edit task: ', taskId)
  const task = tasks.value.find(t => t.id === taskId)
  if (task) {
    editingTask.value = { ...task }
    showTaskForm.value = true
  }
}
const closeTaskForm = () => {
  showTaskForm.value = false
  editingTask.value = null
}
const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}
onMounted(() => {
  fetchTasks()
})
</script>

<template>
  <div class="task-list">
    <div class="task-header">
      <h2>My tasks</h2>
      <!--      TODO-->
      <my-button @click="addNewTask">+ Add New Task</my-button>
    </div>
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      Loading tasks...
    </div>
    <div v-else-if="error" class="error-state">{{ error }}</div>
    <div v-else-if="tasks.length === 0" class="empty-state">
      <h3>No tasks yet</h3>
      <p>Create your first task to get started!</p>
    </div>
    <div v-else class="tasks-container">
      <div v-for="task in tasks" :key="task.id" class="task-card">
        <div class="task-main">
          <div class="task-info">
            <h3 class="task-title" :class="{ completed: task.completed }">
              {{ task.title }}
            </h3>
            <p v-if="task.description" class="task-description">
              {{ task.description }}
            </p>

            <div class="task-meta">
              <span v-if="task.dueDate" class="due-date">
                📅 {{ formatDate(task.dueDate) }}
              </span>
              <span class="created-date">
                Created: {{ formatDate(task.createdAt) }}
              </span>
            </div>
          </div>

          <div class="task-badges">
            <span class="status-badge" :class="{ completed: task.completed }">
              {{ task.completed ? 'Done' : 'Pending' }}
            </span>
            <span class="priority-badge" :class="`priority-${task.priority.toLowerCase()}`">
              {{ task.priority }}
            </span>
          </div>
        </div>

        <div class="task-actions">
          <my-button
            @click="handleTaskSubmit(task.id)"
            :class="task.completed ? 'btn-secondary' : 'btn-success'"
            size="small"
          >
            {{ task.completed ? '↺ Undo' : '✓ Done' }}
          </my-button>

          <my-button
            @click="editTask(task.id)"
            class="btn-primary"
            size="small"
          >
            ✏ Edit
          </my-button>

          <my-button
            @click="deleteTask(task.id)"
            class="btn-danger"
            size="small"
          >
            🗑 Delete
          </my-button>
        </div>
      </div>
    </div>
  </div>
  <TaskForm
    v-if="showTaskForm"
    :is-edit="!!editingTask"
    :task-data="editingTask || {}"
    @close="closeTaskForm"
    @submit="handleTaskSubmit"
  />
</template>
<style scoped>
.task-list {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem;
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #e1e5e9;
}

.task-header h2 {
  margin: 0;
  color: #333;
  font-size: 2rem;
  font-weight: 600;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 3rem;
  color: #666;
  font-size: 1.1rem;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.error-state {
  background: #fee;
  color: #c33;
  padding: 1rem;
  border-radius: 8px;
  border: 1px solid #fcc;
  text-align: center;
  margin-bottom: 1rem;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #666;
  background: #f8f9fa;
  border-radius: 12px;
  border: 2px dashed #ddd;
}

.empty-state h3 {
  margin: 0 0 1rem 0;
  color: #333;
  font-size: 1.5rem;
}

.tasks-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.task-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #e1e5e9;
  transition: all 0.3s ease;
  overflow: hidden;
}

.task-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.task-main {
  padding: 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
}

.task-info {
  flex: 1;
}

.task-title {
  margin: 0 0 0.5rem 0;
  font-size: 1.25rem;
  font-weight: 600;
  color: #333;
  transition: all 0.3s ease;
}

.task-title.completed {
  text-decoration: line-through;
  color: #666;
  opacity: 0.7;
}

.task-description {
  margin: 0 0 1rem 0;
  color: #666;
  line-height: 1.5;
  font-size: 0.95rem;
}

.task-meta {
  display: flex;
  gap: 1rem;
  font-size: 0.85rem;
  color: #888;
}

.due-date {
  font-weight: 500;
}

/* Badges */
.task-badges {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  align-items: flex-end;
}

.status-badge, .priority-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.status-badge {
  background: #fff3cd;
  color: #856404;
}

.status-badge.completed {
  background: #d4edda;
  color: #155724;
}

.priority-badge {
  min-width: 80px;
  text-align: center;
}

.priority-low {
  background: #d1ecf1;
  color: #0c5460;
}

.priority-medium {
  background: #fff3cd;
  color: #856404;
}

.priority-high {
  background: #f8d7da;
  color: #721c24;
}

.priority-urgent {
  background: #721c24;
  color: white;
}

/* Действия */
.task-actions {
  background: #f8f9fa;
  padding: 1rem 1.5rem;
  display: flex;
  gap: 0.75rem;
  border-top: 1px solid #e9ecef;
}

.btn-primary {
  background: #667eea;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background: #5a6fd8;
}

.btn-success {
  background: #28a745;
  color: white;
}

.btn-success:hover {
  background: #218838;
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background: #545b62;
}

.btn-danger {
  background: #dc3545;
  color: white;
}

.btn-danger:hover {
  background: #c82333;
}

/* Responsive */
@media (max-width: 768px) {
  .task-header {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }

  .task-main {
    flex-direction: column;
    gap: 1rem;
  }

  .task-badges {
    flex-direction: row;
    align-items: center;
  }

  .task-actions {
    flex-wrap: wrap;
  }
}
</style>
