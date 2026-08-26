data class Task(
    val id: Int = generateId(),
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val createdAt: String
) {
    companion object {
        private var nextId = 1

        private fun generateId(): Int = nextId++
    }
}