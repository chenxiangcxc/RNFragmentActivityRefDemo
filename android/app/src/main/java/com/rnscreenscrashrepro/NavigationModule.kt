package com.rnscreenscrashrepro

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule

class NavigationModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "NavigationModule"

    companion object {
        var instance: NavigationModule? = null
    }

    init {
        instance = this
    }

    fun sendActivityStateEvent(status: String) {
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            ?.emit("onActivityStateChanged", status)
    }

    @ReactMethod
    fun pushReactFragmentA() {
        reactApplicationContext.currentActivity?.runOnUiThread {
            (reactApplicationContext.currentActivity as? MainActivity)?.pushReactFragmentA()
        }
    }

    @ReactMethod
    fun pushReactFragmentB() {
        reactApplicationContext.currentActivity?.runOnUiThread {
            (reactApplicationContext.currentActivity as? MainActivity)?.pushReactFragmentB()
        }
    }

    @ReactMethod
    fun pushNativeFragment() {
        reactApplicationContext.currentActivity?.runOnUiThread {
            (reactApplicationContext.currentActivity as? MainActivity)?.pushNativeFragment()
        }
    }

    @ReactMethod
    fun pop() {
        reactApplicationContext.currentActivity?.runOnUiThread {
            (reactApplicationContext.currentActivity as? MainActivity)?.pop()
        }
    }

    @ReactMethod
    fun checkActivityState(promise: com.facebook.react.bridge.Promise) {
        try {
            val activity = reactApplicationContext.currentActivity
            val hasActivity = reactApplicationContext.hasCurrentActivity()

            val status = if (activity == null) {
                "Activity: null (DETACHED - This is the bug!)"
            } else {
                "Activity: ${activity.javaClass.simpleName}@${Integer.toHexString(activity.hashCode())}"
            }

            android.util.Log.d("NavigationModule", "Activity status: $status")
            android.util.Log.d("NavigationModule", "Has current activity: $hasActivity")

            promise.resolve(status)
        } catch (e: Exception) {
            promise.reject("ERROR", "Failed to check activity state", e)
        }
    }

    @ReactMethod
    fun addListener(eventName: String) {
        // Keep: Required for RN built-in Event Emitter Calls.
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        // Keep: Required for RN built-in Event Emitter Calls.
    }
}
