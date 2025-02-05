import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.DosFileAttributes
import kotlin.coroutines.coroutineContext

fun unlockFile(file: Path) {
    Files.setAttribute(file, "dos:readonly", false)
}

fun lockFile(file: Path) {
    Files.setAttribute(file, "dos:readonly", true)
}

fun modifyOverrideFlag(
    filePath: Path
) {
    val content = Files.readString(filePath)
    val modifiedContent = content.replace("_Override\":true", "_Override\":false")

    Files.writeString(filePath, modifiedContent, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)
}

fun watchFileChanges(filePath: Path): Flow<Boolean> = flow {
    val watchService = FileSystems.getDefault().newWatchService()
    val directory = filePath.parent

    val initialAttrs = Files.readAttributes(filePath, DosFileAttributes::class.java)
    emit(initialAttrs.isReadOnly)

    directory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)

    while (coroutineContext.isActive) {
        val key = watchService.poll(1, java.util.concurrent.TimeUnit.SECONDS) ?: continue
        for (event in key.pollEvents()) {
            val changedFile = event.context() as Path
            val fullPath = directory.resolve(changedFile)
            val fileChanged = Files.exists(fullPath) && fullPath == filePath

            if (fileChanged) {
                try {
                    val attrs = Files.readAttributes(fullPath, DosFileAttributes::class.java)
                    emit(attrs.isReadOnly)
                } catch (e: IOException) {
                    emit(false)
                }
            }
        }
        key.reset()
    }
}.flowOn(Dispatchers.IO)

fun rebootSystem(onComplete: (String) -> Unit) {
    runCatching {
        Runtime.getRuntime().exec("shutdown -r -t 0")
        onComplete("System rebooting...")
    }.onFailure {
        onComplete("Failed to initiate reboot: ${it.message}")
    }
}

fun restartNvidiaServices() : Boolean {
    try {
        val command = "powershell -Command \"Start-Process cmd -ArgumentList '/c net stop NvContainerLocalSystem && net start NvContainerLocalSystem && net stop NVDisplay.ContainerLocalSystem && net start NVDisplay.ContainerLocalSystem' -Verb RunAs\""

        val process = Runtime.getRuntime().exec(command)
        process.waitFor()

        return true
    } catch (e: Exception) {
        return false
    }
}