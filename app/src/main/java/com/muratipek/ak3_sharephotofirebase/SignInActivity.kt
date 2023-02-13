package com.muratipek.ak3_sharephotofirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.muratipek.ak3_sharephotofirebase.databinding.ActivitySigninBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            goFeedActivity()
        }
    }
    fun login(view: View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val currentUser = auth.currentUser?.email.toString()
                Toast.makeText(applicationContext, "Welcome ${currentUser}", Toast.LENGTH_LONG).show()

                goFeedActivity()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    fun signUp(view: View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            //asenkron
            if(task.isSuccessful){
                //diğer aktiviteye gidelim
                goFeedActivity()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    fun goFeedActivity(){
        val intent = Intent(applicationContext, FeedActivity::class.java)
        startActivity(intent)
        finish()
    }
}