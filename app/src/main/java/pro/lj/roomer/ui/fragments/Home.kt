package pro.lj.roomer.ui.fragments

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import pro.lj.roomer.R
import pro.lj.roomer.data.Item
import pro.lj.roomer.databinding.HomeBinding
import pro.lj.roomer.ui.adapters.HomeAdapter
import pro.lj.roomer.ui.app.AR
import pro.lj.roomer.ui.app.Dashboard
import pro.lj.roomer.ui.app.MainActivity
import pro.lj.roomer.util.BounceEdgeEffectFactory


class Home : Fragment(R.layout.home) {
    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!
    lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var storageRef : FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onPause() {
        binding.blurLayout.pauseBlur()
        super.onPause()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBlur()
        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance()
        setupHomeRecyclerView()
        fireStore.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val itemList : MutableList<Item> = arrayListOf()
                for (document in result) {

                    itemList.add(
                        document.toObject(Item::class.java)
                    )

                    homeAdapter.differ.submitList(itemList)
                    Log.d("TABY", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TABY", "Error getting documents.", exception)
            }
        homeAdapter.setOnItemLongClickListener { it, clipText ->
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)
            val dragShadowBuilder = DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, DRAG_FLAG_OPAQUE)
        }
        homeAdapter.setOnItemClickListener {
            findNavController().navigate(R.id.action_home_to_productDetail)
        }
        binding.Linear2.setOnDragListener(dragListener)
    }

    private fun setupBlur(){
        binding.blurLayout.startBlur()
        binding.blurLayout.visibility = INVISIBLE

    }

    private fun reduceAlpha(){
        binding.blurLayout.startBlur()
        binding.etSearch.isEnabled = false
        binding.blurLayout.visibility = VISIBLE


    }


    private fun resetAlpha(){
        binding.etSearch.isEnabled = true
        binding.blurLayout.visibility = INVISIBLE

    }
    private fun signOut(){
        auth.signOut()

        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    private fun setupHomeRecyclerView(){
        homeAdapter =
                HomeAdapter()
        binding.homeRV.apply {
            adapter = homeAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
            edgeEffectFactory =
                    BounceEdgeEffectFactory()
        }
    }



    val dragListener = View.OnDragListener { view, event ->
        when(event.action){
            DragEvent.ACTION_DRAG_STARTED -> {
                reduceAlpha()
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED-> {
                view.invalidate()
                binding.tvDnD.typeface = Typeface.DEFAULT_BOLD
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                binding.tvDnD.typeface = Typeface.DEFAULT
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP-> {
                binding.tvDnD.typeface = Typeface.DEFAULT

                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                resetAlpha()
                Toast.makeText(activity, dragData, Toast.LENGTH_SHORT).show()

                view.invalidate()

                val v = event.localState as View
                val destination = view
                val intent = Intent(activity, AR::class.java)
                Log.d("TAGGG","heresss")
                startActivity(intent)
                true
            }
            DragEvent.ACTION_DRAG_ENDED-> {
                resetAlpha()
                view.invalidate()
                true
            }
            else-> {
                false
            }
        }
    }


}