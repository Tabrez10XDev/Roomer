package pro.lj.roomer.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import pro.lj.roomer.R
import pro.lj.roomer.databinding.OrderInformationBinding
import pro.lj.roomer.util.Status
import pro.lj.roomer.viewmodel.MainViewModel
import java.io.File

class OrderInformation : Fragment() {
    private var _binding: OrderInformationBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = OrderInformationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        Glide.with(requireContext())
                .load("https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/0,0,16,0,0/400x400?access_token=pk.eyJ1IjoibG93anVua2llIiwiYSI6ImNsMmhlajNuZDBjY3gzY256eGt0ZGpncTIifQ.wPCLsXXPdlP2dY365C-x7A")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.ivMap)
//        viewModel.fetchMapData()
//        viewModel.mapData.observe(viewLifecycleOwner, Observer {
//                    when(it.status){
//            Status.LOADING -> {
//                Log.d("LJ","jkjkjkj")
//                // NO OP
//            }
//            Status.SUCCESS ->{
//                it.data?.let { Log.d("LJ", "bodddddddddddd") }
//            }
//            Status.ERROR -> {
//                Log.d("LJ","lllllkkkkkjjjjj"+it.message.toString())
//
//                // TODO Show Error message
//            }
//        }
//        })
    }
}