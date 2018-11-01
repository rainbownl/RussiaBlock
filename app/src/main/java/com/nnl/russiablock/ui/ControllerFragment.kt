package com.nnl.russiablock.ui


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.nnl.russiablock.R
import com.nnl.russiablock.msg.MsgId

/**
 * A simple [Fragment] subclass.
 *
 */
class ControllerFragment : Fragment(), View.OnClickListener{
    companion object {
        private val MSG_SCORE = MsgId.getId()
    }
    private var mHandler: Handler? = null
    private var mTvScore : TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_controller, container, false)

        view.findViewById<Button>(R.id.controller_btn_down).setOnClickListener(this)
        view.findViewById<Button>(R.id.controller_btn_left).setOnClickListener(this)
        view.findViewById<Button>(R.id.controller_btn_pause).setOnClickListener(this)
        view.findViewById<Button>(R.id.controller_btn_right).setOnClickListener(this)
        view.findViewById<Button>(R.id.controller_btn_rotate).setOnClickListener(this)
        mTvScore = view.findViewById(R.id.controller_tv_score)
        return view
    }

    override fun onClick(v: View?) {
        val mainActivity = activity as MainActivity
        when(v?.id) {
            R.id.controller_btn_left -> mainActivity.gameManager?.moveLeft()
            R.id.controller_btn_right -> mainActivity.gameManager?.moveRight()
            R.id.controller_btn_rotate -> mainActivity.gameManager?.rotate()
            R.id.controller_btn_down -> mainActivity.gameManager?.fallDown()
        }
    }

    override fun onStart() {
        super.onStart()
        mHandler = Handler { msg ->
            when (msg.what) {
                MSG_SCORE -> {
                    mTvScore?.text = ("" + msg.arg1)
                    true
                }
                else -> false
            }
        }
    }
}
