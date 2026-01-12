package com.rnscreenscrashrepro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.facebook.react.ReactFragment
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled

/**
 * Custom ReactFragment that controls ReactHost lifecycle to reproduce the detached activity bug
 */
class CustomRNFragment(
    private val componentName: String,
    private val fragmentId: String = "A"
) : ReactFragment() {

    private val statusCheckHandler = Handler(Looper.getMainLooper())
    private var statusCheckRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Bundle().apply {
            putString(ARG_COMPONENT_NAME, componentName)
            putBoolean(ARG_FABRIC_ENABLED, fabricEnabled)

            // Pass fragmentId as initial props to React Native
            val initialProps = Bundle().apply {
                putString("fragmentId", fragmentId)
            }
            putBundle(ARG_LAUNCH_OPTIONS, initialProps)
        }
        .let(::setArguments)
        super.onCreate(savedInstanceState)
        Log.d("CustomRNFragment", "[$fragmentId] onCreate - activity: ${activity}")
    }

    override fun onResume() {
        Log.d("CustomRNFragment", "[$fragmentId] onResume START - activity: ${activity}")

        // This will call ReactHost.onHostResume() and re-attach the activity
        super.onResume()

        // Start periodic status check from native side (only for Fragment A)
        if (fragmentId == "A") {
            startStatusCheck()
        }
    }

    override fun onPause() {
        Log.d("CustomRNFragment", "[$fragmentId] onPause - activity: ${activity}")

        // Stop periodic status check
        if (fragmentId == "A") {
            stopStatusCheck()
        }

        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.d("CustomRNFragment", "[$fragmentId] onStop - activity: ${activity}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("CustomRNFragment", "[$fragmentId] onDestroyView - activity: ${activity}")
    }

    override fun onDestroy() {
        Log.d("CustomRNFragment", "[$fragmentId] onDestroy - activity: ${activity}")
        Log.d("CustomRNFragment", "[$fragmentId] isRemoving: $isRemoving, isAdded: $isAdded")

        // Stop periodic status check
        if (fragmentId == "A") {
            stopStatusCheck()
        }

        // Call super.onDestroy() which will trigger ReactHost.onHostDestroy()
        // This sets activity to null in ReactContext
        super.onDestroy()
    }

    private fun startStatusCheck() {
        Log.d("CustomRNFragment", "[$fragmentId] Starting periodic status check")
        var lastStatus: String? = null
        statusCheckRunnable = object : Runnable {
            override fun run() {
                try {
                    val app = activity?.application as? com.facebook.react.ReactApplication
                    val currentActivity = app?.reactHost?.currentReactContext?.currentActivity

                    val status = if (currentActivity == null) {
                        "Activity: null (DETACHED - This is the bug!)"
                    } else {
                        "Activity: ${currentActivity.javaClass.simpleName}@${Integer.toHexString(currentActivity.hashCode())}"
                    }

                    Log.d("CustomRNFragment", "[$fragmentId] Periodic check - status: $status")

                    // Show toast when status changes
                    if (status != lastStatus) {
                        activity?.runOnUiThread {
                            android.widget.Toast.makeText(
                                requireContext(),
                                if (currentActivity == null) {
                                    "🐛 BUG: ReactContext.currentActivity = null"
                                } else {
                                    "✅ Activity OK: ${currentActivity.javaClass.simpleName}"
                                },
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                        lastStatus = status
                    }

                    // Send event to JS (may not work if ReactContext is destroyed)
                    NavigationModule.instance?.sendActivityStateEvent(status)

                    // Schedule next check
                    statusCheckHandler.postDelayed(this, 500)
                } catch (e: Exception) {
                    Log.e("CustomRNFragment", "[$fragmentId] Error in status check", e)
                }
            }
        }
        statusCheckHandler.post(statusCheckRunnable!!)
    }

    private fun stopStatusCheck() {
        Log.d("CustomRNFragment", "[$fragmentId] Stopping periodic status check")
        statusCheckRunnable?.let { statusCheckHandler.removeCallbacks(it) }
        statusCheckRunnable = null
    }
}
