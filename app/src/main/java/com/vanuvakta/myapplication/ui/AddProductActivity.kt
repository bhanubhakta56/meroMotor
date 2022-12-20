package com.vanuvakta.myapplication.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.vanuvakta.myapplication.R
import com.vanuvakta.myapplication.entity.Product
import com.vanuvakta.myapplication.notification.MeroMotorNotification
import com.vanuvakta.myapplication.repository.ProductRepository
import com.vanuvakta.myapplication.repository.UserRepository
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

class AddProductActivity : AppCompatActivity() {
    private lateinit var productName: EditText
    private lateinit var price:EditText
    private lateinit var brand:EditText
    private lateinit var description:EditText
    private lateinit var qty:EditText
    private lateinit var btn_add: Button
    private lateinit var photo : ImageView
    private  var message = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        productName = findViewById(R.id.productName)
        price = findViewById(R.id.price)
        brand = findViewById(R.id.brand)
        description = findViewById(R.id.description)
        btn_add = findViewById(R.id.btn_add)
        photo = findViewById(R.id.photo)
        qty = findViewById(R.id.qty)

        photo.setOnClickListener{
            loadPopUpMenu()
        }

        btn_add.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
//                    if (imageUrl!=null) {
                        val productName = productName.text.toString()
                        val brand = brand.text.toString()
                        val price = price.text.toString().toInt()
                        val description =  description.text.toString()
                        val available = qty.text.toString().toInt()

                        val product = Product(_id = "it will not be added in data base", productName = productName, brand = brand, price = price, description = description, available = available)

                        val productRepository = ProductRepository()
                        val productResponse = productRepository.addProduct(product)

                        if(productResponse.success==true) {
                            //for image process
                            if (imageUrl != null) {
                                uploadImage(productResponse.data!!._id)
                            }
                            sendNotification()
                            startActivity(Intent(this@AddProductActivity, MyCompanyActivity::class.java))
                            finish()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@AddProductActivity,
                                        productResponse.message.toString(), Toast.LENGTH_SHORT).show()
                            }
//                        }
                        }
//                    else {
//                        withContext(Dispatchers.Main) {
//                            val builder = androidx.appcompat.app.AlertDialog.Builder(this@AddProductActivity)
//                            builder.setTitle("No Photo")
//                            builder.setMessage("PLEASE UPLOAD PRODUCT PHOTO TO CONTINUE")
//                            builder.setIcon(android.R.drawable.sym_def_app_icon)
//                            builder.setPositiveButton("OK") { _, _ ->
//                            }
//                            val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
//                            alertDialog.setCancelable(false)
//                            alertDialog.show()
//                        }
//                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddProductActivity,
                            "Error : $ex", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun sendNotification(){
        val notificationManager = NotificationManagerCompat.from(this)

        val meroMotorNotification = MeroMotorNotification(this)
        meroMotorNotification.createNotificationChannels()

        val notification = NotificationCompat.Builder(this, meroMotorNotification.CHANNEL_1)
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle("Product Added")
            .setContentText("New Product has been added in your account")
            .setColor(Color.BLUE)
            .build()
        notificationManager.notify(1, notification)
    }

    private fun uploadImage(productId: String) {
        if (imageUrl != null) {
            val file = File(imageUrl!!)
            val reqFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body =
                MultipartBody.Part.createFormData("file", file.name, reqFile)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val productRepository = ProductRepository()
                    val response = productRepository.uploadImage(productId, body)
                    if (response.success == true) {
                        message = "success"
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AddProductActivity, body.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Mero Error ", ex.localizedMessage)
                        Toast.makeText(
                            this@AddProductActivity,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(this, photo)
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
                photo.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                photo.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
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