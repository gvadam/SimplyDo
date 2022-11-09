package com.example.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        binding.signUpTV.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.nextBtn.setOnClickListener {
            val email = binding.emailEdit.text.toString()
            val pass = binding.passwordEdit.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                loginUser(email, pass)
            }
            else {
                Toast.makeText(context, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, pass: String) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                navController.navigate(R.id.action_signInFragment_to_homeFragment)
            }
            else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
    }

}