package pro.lj.roomer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import pro.lj.roomer.R
import pro.lj.roomer.databinding.ShoppingAddressBinding


class ShoppingAddress : Fragment(R.layout.shopping_address) {
    private var _binding: ShoppingAddressBinding? = null
    private val binding get() = _binding!!

    private val COUNTRIES = arrayOf(
            "Belgium", "France", "Italy", "Germany", "Spain"
    )

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = ShoppingAddressBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(),
                android.R.layout.simple_dropdown_item_1line, COUNTRIES)
        binding.outlinedExposedDropdownEditable.setAdapter(adapter)
    }



}