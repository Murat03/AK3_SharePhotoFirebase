package com.muratipek.ak3_sharephotofirebase.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muratipek.ak3_sharephotofirebase.databinding.ActivityPhotoShareBinding
import java.util.*

class PhotoShareActivity : AppCompatActivity() {
    var selectedImage : Uri? = null
    var selectedBitmap : Bitmap? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore

    private lateinit var binding: ActivityPhotoShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()
    }
    fun shareButton(view: View){
        //depo işlemleri
        //UUID -> universal unique id
        val uuid = UUID.randomUUID()
        val imageName = "${uuid}.jpg"

        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)

        selectedImage?.let {
            imageReference.putFile(it).addOnSuccessListener { taskSnapshot ->
                val uploadedImageReference = FirebaseStorage.getInstance().reference.child("images").child(imageName)
                uploadedImageReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    val currentUserEmail = auth.currentUser!!.email.toString()
                    val userComment = binding.commentText.text.toString()
                    val date = Timestamp.now()
                //database
                    val postHashMap = hashMapOf<String, Any>()
                    postHashMap.put("imageurl", downloadUrl)
                    postHashMap.put("useremail", currentUserEmail)
                    postHashMap.put("usercomment", userComment)
                    postHashMap.put("date", date)

                    database.collection("Post").add(postHashMap).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(applicationContext, exception.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener{ exception ->
                Toast.makeText(applicationContext, exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }
    fun selectImage(view: View){
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }else{
            //izin alınmış resim seç
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1){
            if( grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //izin verildi
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 2)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            selectedImage = data.data
            selectedImage?.let { imageUri ->
                //if sdk >= 28
                val source = ImageDecoder.createSource(this.contentResolver, imageUri)
                selectedBitmap = ImageDecoder.decodeBitmap(source)
                binding.imageView4.setImageBitmap(selectedBitmap)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}