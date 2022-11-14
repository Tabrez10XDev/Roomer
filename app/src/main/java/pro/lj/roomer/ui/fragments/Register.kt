package pro.lj.roomer.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pro.lj.roomer.R
import pro.lj.roomer.data.User
import pro.lj.roomer.databinding.RegisterBinding
import pro.lj.roomer.ui.app.Dashboard
import pro.lj.roomer.ui.app.MainActivity

class Register : Fragment(R.layout.register) {

    private var _binding: RegisterBinding? = null
    lateinit var auth: FirebaseAuth
    private var fireStore : FirebaseFirestore = FirebaseFirestore.getInstance()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding.btnSignup.setOnClickListener {
            hideKeyboard()
            registerUser()

        }

        binding.tvLogin.setOnClickListener {
            findNavController().popBackStack()
        }
//        binding.ibtnGoogle.setOnClickListener {
//            (activity as MainActivity).gSignIn()
//        }
    }
    private fun hideKeyboard(){
        val view = activity?.currentFocus
        view?.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
    private fun checkLoggedInState(){
        if(auth.currentUser != null){
            val intent = Intent(activity, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()

        }
    }


    private fun registerUser() {
        //showbar()
        val name = binding.editTextName.text.toString()
        val number = binding.editTextPhone.text.toString()
        val password = binding.editTextPassword.text.toString()
        val email = binding.editTextMail.text.toString()
        val user = User(name, email, number)

        if( email.isNotEmpty() && password.isNotEmpty()){
            Log.d("tsbrez","not")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                        Log.d("tsbrez","Pass")

                        fireStore.collection("users").document(auth.uid!!).set(user)
                        Toast.makeText(activity,"Account created",Toast.LENGTH_SHORT).show()
                        checkLoggedInState()

                    }.addOnFailureListener(){
                        Log.d("tsbrez","Fail")

                        Toast.makeText(activity,it.message,Toast.LENGTH_SHORT).show()
                        //hidebar()
                    }.addOnCompleteListener {
                        Log.d("tsbrez","compp")

                    }.addOnCanceledListener {
                        Log.d("tsbrez","cancel")

                    }


                }
                catch (e : Exception){

                    withContext(Dispatchers.Main){
                        //hidebar()

                        Toast.makeText(activity,e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}