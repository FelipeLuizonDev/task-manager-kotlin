import java.time.LocalDateTime

data class Task(
    val id: Int = generateId(),
    val title: String,
    val description: String? = null,
    var isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
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

    fun getTaskCount(): Int {
        return tasks.size
    }
}

fun readTaskId(): Int {
    var id: Int? = null

    while (id == null || id < 1) {
        print("-> ")
        id = readlnOrNull()?.toIntOrNull()

        if (id == null || id < 1) {
            println("O ID inserido é inválido. Tente novamente.")
        }
    }

    return id
}

fun readTaskTitle(): String {
    var title: String? = null

    while (title.isNullOrBlank()) {
        print("-> ")
        title = readlnOrNull()

        if (title.isNullOrBlank()) {
            println("O título inserido é inválido. Tente novamente.")
        }
    }

    return title
}

fun readTaskStatus(): Boolean {
    var status: Boolean? = null

    while (status == null) {
        print("-> ")
        status = readlnOrNull()?.toBooleanStrictOrNull()

        if (status == null) {
            println("Status inválido! Digite true ou false.")
        }
    }

    return status
}

fun main() {
    val manager = TaskManager()

    var option: Int? = null

    while (option != 0) {
        println(
            """
            
            +----------------------------------+
            |         LISTA DE TAREFAS         |
            +----------------------------------+
            | 1 - Adicionar tarefa             |
            | 2 - Listar tarefas               |
            | 3 - Buscar tarefa por ID         |
            | 4 - Atualizar status             |
            | 5 - Excluir tarefa               |
            | 6 - Tarefas concluídas           |
            | 7 - Tarefas pendentes            |
            | 8 - Quantidade de tarefas        |
            | 0 - Sair                         |
            +----------------------------------+
            """.trimIndent()
        )

        println("Escolha uma opção:")
        print("-> ")

        option = readlnOrNull()?.toIntOrNull()

        when (option) {

            1 -> {
                println("Insira o título da tarefa:")
                val title = readTaskTitle()

                println("Insira uma descrição (opcional):")
                print("-> ")

                val description = readlnOrNull()
                    ?.takeIf { it.isNotBlank() }

                val task = Task(
                    title = title,
                    description = description
                )

                println(manager.addTask(task))
            }

            2 -> {
                println("\nTAREFAS:")

                if (manager.getTaskCount() == 0) {
                    println("Nenhuma tarefa cadastrada.")
                } else {
                    manager.listTasks()
                }
            }

            3 -> {
                println("Insira o ID da tarefa:")
                val id = readTaskId()

                val task = manager.findTaskById(id)

                println(
                    task?.toFormattedString()
                        ?: "Não existe nenhuma tarefa com esse ID."
                )
            }

            4 -> {
                println("Insira o ID da tarefa:")
                val id = readTaskId()

                println("Digite o novo status da tarefa (true/false):")
                val status = readTaskStatus()

                try {
                    println(
                        manager.updateTaskStatus(
                            id = id,
                            isCompleted = status
                        )
                    )
                } catch (exception: IllegalArgumentException) {
                    println(exception.message)
                }
            }

            5 -> {
                println("Insira o ID da tarefa:")
                val id = readTaskId()

                try {
                    println(manager.deleteTask(id))
                } catch (exception: IllegalArgumentException) {
                    println(exception.message)
                }
            }

            6 -> {
                val completedTasks = manager.getCompletedTasks()

                println("\nTAREFAS CONCLUÍDAS:")

                if (completedTasks.isEmpty()) {
                    println("Nenhuma tarefa concluída.")
                } else {
                    completedTasks.forEach { task ->
                        println(task.toFormattedString())
                        println()
                    }
                }
            }

            7 -> {
                val pendingTasks = manager.getPendingTasks()

                println("\nTAREFAS PENDENTES:")

                if (pendingTasks.isEmpty()) {
                    println("Nenhuma tarefa pendente.")
                } else {
                    pendingTasks.forEach { task ->
                        println(task.toFormattedString())
                        println()
                    }
                }
            }

            8 -> {
                println(
                    "Quantidade de tarefas: ${manager.getTaskCount()}"
                )
            }

            0 -> {
                println("Programa encerrado.")
            }

            else -> {
                println("Opção inválida. Tente novamente.")
            }
        }
    }
}