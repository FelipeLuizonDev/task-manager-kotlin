data class Task(
    val id: Int = generateId(),
    val title: String,
    val description: String? = null,
    var isCompleted: Boolean = false,
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

    fun toFormattedString(): String {
        return """
            ID: $id
            Título: $title
            Descrição: ${description ?: "Sem descrição"}
            Concluída: $isCompleted
            Criada em: $createdAt
        """.trimIndent()
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

    fun findTaskById(id: Int): Task? {
        return tasks.find { task ->
            task.id == id
        }
    }

    fun updateTaskStatus(id: Int, isCompleted: Boolean): TaskResult {
        val task = findTaskById(id)

        require(task != null) {
            "Tarefa com ID $id não encontrada."
        }

        task.isCompleted = isCompleted

        return TaskResult.Success(
            "Status da tarefa ID $id atualizado para $isCompleted"
        )
    }

    fun deleteTask(id: Int): TaskResult {
        val task = findTaskById(id)

        require(task != null) {
            "Tarefa com ID $id não encontrada."
        }

        tasks.remove(task)

        return TaskResult.Success(
            "Tarefa ID $id excluida com sucesso!"
        )
    }

    fun getCompletedTasks(): List<Task> {
        return tasks.filter { task ->
            task.isCompleted
        }
    }

    fun getPendingTasks(): List<Task> {
        return tasks.filter { task ->
            !task.isCompleted
        }
    }
}