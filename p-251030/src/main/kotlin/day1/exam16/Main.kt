package day1.exam16

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun main() {
    Files.copy(File("a.txt").toPath(), File("a_copy.txt").toPath(), StandardCopyOption.REPLACE_EXISTING)
}