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

fun main() {
    val manager = TaskManager()

    while (true) {
        println()
        println("=== LISTA DE TAREFAS ===")
        println("1 - Adicionar tarefa")
        println("2 - Listar tarefas")
        println("3 - Buscar tarefa por ID")
        println("4 - Atualizar status")
        println("5 - Excluir tarefa")
        println("6 - Listar tarefas concluídas")
        println("7 - Listar terafas pendentes")
        println("8 - Mostrar quantidade de tarefas")
        println("0 - Sair")

        println("Escolha uma opção: ")

        when (readln()) {

            "1" -> {
                print("Título: ")
                val title = readln()

                println("Descrição (opcional): ")
                val descriptionInput = readln()

                val description =
                    if (descriptionInput.isBlank()) null
                    else descriptionInput

                val task = Task(
                    title = title,
                    description = description
                )

                println(manager.addTask(task))
            }

            "2" -> {
                println("\nTarefas: ")
                manager.listTasks()
            }

            "3" -> {
                print("Digite o ID da tarefa: ")
                val id = readln().toInt()

                val task = manager.findTaskById(id)

                if (task != null) {
                    println(task.toFormattedString())
                } else {
                    println("Tarefa não encontrada.")
                }
            }

            "4" -> {
                print("Digite o ID da tarefa: ")
                val id = readln().toInt()

                print("Concluir tarefa? (true/false): ")
                val isCompleted = readln().toBoolean()

                try {
                    println(
                        manager.updateTaskStatus(
                            id = id,
                            isCompleted = isCompleted
                        )
                    )
                } catch (exception: IllegalArgumentException) {
                    println(exception.message)
                }
            }

            "5" -> {
                print("Digite o ID da tarefa: ")
                val id = readln().toInt()

                try {
                    println(manager.deleteTask(id))
                } catch (exception: IllegalArgumentException) {
                    println(exception.message)
                }
            }

            "6" -> {
                println("\nTarefas concluídas: ")

                manager
                    .getCompletedTasks()
                    .forEach {
                        println(it.toFormattedString())
                        println()
                    }
            }

            "7" -> {
                println("\nTarefas pendentes: ")

                manager
                    .getPendingTasks()
                    .forEach {
                        println(it.toFormattedString())
                        println()
                    }
            }

            "8" -> {
                println("Total de tarefas: ${manager.getTaskCount()}")
            }

            "0" -> {
                println("Programa encerrado.")
                break
            }

            else -> {
                println("Opção inválida.")
            }
        }
    }
}