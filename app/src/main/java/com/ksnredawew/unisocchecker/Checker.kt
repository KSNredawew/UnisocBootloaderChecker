package com.ksnredawew.unisocchecker

import android.os.Build

class Checker {

    data class CheckResult(
        val overallStatus: String,
        val details: String
    )

    fun performAllChecks(): CheckResult {
        if (!Utils.isRootAvailable()) {
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
                getString(R.string.error_unknown_soc),
                "SoC manufacturer: ${socInfo.first ?: "Unknown"}. This tool is designed for Unisoc/Spreadtrum devices."
            )
        }

        val securityPatch = Build.VERSION.SECURITY_PATCH
        detailsBuilder.append("• Security Patch: $securityPatch\n")
        val isPatchOld = securityPatch.compareTo("2022-10-01") < 0

        val kernelVersion = getKernelVersion()
        detailsBuilder.append("• Kernel Version: $kernelVersion\n")

        val hasAntiRollback = checkAntiRollback()
        detailsBuilder.append("• Anti-Rollback: ${if (hasAntiRollback) "Detected" else "Not detected"}\n")

        val supportedChipsets = listOf("ums9620", "ums9230", "t618", "t606", "t700", "t740")
        val isChipsetSupported = supportedChipsets.any { socInfo.second?.contains(it, ignoreCase = true) ?: false }

        val overallStatus = when {
            !isChipsetSupported -> "Chipset may not be supported"
            !isPatchOld -> "Low success probability. Security patch is newer than $criticalDate."
            hasAntiRollback -> "High risk of brick. Anti-rollback detected."
            else -> "High success probability (but no guarantees)"
        }

        detailsBuilder.insert(0, "RESULT: $overallStatus\n\nDetails:\n")
        return CheckResult(overallStatus, detailsBuilder.toString())
    }

    private fun getSocInfo(): Pair<String?, String?> {
        val boardPlatform = Utils.getSystemProperty("ro.board.platform")
        val hardwarePlatform = Utils.getSystemProperty("ro.hardware")
        val chipname = Utils.getSystemProperty("ro.hardware.chipname")
        val suspectedSoc = boardPlatform ?: hardwarePlatform ?: "unknown"
        return Pair(suspectedSoc, chipname)
    }

    private fun getKernelVersion(): String {
        return System.getProperty("os.version") ?: "Unknown"
    }

    private fun checkAntiRollback(): Boolean {
        val result = Utils.executeCommand("getprop ro.boot.flash.locked", true)
        return result.output == "1"
    }

    private fun isUnisocChip(soc: String?): Boolean {
        return soc?.equals("unisoc", ignoreCase = true) == true ||
                soc?.equals("spreadtrum", ignoreCase = true) == true
    }

    companion object {
        private const val criticalDate = "2022-10-01"
    }
}
