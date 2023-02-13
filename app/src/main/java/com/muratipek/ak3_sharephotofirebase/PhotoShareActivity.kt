package com.muratipek.ak3_sharephotofirebase

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.muratipek.ak3_sharephotofirebase.databinding.ActivityPhotoShareBinding

class PhotoShareActivity : AppCompatActivity() {
    var selecetedImage : Uri? = null
    var selectedBitmap : Bitmap? = null

    private lateinit var binding: ActivityPhotoShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoShareBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun shareButton(view: View){

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
            selecetedImage = data.data
            selecetedImage?.let { imageUri ->
                //if sdk >= 28
                val source = ImageDecoder.createSource(this.contentResolver, imageUri)
                selectedBitmap = ImageDecoder.decodeBitmap(source)
                binding.imageView4.setImageBitmap(selectedBitmap)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}