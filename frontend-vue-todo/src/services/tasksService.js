import axios from "axios";

const API_BASE_URL = 'http://localhost:8082'
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json"
  }
})

class TaskService {
  async getAllTasks(filters = {}) {
    try {
      const params = new URLSearchParams()
      if (filters.completed !== undefined) {
        params.append('completed', filters.completed)
      }
      if (filters.priority) {
        params.append('priority', filters.priority)
      }
      if (filters.search) {
        params.append('search', filters.search)
      }
      if (filters.overdue) {
        params.append('overdue', 'true')
      }
      if (filters.today) {
        params.append('today', 'true')
      }
      const queryString = params.toString()
      const url = `/api/v1/tasks${queryString ? '?' + queryString : ''}`
      // http://localhost:8082/api/v1/tasks?overdue=false&today=false
      const response = await apiClient.get(url)
      return {
        success: true,
        data: response.data,
        message: "Tasks loaded successfully"
      }
    } catch (error) {
      console.error("Get tasks error:", error)
      return {
        success: false,
        error: error.response?.data?.message || 'Failed to load tasks',
        status: error.response?.status
      }
    }
  }

  async getTaskById(taskId) {

  }

  async deleteTask(taskId) {
    try {
      console.log("Service received taskId:", taskId);
      const response = await apiClient.delete(`api/v1/tasks/${taskId}`)
      return {
        success: true,
        data: response.data,
        message: "Task status deleted"
      }
    } catch (error) {
      return {
        success: false,
        error: error.response?.data?.message || 'Failed to delete task',
        status: error.response?.status
      }
    }
  }

  async toggleTask(taskId) {
    try {
      console.log("Service received taskId:", taskId);
      const response = await apiClient.patch(`api/v1/tasks/${taskId}/toggle`)
      return {
        success: true,
        data: response.data,
        message: "Task status updated"
      }
    } catch (error) {
      return {
        success: false,
        error: error.response?.data?.message || 'Failed to toggle task',
        status: error.response?.status
      }
    }
  }
}

export const taskService = new TaskService()
export default taskService
