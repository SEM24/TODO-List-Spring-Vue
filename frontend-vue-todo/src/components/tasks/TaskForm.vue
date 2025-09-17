<script setup>
import {reactive, watch} from "vue";

const props = defineProps({
  isEdit: {
    type: Boolean,
    default: false
  },
  taskData: {
    type: Object,
    default: () => ({})
  }
})
const emit = defineEmits(['close', 'submit'])
const form = reactive({
  title: '',
  description: '',
  priority: 'MEDIUM',
  dueDate: ''
})
const formatDateForInput = (dateString) => {
  if (!dateString) return ''
  if (dateString.includes('T') && dateString.includes(':')) {
    return dateString.slice(0, 16) // Берем только "2025-09-05T18:00"
  }
  try {
    const date = new Date(dateString)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')

    return `${year}-${month}-${day}T${hours}:${minutes}`
  } catch (error) {
    console.error('Error formatting date:', error)
    return ''
  }
}
watch(() => props.taskData, (newData) => {
  if (newData && Object.keys(newData).length > 0) {
    form.title = newData.title || ''
    form.description = newData.description || ''
    form.priority = newData.priority || 'MEDIUM'
    form.dueDate = formatDateForInput(newData.dueDate)
  }
}, {immediate: true})
const closeForm = () => {
  form.title = ''
  form.description = ''
  form.priority = 'MEDIUM'
  form.dueDate = ''
  emit('close')
}
const submitForm = () => {
  const dueDate = form.dueDate ? form.dueDate + ':00' : null

  const taskData = {
    title: form.title.trim(),
    description: form.description.trim(),
    priority: form.priority,
    completed: props.taskData?.completed || false, // Сохраняем текущий статус при редактировании
    dueDate: dueDate
  }
  console.log('Submitting task:', taskData)
  emit('submit', taskData)
}
</script>

<template>
  <div class="task-form-overlay" @click.self="closeForm">
    <div class="task-form">
      <div class="form-header">
        <h3>{{ isEdit ? 'Edit Task' : 'Add new Task' }}</h3>
        <button @click="closeForm" class="close-btn">×</button>
      </div>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="title">Title *</label>
          <input type="text" id="title" v-model="form.title" required
                 placeholder="Enter task title">
        </div>
        <div class="form-group">
          <label for="title">Description</label>
          <input type="text" id="description" rows="3" v-model="form.description"
                 placeholder="Enter task description (optional)">
        </div>
        <div class="form-row">
          <div class="form-group">
            <label for="priority">Priority</label>
            <select v-model="form.priority" id="priority">
              <option value="LOW">Low</option>
              <option value="MEDIUM">Medium</option>
              <option value="HIGH">High</option>
              <option value="URGENT">Urgent</option>
            </select>
          </div>
        </div>
        <div class="form-group">
          <label for="dueDate">Due Date</label>
          <input
            id="dueDate"
            v-model="form.dueDate"
            type="datetime-local"
          />
        </div>
        <div class="form-actions">
          <button type="button" @click="closeForm" class="btn-secondary">
            Cancel
          </button>
          <button type="submit" class="btn-primary" :disabled="!form.title.trim()">
            {{ isEdit ? 'Update Task' : 'Create Task' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.task-form-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.task-form {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.form-header h3 {
  margin: 0;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #666;
}

.close-btn:hover {
  color: #333;
}

.form-group {
  margin-bottom: 1rem;
}

.form-row {
  display: flex;
  gap: 1rem;
}

.form-row .form-group {
  flex: 1;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: #667eea;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
}

.btn-primary, .btn-secondary {
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: #667eea;
  color: white;
  border: none;
}

.btn-primary:hover:not(:disabled) {
  background: #5a6fd8;
}

.btn-primary:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.btn-secondary {
  background: #f8f9fa;
  color: #333;
  border: 1px solid #ddd;
}

.btn-secondary:hover {
  background: #e9ecef;
}

@media (max-width: 600px) {
  .form-row {
    flex-direction: column;
  }

  .form-actions {
    flex-direction: column;
  }
}
</style>
