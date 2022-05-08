package pro.lj.roomer.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import pro.lj.roomer.R
import pro.lj.roomer.databinding.ProfileBinding
import pro.lj.roomer.util.Status
import pro.lj.roomer.viewmodel.MainViewModel

class Profile : Fragment(R.layout.profile) {

    private var _binding: ProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val viewModel : MainViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchUser()
        viewModel.user.observe(viewLifecycleOwner, Observer {
            val data = it.data
            when(it.status){
                Status.LOADING ->{
                    //TODO
                }
                Status.SUCCESS->{
                    Log.d("babys","here")
                    binding.tvName.text = "Hey " + data?.name + "!"
                    binding.tvNumber.text = data?.number
                    binding.tvMail.text = data?.email
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.tvShoppingAddress.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_shoppingAddress)
            Log.d("CCC","WTFFFF")
        }
        binding.tvOrderHistory.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_orderInformation)
        }
    }
}