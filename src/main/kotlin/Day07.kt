fun main() {

    val commandRegex = "^\\$.*".toRegex()
    val dirMoveRegex = "^\\$ cd (.+)".toRegex()
    val fileListRegex = "^(\\d+) (.+)".toRegex()
    val totalSpace = 70000000
    val targetSpace = 30000000

    val fileContent = getFileContent("7.txt")

    val rootDir = Directory("/", null)
    var currentDir = rootDir


    fileContent.split("\n").forEach { line ->

        if (commandRegex.matches(line)) {
            val dirCommand = dirMoveRegex.matchEntire(line)
            if (dirCommand != null) {
                val (dir) = dirCommand.destructured
                currentDir = when (dir) {
                    ".." -> {
                        currentDir.parentDirectory!!
                    }
                    "/" -> {
                        rootDir
                    }
                    else -> {
                        Directory(dir, currentDir)
                    }
                }
            }
        } else {
            val fileListing = fileListRegex.matchEntire(line)
            if (fileListing != null) {
                val (size, fileName) = fileListing.destructured
                currentDir.files[fileName] = size.toInt()
            }
        }
    }

    rootDir.calculateFileSize()

    println(rootDir.recursiveSum { it < 100000 })

    val spaceToFree = targetSpace - (totalSpace - rootDir.size)

    println("need to free $spaceToFree")

    val dirToDelete = rootDir.findDirWithSizeCloseTo(spaceToFree)

    println("Remove dir '${dirToDelete!!.name}' with size ${dirToDelete.size}")

}

class Directory(
    val name: String,
    val parentDirectory: Directory?
) {
    private val subdirectories = mutableListOf<Directory>()
    val files = mutableMapOf<String, Int>()
    var size: Int = 0

    init {
        parentDirectory?.subdirectories?.add(this)
    }

    fun calculateFileSize(): Int {
        size = files.values.sum() + subdirectories.sumOf { it.calculateFileSize() }
        return size
    }

    fun recursiveSum(filter: (Int) -> Boolean): Int =
        subdirectories.sumOf { it.recursiveSum(filter) } + (size.takeIf(filter) ?: 0)

    fun findDirWithSizeCloseTo(targetSize: Int): Directory? =
        (subdirectories.mapNotNull { it.findDirWithSizeCloseTo(targetSize) } + this)
            .filter { it.size > targetSize }
            .minByOrNull { it.size }
}

