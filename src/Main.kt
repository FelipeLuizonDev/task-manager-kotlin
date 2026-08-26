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