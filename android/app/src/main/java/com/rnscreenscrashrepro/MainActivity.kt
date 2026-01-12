package com.rnscreenscrashrepro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler

class MainActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Show NativeFragment on first launch
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NativeFragment())
                .commit()
        }
    }

    fun pushReactFragmentA() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, CustomRNFragment("RNScreensCrashRepro", "A"))
            .addToBackStack("FragmentA")
            .commit()
    }

    fun pushReactFragmentB() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, CustomRNFragment("RNScreensCrashRepro", "B"))
            .addToBackStack("FragmentB")
            .commit()
    }

    fun pushNativeFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, NativeFragment())
            .addToBackStack("NativeFragment")
            .commit()
    }

    fun pop() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }
}
