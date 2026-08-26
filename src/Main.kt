data class Task(
    val id: Int = generateId(),
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val createdAt: String
) {
    init {
        require(title.isNotBlank()) {
            "O título da tarefa não pode estar vazio."
        }
    }

    companion object {
        private var nextId = 1

        private fun generateId(): Int = nextId++
    }
}

sealed class TaskResult {
    data class Success(val message: String) : TaskResult()
    data class Error(val message: String) : TaskResult()
}

class TaskManager {
    private val tasks = mutableListOf<Task>()

    fun addTask(task: Task): TaskResult {
        tasks.add(task)

        return TaskResult.Success(
            "Tarefa adicionada com sucesso! ID: ${task.id}"
        )
    }

    fun listTasks() {
        tasks.forEach { task ->
            val (_, title, _, isCompleted) = task

            println("($title, $isCompleted)")
        }
    }
}