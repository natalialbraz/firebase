package com.example.aulasfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toFile
import com.example.aulasfirebase.databinding.ActivityStorageBinding
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class StorageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStorageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btUpload.setOnClickListener {
            val intent = Intent()
            // filtra os tipos de arquivos e suas extensoes
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, GET_IMAGE_REQUEST)
        }
    }

    private fun loadProfileImageFromStorage(){
        val firebase = FirebaseStorage.getInstance()
        val storage = firebase.getReference("uploads")
        storage.child("profile.jpg").downloadUrl.addOnSuccessListener {
            Picasso.get()
                .load(it)
                .error(R.drawable.ic_launcher_background)
                .into(binding.ivPerfil)
        }
    }

    override fun onActivityResult(request: Int, code: Int, intent: Intent?) {
        super.onActivityResult(request, code, intent)

        // checa se está tudo ok após selecionar a imagem
        if (code == RESULT_OK && request == GET_IMAGE_REQUEST && intent?.data != null){
            val imageURI = intent.data!!

            imageURI.run {
                binding.ivPerfil.setImageURI(this)

                val firebase = FirebaseStorage.getInstance()
                val storage = firebase.getReference("uploads")
                val fileReference = storage.child("profile.jpg")

                fileReference.putFile(this)
                    .addOnSuccessListener {
                        Toast.makeText(this@StorageActivity, "Upload com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(this@StorageActivity, "Upload falhou!", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
const val GET_IMAGE_REQUEST = 1