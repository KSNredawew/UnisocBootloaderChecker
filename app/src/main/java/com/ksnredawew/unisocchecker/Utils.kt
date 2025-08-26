package com.ksnredawew.unisocchecker

import java.io.BufferedReader
import java.io.InputStreamReader

object Utils {

    fun executeCommand(command: String, requireRoot: Boolean = false): CommandResult {
        return try {
            val fullCommand = if (requireRoot) "su -c $command" else command
            val process = Runtime.getRuntime().exec(fullCommand)
            
            val outputReader = BufferedReader(InputStreamReader(process.inputStream))
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))
            
            val output = StringBuilder()
            val error = StringBuilder()
            
            var line: String?
            while (outputReader.readLine().also { line = it } != null) {
                output.appendLine(line)
            }
            
            while (errorReader.readLine().also { line = it } != null) {
                error.appendLine(line)
            }
            
            process.waitFor()
            
            CommandResult(
                exitCode = process.exitValue(),
                output = output.toString().trim(),
                error = error.toString().trim()
            )
        } catch (e: Exception) {
            CommandResult(
                exitCode = -1,
                output = "",
                error = "Command execution failed: ${e.message}"
            )
        }
    }

    fun getSystemProperty(key: String): String? {
        return try {
            val method = Class.forName("android.os.SystemProperties")
                .getMethod("get", String::class.java)
            method.invoke(null, key) as? String
        } catch (e: Exception) {
            null
        }
    }

    fun isRootAvailable(): Boolean {
        return executeCommand("id").output.contains("uid=0")
    }

    data class CommandResult(
        val exitCode: Int,
        val output: String,
        val error: String
    )
}
