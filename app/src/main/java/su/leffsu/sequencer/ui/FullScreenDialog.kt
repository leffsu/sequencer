package su.leffsu.sequencer.ui

import android.content.Context
import android.view.View
import org.leffsu.sequencer.R

class FullscreenDialog(context: Context) : View(context), View.OnClickListener {

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.dialog_fullscreen, container, false)
//        val close = view.findViewById<ImageButton>(R.id.fullscreen_dialog_close)
//        val action = view.findViewById<TextView>(R.id.fullscreen_dialog_action)
//
//        close.setOnClickListener(this)
//        action.setOnClickListener(this)
//
//        return view
//
//    }

    override fun onClick(v: View) {
        val id = v.getId()

        when (id) {

//            R.id.fullscreen_dialog_close -> dismiss()

            R.id.fullscreen_dialog_action -> {
                callback!!.onActionClick("Whatever")
//                dismiss()
            }
        }

    }

    interface Callback {

        fun onActionClick(name: String)

    }
}