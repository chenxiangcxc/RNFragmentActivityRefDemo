package com.rnscreenscrashrepro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class NativeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_native, container, false)

        // Set up button to launch Fragment A
        view.findViewById<Button>(R.id.btn_launch_fragment_a)?.setOnClickListener {
            (activity as? MainActivity)?.pushReactFragmentA()
        }

        return view
    }
}
