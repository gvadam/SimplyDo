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
import com.example.todoapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        binding.signInTV.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        binding.nextBtn.setOnClickListener {
            val email = binding.emailEdit.text.toString()
            val pass = binding.passwordEdit.text.toString()
            val pass2 = binding.passwordEdit2.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && pass2.isNotEmpty()) {
                if (pass == pass2) {
                    registerUser(email, pass)
                } else {
                    Toast.makeText(context, "Password is not same", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun registerUser(email: String, pass: String) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful)
                navController.navigate(R.id.action_signUpFragment_to_homeFragment)
            else
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
    }

}