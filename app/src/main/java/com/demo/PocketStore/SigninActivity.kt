package com.demo.PocketStore

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import android.widget.TextView
import com.demo.PocketStore.UserDataManager
import com.demo.PocketStore.EventDataManager
import android.os.Bundle
import com.demo.PocketStore.R
import android.os.Build
import android.content.pm.PackageManager
import android.widget.Toast
import android.content.Intent
import com.demo.PocketStore.Config.curUser
import com.demo.PocketStore.HomeActivity
import com.demo.PocketStore.MainActivity
import com.demo.PocketStore.SignupActivity
import com.demo.PocketStore.SigninActivity

class SigninActivity : AppCompatActivity(), View.OnClickListener {
    var inputUsername: EditText? = null
    var inputPassword: EditText? = null
    var btnLogin: AppCompatButton? = null
    var linkSignup: TextView? = null
    private var mUserDataManager //the class for user data management
            : UserDataManager? = null
    private val eventDataManager: EventDataManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        initViews()
        initData()
        if (mUserDataManager == null) {
            mUserDataManager = UserDataManager(this)
            mUserDataManager!!.openDataBase() //open the database
        }
        //        if (eventDataManager==null){
//            eventDataManager=new EventDataManager(this);
//            eventDataManager.openDataBase();
//        }
    }

    private fun initViews() {
        btnLogin = findViewById<View>(R.id.btn_login) as AppCompatButton
        inputUsername = findViewById<View>(R.id.input_username) as EditText
        inputPassword = findViewById<View>(R.id.input_password) as EditText
        linkSignup = findViewById<View>(R.id.link_signup) as TextView
        btnLogin!!.setOnClickListener(this)
        inputUsername!!.setOnClickListener(this)
        inputPassword!!.setOnClickListener(this)
        linkSignup!!.setOnClickListener(this)
    }

    fun initData() {
        /**
         * Dynamically obtaining permissions，Android 6.0 new features，
         * some protection authority，need declaration from AndroidManifest，also need the the following code
         */
        if (Build.VERSION.SDK_INT >= 23) {
            val REQUEST_CODE_CONTACT = 101
            val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_LOGS
            )
            //verify if permissions are granted
            for (str in permissions) {
                if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //request permission
                    requestPermissions(permissions, REQUEST_CODE_CONTACT)
                    return
                }
            }
        }
    }

    /**
     * login method
     */
    fun login() {
        //if the input content is invalid, then return directly and show errors
        if (!validate()) {
            Toast.makeText(applicationContext, "input is invalid", Toast.LENGTH_SHORT).show()
            return
        }
        //get input data
        val username = inputUsername!!.text.toString().trim { it <= ' ' }
        val password = inputPassword!!.text.toString().trim { it <= ' ' }
        if ("admin" == username && "123" == password) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext, "Admin Login successfully", Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }
        val userData = mUserDataManager!!.findUserByNameAndPwd(username, password)
        if (userData != null && "1" == userData.userStatus) {
            //return 1 when both username and password are correct
            val intent = Intent(this, MainActivity::class.java)
            curUser = userData
            startActivity(intent)
            Toast.makeText(applicationContext, "Login successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else if (userData != null && "0" == userData.userStatus) {
            Toast.makeText(
                applicationContext,
                "Waiting for administrator review!",
                Toast.LENGTH_SHORT
            ).show()
            // startActivity(new Intent(this, RegisterActivity.class));
        } else {
            Toast.makeText(applicationContext, "Login failed！", Toast.LENGTH_SHORT).show()
            // startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    /**
     * override the the function of return key
     */
    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

    /**
     * @return determine whether this account is valid or not
     */
    fun validate(): Boolean {
        //set initial value, default to valid
        var valid = true

        //get input data
        val email = inputUsername!!.text.toString().trim { it <= ' ' }
        val password = inputPassword!!.text.toString().trim { it <= ' ' }

        //determine account
        if (email.isEmpty()) {
            inputUsername!!.error = "email is empty"
            valid = false
        } else {
            inputUsername!!.error = null
        }
        //determine password
        if (password.isEmpty()) {
            inputPassword!!.error = "password is empty"
            valid = false
        } else {
            inputPassword!!.error = null
        }
        return valid
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login -> login()
            R.id.link_signup -> {
                val intent = Intent(applicationContext, SignupActivity::class.java)
                startActivityForResult(intent, REQUEST_SIGNUP)
                finish()
            }
        }
    }

    companion object {
        private const val REQUEST_SIGNUP = 0
    }
}