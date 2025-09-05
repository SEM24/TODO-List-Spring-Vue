# Todo List Frontend

Modern Vue.js 3 application for managing todo tasks with authentication and beautiful UI.
## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).

## Project Setup

```sh
npm install
```

## Available Scripts

```bash
# Development
npm run dev          # Start development server
npm run build        # Build for production
npm run preview      # Preview production build
```

## Components Overview

### UI Components
- **MyButton**: Customizable button with different variants
- **MyInput**: Enhanced input field with validation support
- **LoadingSpinner**: Reusable loading indicator

### Task Components
- **TaskList**: Main container for displaying tasks
- **TaskItem**: Individual task card with actions
- **TaskForm**: Form for creating/editing tasks
- **TaskFilters**: Search and filter controls

### Pages
- **SignInPage**: User authentication
- **RegisterPage**: New user registration
- **DashboardView**: Main task management interface
- **UserPage**: User profile and settings

## Services

### AuthService
Handles all authentication-related API calls:
- User login/logout
- Registration
- Token management
- OAuth2 integration
- User profile management

### TaskService
Manages task-related operations:
- CRUD operations for tasks
- Filtering and searching
- Status updates
- Priority management

## Screenshots

### Login Page
<img width="1803" height="1301" alt="image" src="https://github.com/user-attachments/assets/7d54f096-aa44-450f-b8c4-6264d6f96add" />


### Task Management
<img width="976" height="993" alt="image" src="https://github.com/user-attachments/assets/9589b073-1175-449d-95bf-f34e8d8fcc02" />
