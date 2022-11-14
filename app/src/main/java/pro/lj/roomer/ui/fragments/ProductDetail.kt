package pro.lj.roomer.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.airbnb.epoxy.CarouselModel_
import com.google.firebase.auth.FirebaseAuth
import pro.lj.roomer.CardBindingModel_
import pro.lj.roomer.ProductCardBindingModel_
import pro.lj.roomer.R
import pro.lj.roomer.databinding.LoginBinding
import pro.lj.roomer.databinding.ProductDetailBinding
import pro.lj.roomer.ui.app.Dashboard
import pro.lj.roomer.util.Constants
import pro.lj.roomer.viewmodel.MainViewModel

class ProductDetail : Fragment() {

    private var _binding: ProductDetailBinding? = null
    lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = ProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private val viewModel : MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDetails.withModels {
            val deptModels = mutableListOf<ProductCardBindingModel_>()
            Constants.CATEGORIES.forEachIndexed { index, item ->

                deptModels.add(
                    ProductCardBindingModel_()
                        .id(index)

                )
            }

            CarouselModel_()
                .id("carousel2")
                .models(deptModels)
                .addTo(this);
        }
//        var args : ProductDetailArgs = ProductDetailArgs.fromBundle(this.requireArguments())
//        var item = args.item



    }
}