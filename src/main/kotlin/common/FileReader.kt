package common

/**
 * Used for reading resources in the resource folder only
 */
class FileReader {}

fun getFileContent(filename: String): String {
    return FileReader::class.java.getResource(filename)!!.readText()
}