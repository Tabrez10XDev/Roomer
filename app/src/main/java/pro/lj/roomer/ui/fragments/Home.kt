package pro.lj.roomer.ui.fragments

import android.content.ClipDescription
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import pro.lj.roomer.R
import pro.lj.roomer.databinding.HomeBinding
import pro.lj.roomer.ui.app.Dashboard
import pro.lj.roomer.ui.app.MainActivity


class Home : Fragment(R.layout.home) {
    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.button1.setOnClickListener {
            signOut()
        }

    }

    private fun signOut(){
        auth.signOut()

        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    //        imageView.setOnDragListener(dragListener)
//        cvBlue.setOnLongClickListener {
//            val clipText = "This is our clipData Text"
//            val item = ClipData.Item(clipText)
//            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
//            val data = ClipData(clipText, mimeTypes, item)
//
//            val dragShadowBuilder = View.DragShadowBuilder(it)
//            it.startDragAndDrop(data, dragShadowBuilder, it, 0)
//            true
//        }

    val dragListener = View.OnDragListener { view, event ->
        when(event.action){
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED-> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP-> {
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                Toast.makeText(activity, dragData, Toast.LENGTH_SHORT).show()

                view.invalidate()

                val v = event.localState as View
                val destination = view
                Log.d("TABY",dragData.toString())
                true
            }
            DragEvent.ACTION_DRAG_ENDED-> {
                view.invalidate()
                true
            }
            else-> {
                false
            }
        }
    }
}