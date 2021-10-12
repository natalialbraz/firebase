package com.example.aulasfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.aulasfirebase.databinding.ActivityHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.config.GservicesValue.value
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

/*data class Info(
    val etName: String? = null,
    val etEmail: String? = null
        ) {}*/

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDeslogar.setOnClickListener {
            Firebase.auth.signOut()
            logOut()
        }
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message").child("config")

        val etName = binding.etName
        val etEmail = binding.etEmail
        val btnSave = binding.btSave
        val tvNameAtual = binding.tvNameLoad
        val tvEmailAtual = binding.tvEmailLoad


        btnSave.setOnClickListener {
            myRef.child("config").setValue(etName.text.toString())
            Toast.makeText(this@HomeActivity, "Configurações Salvas", Toast.LENGTH_SHORT).show()
        }


        // Read from the database
        myRef.child("config").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue<String>()
                tvNameAtual.text = value
                //tvEmailAtual.text = value?.etEmail
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Ocorreu um erro", Toast.LENGTH_SHORT).show()
                Log.w("TAG", "Failed to read value.", error.toException())
            }

        })

    }
    private fun logOut(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(this, gso).signOut()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}