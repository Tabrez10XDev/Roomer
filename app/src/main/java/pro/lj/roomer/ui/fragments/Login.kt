package pro.lj.roomer.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pro.lj.roomer.R
import pro.lj.roomer.databinding.LoginBinding
import pro.lj.roomer.ui.app.Dashboard

class Login : Fragment(R.layout.login) {

    private var _binding: LoginBinding? = null
    lateinit var auth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
      //  hidebar()
            binding.btnLogin.setOnClickListener {
                hideKeyboard()
                loginUser()
            }
            binding.btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_register)
            }
    }

    private fun hideKeyboard(){
        val view = activity?.currentFocus
        view?.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }



    private fun loginUser() {
        //showbar()
        val email = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        if( email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                      //  hidebar()
                        checkLoggedInState()
                    }.addOnFailureListener {
                       // hidebar()
                        Toast.makeText(activity, "Invalid Credentials", Toast.LENGTH_SHORT).show()

                    }.addOnCanceledListener {
                       // hidebar()
                        Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show()

                    }

                }
                catch (e : Exception){
                    withContext(Dispatchers.Main){
                        //hidebar()
                        Toast.makeText(activity,e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        else{
            Toast.makeText(activity,"Invalid Credentials", Toast.LENGTH_SHORT).show()
          //  hidebar()
        }
    }



    private fun checkLoggedInState(){
        if(auth.currentUser != null){
            Toast.makeText(activity,"Logged in as " + auth.currentUser?.displayName, Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()

        }
    }

//    private fun hidebar(){
//        progresslog.visibility = View.INVISIBLE
//        blogin.isEnabled = true
//        bsignup.isEnabled = true
//    }
//
//    private fun showbar(){
//        progresslog.visibility = View.VISIBLE
//        blogin.isEnabled = false
//        bsignup.isEnabled = false
//    }

}