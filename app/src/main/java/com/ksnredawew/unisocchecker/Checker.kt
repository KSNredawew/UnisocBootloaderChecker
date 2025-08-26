package com.ksnredawew.unisocchecker

import android.os.Build
import java.io.BufferedReader
import java.io.InputStreamReader

class Checker {

    data class CheckResult(
        val overallStatus: String,
        val details: String
    )

    fun performAllChecks(): CheckResult {
        if (!hasRootAccess()) {
            return CheckResult(
                getString(R.string.error_root),
                getString(R.string.error_root_details)
            )
        }

        val detailsBuilder = StringBuilder()
        val socInfo = getSocInfo()
        
        detailsBuilder.append("• SoC: ${socInfo.first ?: "Unknown"}\n")
        
        if (!isUnisocChip(socInfo.first)) {
            return CheckResult(
                getString(R.string.error_not_unisoc),
                "SoC manufacturer: ${socInfo.first ?: "Unknown"}"
            )
        }

        val securityPatch = Build.VERSION.SECURITY_PATCH
        detailsBuilder.append("• Security Patch: $securityPatch\n")
        
        val kernelVersion = getKernelVersion()
        detailsBuilder.append("• Kernel Version: $kernelVersion\n")

        val hasAntiRollback = checkAntiRollback()
        detailsBuilder.append("• Anti-Rollback: ${if (hasAntiRollback) "Detected" else "Not detected"}\n")

        val isChipsetSupported = isSupportedChipset(socInfo.second)
        val isPatchOld = securityPatch.compareTo("2022-10-01") < 0

        val overallStatus = when {
            !isChipsetSupported -> getString(R.status_chipset_unsupported)
            !isPatchOld -> getString(R.status_patch_too_new)
            hasAntiRollback -> getString(R.status_arb_detected)
            else -> getString(R.status_success_possible)
        }

        detailsBuilder.insert(0, "RESULT: $overallStatus\n\nDetails:\n")
        return CheckResult(overallStatus, detailsBuilder.toString())
    }

    private fun hasRootAccess(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("su -c id")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText()
            process.waitFor()
            output.contains("uid=0")
        } catch (e: Exception) {
            false
        }
    }

    private fun getSocInfo(): Pair<String?, String?> {
        val boardPlatform = getSystemProperty("ro.board.platform")
        val hardwarePlatform = getSystemProperty("ro.hardware")
        val chipname = getSystemProperty("ro.hardware.chipname")
        val suspectedSoc = boardPlatform ?: hardwarePlatform ?: "unknown"
        return Pair(suspectedSoc, chipname)
    }

    private fun getSystemProperty(propName: String): String? {
        return try {
            Class.forName("android.os.SystemProperties")
                .getMethod("get", String::class.java)
                .invoke(null, propName) as? String
        } catch (e: Exception) {
            null
        }
    }

    private fun getKernelVersion(): String {
        return System.getProperty("os.version") ?: "Unknown"
    }

    private fun checkAntiRollback(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("su -c getprop ro.boot.flash.locked")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText().trim()
            process.waitFor()
            output == "1"
        } catch (e: Exception) {
            false
        }
    }

    private fun isUnisocChip(soc: String?): Boolean {
        return soc?.equals("unisoc", ignoreCase = true) == true ||
                soc?.equals("spreadtrum", ignoreCase = true) == true
    }

    private fun isSupportedChipset(chipname: String?): Boolean {
        val supportedChipsets = listOf("ums9620", "ums9230", "t618", "t606", "t700", "t740")
        return supportedChipsets.any { chipname?.contains(it, ignoreCase = true) ?: false }
    }
}
