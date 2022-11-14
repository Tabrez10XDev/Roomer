package pro.lj.roomer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.CarouselModel_
import com.google.firebase.auth.FirebaseAuth
import pro.lj.roomer.*
import pro.lj.roomer.data.Item
import pro.lj.roomer.databinding.HomeBinding
import pro.lj.roomer.ui.adapters.MeasureAdapter
import pro.lj.roomer.ui.app.MainActivity
import pro.lj.roomer.util.Constants.CATEGORIES
import pro.lj.roomer.util.Status
import pro.lj.roomer.viewmodel.MainViewModel

class Home : Fragment(R.layout.home) {
    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!
    lateinit var auth: FirebaseAuth
    private lateinit var measureAdapter: MeasureAdapter
    var selected: Int ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        measureAdapter =
                MeasureAdapter()
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


        viewModel.producutList.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.LOADING ->{
                    //TODO
                }
                Status.SUCCESS->{
                  //  homeAdapter.differ.submitList(it.data)
                    setupHomeRecyclerView(it.data)
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                }
            }
        })

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

    private fun setupHomeRecyclerView(data: List<Item>?) {
        binding.rvHome.withModels {
            val ivModels = mutableListOf<IvBindingModel_>()

            val ivModel = IvBindingModel_()
                .id("-1")
                .spanSizeOverride { totalSpanCount, position, itemCount -> 1 }
            ivModels.add(ivModel)
            CarouselModel_()
                .id("carousel3")
                .models(ivModels)
                .addTo(this);
//            iv {
//                id(-1)
//                spanCount = 1
//            }
            tv {
                id(-2)
                text("Category")

            }
            val deptModels = mutableListOf<CardBindingModel_>()
            CATEGORIES.forEachIndexed { index, item ->
                val colour = if(selected==index) R.color.date_selected else R.color.date_unselected
                val textColour = if(selected==index) R.color.white else R.color.text_unselected

                deptModels.add(
                    CardBindingModel_()
                        .id(index)
                        .category(item.name)
                        .imgRes(item.img)
                        .textColour(textColour)
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

            val TvModels = mutableListOf<TvBindingModel_>()

            val TvModel = TvBindingModel_()
                .id("-10")
                .text("Top Sofa")
                .spanSizeOverride { totalSpanCount, position, itemCount -> 1 }
            TvModels.add(TvModel)
            CarouselModel_()
                .id("carousel4")
                .models(TvModels)
                .addTo(this);



            data?.forEachIndexed { index, i ->
                productCard {
                    id(100 + index)
                    name(i.name)
                    price("₹" + i.price)
                    img(i.imageUri)

                //    spanSizeOverride { totalSpanCount, position, itemCount -> totalSpanCount/2  }
                    onClickContent { _ ->
                        val bundle = Bundle().apply {
                            putSerializable("item",i)
                        }
                        findNavController().navigate(R.id.action_home_to_productDetail,bundle)
                    }
                }
            }
        }


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