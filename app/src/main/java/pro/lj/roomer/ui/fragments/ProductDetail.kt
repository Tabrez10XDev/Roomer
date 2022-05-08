package pro.lj.roomer.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import pro.lj.roomer.R
import pro.lj.roomer.databinding.LoginBinding
import pro.lj.roomer.databinding.ProductDetailBinding
import pro.lj.roomer.ui.app.Dashboard
import pro.lj.roomer.viewmodel.MainViewModel

class ProductDetail : Fragment(R.layout.product_detail) {

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
        var args : ProductDetailArgs = ProductDetailArgs.fromBundle(this.requireArguments())
        var item = args.item
        binding.ratingBar.rating = item.stars
        binding.tvName.text = item.name
        binding.tvPrice.text = "₹" + item.price
        viewModel.fetchImage(item.imageUri, binding.ivProduct)


    }
}