<script setup>
import {onMounted, ref} from "vue";
import taskService from "@/services/tasksService.js";
import MyButton from "@/components/UI/MyButton.vue";

const tasks = ref([])
const loading = ref(false)
const error = ref(null)
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
onMounted(() => {
  fetchTasks()
})
</script>

<template>
  <div class="task-list">
    <div v-if="loading">Loading...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else-if="tasks.length === 0" class="empty-state">
      <h3>No tasks yet</h3>
      <p>Create your first task!</p>
    </div>
    <div v-else>
      <div v-for="task in tasks" :key="task.id" class="task-item">
        <h4>{{ task.title }}</h4>
        <h4 :data-completed="task.completed">
          {{ task.completed ? 'Done' : 'Pending' }}
        </h4>
        <h4 :class="`priority-${task.priority.toLowerCase()}`">
          {{ task.priority }}
        </h4>
        <!--        todo add ability to toggle status-->
        <my-button>
          {{ task.completed === 'false' ? 'Not completed' : 'Completed' }}
        </my-button>
      </div>
    </div>
  </div>
</template>
<style scoped>
.task-list {
  max-width: 800px;
  margin: 0 auto;
  padding: 1rem;
}

.task-item {
  background: white;
  border: 1px solid #e1e5e9;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 0.75rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.task-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-color: #667eea;
}

.task-item h4 {
  margin: 0;
  font-size: 1rem;
}

.task-item h4:first-child {
  flex: 1;
  font-weight: 500;
  color: #333;
}

.task-item h4:nth-child(2) {
  font-size: 0.8rem;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-weight: 600;
  text-transform: uppercase;
}

/* Стили для completed статуса */
.task-item h4:nth-child(2)[data-completed="true"] {
  background: #d4edda;
  color: #155724;
}

.task-item h4:nth-child(2)[data-completed="false"] {
  background: #f8d7da;
  color: #721c24;
}

.task-item h4:nth-child(3) {
  font-size: 0.8rem;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-weight: 600;
  min-width: 70px;
  text-align: center;
}

/* Стили для приоритетов */
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

/* Loading и Error стили */
.task-list > div:first-child {
  text-align: center;
  padding: 3rem;
  font-size: 1.2rem;
  color: #666;
}

.task-list > div:nth-child(2) {
  text-align: center;
  padding: 2rem;
  background: #f8d7da;
  color: #721c24;
  border-radius: 8px;
  border: 1px solid #f5c6cb;
}

/* Пустой список */
.empty-state {
  text-align: center;
  padding: 3rem;
  color: #666;
}

.empty-state h3 {
  margin-bottom: 0.5rem;
  color: #333;
}
</style>
