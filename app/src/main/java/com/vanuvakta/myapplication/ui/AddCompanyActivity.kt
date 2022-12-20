package com.vanuvakta.myapplication.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.entity.Company
import com.vanuvakta.myapplication.repository.CompanyRepository
import com.vanuvakta.myapplication.repository.ProductRepository
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
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddCompanyActivity : AppCompatActivity() {
    private lateinit var img_photo: ImageView
    private lateinit var et_companyName: EditText
    private lateinit var et_phoneNumber: EditText
    private lateinit var et_email: EditText
    private lateinit var et_address: EditText
    private lateinit var btn_add_company: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_company)
        img_photo = findViewById(R.id.img_photo)
        et_companyName = findViewById(R.id.companyName)
        et_phoneNumber = findViewById(R.id.phoneNumber)
        et_email = findViewById(R.id.email)
        et_address = findViewById(R.id.address)
        btn_add_company = findViewById(R.id.btn_add_company)

        img_photo.setOnClickListener{
            loadPopUpMenu()
        }

        btn_add_company.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
//                    if(imageUrl!=null){
                        val companyName = et_companyName.text.toString()
                        val contact = et_phoneNumber.text.toString()
                        val email = et_email.text.toString()
                        val address = et_address.text.toString()
                        val company = Company(companyName=companyName, contact = contact, email = email, address = address)
                        val companyRepository = CompanyRepository()
                        val companyResponse = companyRepository.addCompany(company)
                        if(companyResponse.success==true){
                            //for image process
                            if(imageUrl!=null){
                                uploadImage(companyResponse.company!!._id!!)
                            }
                            startActivity(Intent(this@AddCompanyActivity, AddProductActivity::class.java))
                            finish()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@AddCompanyActivity,
                                    companyResponse.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@AddCompanyActivity,
                                    companyResponse.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
//                    }
//                    else{
//                        withContext(Dispatchers.Main) {
//                            val builder = androidx.appcompat.app.AlertDialog.Builder(this@AddCompanyActivity)
//                            builder.setTitle("No Photo")
//                            builder.setMessage("PLEASE UPLOAD YOUR COMPANY PHOTO TO CONTINUE")
//                            builder.setIcon(android.R.drawable.sym_def_app_icon)
//                            builder.setPositiveButton("OK") { _, _ ->
//                            }
//                            val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
//                            alertDialog.setCancelable(false)
//                            alertDialog.show()
//                        }
//
//                    }

                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddCompanyActivity,
                            "Error : $ex", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    //upload image function
    private fun uploadImage(id:String){
        if(imageUrl!=null){
            val file = File(imageUrl!!)
            val reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body = MultipartBody.Part.createFormData("file", file.name, reqFile)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val companyRepository =  CompanyRepository()
                    val response = companyRepository.uploadImage(id, body)
                    if(response.success==true){
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AddCompanyActivity, response.message.toString(), Toast.LENGTH_SHORT)
                                .show()
                            }
                    }
                }catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
//                        Log.d("Mero Error ", ex.localizedMessage!!)
                        Toast.makeText(
                            this@AddCompanyActivity,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    ///loading popup menu and more
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
        if (resultCode == RESULT_OK) {
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

    private fun bitmapToFile(imageBitmap: Bitmap, fileNameToSave: String): File? {
        var file: File?=null
        return try {
            file = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()+File.separator+fileNameToSave)
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) //You can save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        }catch(e:Exception){
            e.printStackTrace()
            file //it will return null
        }
    }

}