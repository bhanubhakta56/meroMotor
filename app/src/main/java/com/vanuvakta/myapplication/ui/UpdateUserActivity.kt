package com.vanuvakta.myapplication.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import com.bumptech.glide.Glide
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.entity.User
import com.vanuvakta.myapplication.repository.CompanyRepository
import com.vanuvakta.myapplication.repository.UserRepository
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class UpdateUserActivity : AppCompatActivity() {
    //declaring variables
    private lateinit var img_back: ImageView
    private lateinit var img_photo: CircleImageView
    private lateinit var et_first_name: EditText
    private lateinit var et_last_name: EditText
    private lateinit var rd_male: RadioButton
    private lateinit var rd_female: RadioButton
    private lateinit var rd_others: RadioButton
    private lateinit var et_email: EditText
    private lateinit var et_mobile: EditText
    private lateinit var btn_update: Button
    private lateinit var btn_change_photo: Button
    private var gender: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)
        //binding variables
        et_first_name = findViewById(R.id.et_first_name)
        et_last_name = findViewById(R.id.et_last_name)
        rd_male = findViewById(R.id.rd_male)
        rd_female = findViewById(R.id.rd_female)
        rd_others = findViewById(R.id.rd_others)
        et_email =findViewById(R.id.et_email)
        et_mobile =findViewById(R.id.et_mobile)
        btn_update = findViewById(R.id.btn_update)
        btn_change_photo = findViewById(R.id.btn_change_photo)
        img_back = findViewById(R.id.img_back)
        img_photo = findViewById(R.id.img_photo)

        img_back.setOnClickListener {
            finish()
        }

        val user = intent.getParcelableExtra<User>("user")!!
        et_first_name.setText("${user.first_name}")
        et_last_name.setText("${user.last_name}")
        et_email.setText("${user.email}")

        //check male
        if(user.gender=="Male"){
            rd_male.isChecked=true
        }

        //check female
        if(user.gender=="Female"){
            rd_male.isChecked=true
        }

        //check others
        if(user.gender=="Others"){
            rd_male.isChecked=true
        }

        //male
        rd_male.setOnClickListener {
            gender = rd_male.text.toString()
        }
        //female
        rd_female.setOnClickListener {
            gender = rd_female.text.toString()
        }
        //others
        rd_others.setOnClickListener {
            gender = rd_others.text.toString()
        }

//        loading image from server
        val imagePath = ServiceBuilder.loadImagePath() + user.profile   //product image
        if(imagePath!=""){
            Glide.with(this)
                .load(imagePath)
                .fitCenter()
                .into(img_photo)
        }


        btn_change_photo.setOnClickListener {
            loadPopUpMenu()
        }

        btn_update.setOnClickListener {
            val first_name = et_first_name.text.toString()
            val last_name = et_last_name.text.toString()
            val email = et_email.text.toString()
            val mobile = et_mobile.text.toString()
            val userData = User(_id = user._id, first_name = first_name, last_name = last_name, gender = gender, email = email, mobile = mobile)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepository = UserRepository()
                    val response = userRepository.updateUser(user._id,userData)
                    if(response.success == true){
                        //for image process
                        if(imageUrl!=null){
                            uploadImage(response.data!!._id!!)
                            finish()
                        }
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@UpdateUserActivity, "Updated", Toast.LENGTH_SHORT).show()
                        }
                        startActivity(Intent(this@UpdateUserActivity, LoginActivity::class.java))
                        finish()
                    }
                    else{
                        withContext(Dispatchers.Main) {
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@UpdateUserActivity, "", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }catch (ex: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@UpdateUserActivity, ex.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    //
    private fun uploadImage(id:String){
        if(imageUrl!=null){
            val file = File(imageUrl!!)
            val reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body = MultipartBody.Part.createFormData("file", file.name, reqFile)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepository =  UserRepository()
                    val response = userRepository.uploadUserImage(id, body)
                    if(response.success==true){
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@UpdateUserActivity, "Photo Uploaded", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }catch (ex: java.lang.Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@UpdateUserActivity,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(this, img_photo)
        popupMenu.menuInflater.inflate(R.menu.gallery_camera, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCamera ->
                    openCamera()
                R.id.menuGallery ->
                    openGallery()
            }
            true
        }
        popupMenu.show()
    }
    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = this.contentResolver
                val cursor =
                    contentResolver?.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                img_photo.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                img_photo.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }
        }
    }
    private fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
}