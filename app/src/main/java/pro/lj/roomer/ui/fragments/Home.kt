package pro.lj.roomer.ui.fragments

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyModel.SpanSizeOverrideCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import pro.lj.roomer.*
import pro.lj.roomer.databinding.HomeBinding
import pro.lj.roomer.repositories.MainRepository
import pro.lj.roomer.ui.adapters.HomeAdapter
import pro.lj.roomer.ui.app.AR
import pro.lj.roomer.ui.app.Dashboard
import pro.lj.roomer.ui.app.MainActivity
import pro.lj.roomer.util.BounceEdgeEffectFactory
import pro.lj.roomer.util.Constants.CATEGORIES
import pro.lj.roomer.util.Status
import pro.lj.roomer.viewmodel.MainViewModel


class Home : Fragment(R.layout.home) {
    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!
    lateinit var auth: FirebaseAuth
    private lateinit var homeAdapter: HomeAdapter
    var selected: Int ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeAdapter =
                HomeAdapter()
    }

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

    private val viewModel : MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setupHomeRecyclerView()

//        viewModel.producutList.observe(viewLifecycleOwner, Observer {
//            when(it.status){
//                Status.LOADING ->{
//                    //TODO
//                }
//                Status.SUCCESS->{
//                    Log.d("babys","here")
//                    homeAdapter.differ.submitList(it.data)
//                }
//                Status.ERROR->{
//                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

//        homeAdapter.setOnItemLongClickListener { it, clipText ->
//            val item = ClipData.Item(clipText)
//            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
//            val data = ClipData(clipText, mimeTypes, item)
//            val dragShadowBuilder = DragShadowBuilder(it)
//            it.startDragAndDrop(data, dragShadowBuilder, it, DRAG_FLAG_OPAQUE)
//        }
//        homeAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("item",it)
//            }
//            findNavController().navigate(R.id.action_home_to_productDetail,bundle)
//        }

    }



    private fun signOut(){
        auth.signOut()

        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    private fun setupHomeRecyclerView(){

        binding.rvHome.withModels {
            iv { id(-1) }
            tv {
                id(-2)
                text("Category")
            }
            val deptModels = mutableListOf<CardBindingModel_>()
            CATEGORIES.forEachIndexed { index, item ->
                val colour = if(selected==index) R.color.date_selected else R.color.date_unselected

                deptModels.add(
                    CardBindingModel_()
                        .id(index)
                        .category(item.name)
                        .imgRes(item.img)
                        .onClickContent { _ ->
                            selected = index
                            this.requestModelBuild()
                        }
                        .cardColour(colour)

                )
            }

            CarouselModel_()
                .id("carousel2")
                .models(deptModels)
                .addTo(this);

            tv{
                id(-3)
                text("Top Sofa")
            }


            val arr = arrayListOf<Int>(1,2,3,4,5,6)

            this.spanCount = 2
            arr.forEachIndexed { index, i ->
                productCard {
                    id(100 + index)
                    onClickContent { _ ->
                        findNavController().navigate(R.id.action_home_to_productDetail)
//                        val intent = Intent(activity, AR::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(intent)
                    }
                   // spanSizeOverride { totalSpanCount, position, itemCount -> 2 }
                }
            }
        }

//        binding.homeRV.apply {
//            adapter = homeAdapter
//            layoutManager = GridLayoutManager(requireActivity(), 2)
//            edgeEffectFactory =
//                    BounceEdgeEffectFactory()
//
//
//        }
    }



//    val dragListener = View.OnDragListener { view, event ->
//        when(event.action){
//            DragEvent.ACTION_DRAG_STARTED -> {
//                reduceAlpha()
//                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
//            }
//            DragEvent.ACTION_DRAG_ENTERED-> {
//                view.invalidate()
//                binding.tvDnD.typeface = Typeface.DEFAULT_BOLD
//                true
//            }
//            DragEvent.ACTION_DRAG_LOCATION -> true
//            DragEvent.ACTION_DRAG_EXITED -> {
//                binding.tvDnD.typeface = Typeface.DEFAULT
//                view.invalidate()
//                true
//            }
//            DragEvent.ACTION_DROP-> {
//                binding.tvDnD.typeface = Typeface.DEFAULT
//
//                val item = event.clipData.getItemAt(0)
//                val dragData = item.text
//                resetAlpha()
//                Toast.makeText(activity, dragData, Toast.LENGTH_SHORT).show()
//
//                view.invalidate()
//
//                val v = event.localState as View
//                val destination = view
//                val intent = Intent(activity, AR::class.java)
//                startActivity(intent)
//                true
//            }
//            DragEvent.ACTION_DRAG_ENDED-> {
//                resetAlpha()
//                view.invalidate()
//                true
//            }
//            else-> {
//                false
//            }
//        }
//    }


}