package com.nnl.russiablock.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.nnl.russiablock.R

/**
 * A simple [Fragment] subclass.
 *
 */
class ControllerFragment : Fragment(), View.OnClickListener{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_controller, container, false)

        view.findViewById<Button>(R.id.controller_btn_down).setOnClickListener(this)
        view.findViewById<Button>(R.id.controller_btn_left).setOnClickListener(this)
        view.findViewById<Button>(R.id.controller_btn_pause).setOnClickListener(this)
        view.findViewById<Button>(R.id.controller_btn_right).setOnClickListener(this)
        view.findViewById<Button>(R.id.controller_btn_rotate).setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        var mainActivity = activity as MainActivity
        when(v?.id) {
            R.id.controller_btn_left -> mainActivity.gameManager?.moveLeft()
            R.id.controller_btn_right -> mainActivity.gameManager?.moveRight()
            R.id.controller_btn_rotate -> mainActivity.gameManager?.rotate()
            R.id.controller_btn_down -> mainActivity.gameManager?.fallDown()
        }
    }
}
