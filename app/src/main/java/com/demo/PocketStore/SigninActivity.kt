package com.demo.PocketStore

import android.Manifest

import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Button
import com.demo.PocketStore.db.manager.EventDataManager
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.view.Window
import com.demo.PocketStore.R
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import com.demo.PocketStore.db.manager.AppDataManager
import java.util.ArrayList
import android.widget.ListView
import com.demo.PocketStore.adapter.AppListAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import android.widget.AdapterView
import com.demo.PocketStore.RatingActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.PagerAdapter
import android.widget.LinearLayout
import android.widget.ImageButton
import com.demo.PocketStore.db.manager.UserDataManager
import java.util.HashMap
import android.widget.SimpleAdapter
import android.widget.RelativeLayout
import com.demo.PocketStore.db.manager.RatingDataManager
import com.demo.PocketStore.adapter.RatingListAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import android.view.LayoutInflater
import android.view.ViewGroup
import com.demo.PocketStore.adapter.ManagerListAdapter
import com.demo.PocketStore.ResolveIssueActivity
import com.demo.PocketStore.SendMsgActivity
import com.demo.PocketStore.RecMsgActivity
import android.content.DialogInterface
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import android.widget.RatingBar
import com.demo.PocketStore.db.manager.MsgDataManager
import com.demo.PocketStore.adapter.MsgListAdapter
import com.demo.PocketStore.db.manager.IssueDataManager
import com.demo.PocketStore.adapter.IssueListAdapter
import android.widget.Spinner
import com.demo.PocketStore.db.manager.VolDataManager
import android.widget.ArrayAdapter
import java.util.stream.Collectors
import java.util.Locale
import androidx.appcompat.widget.AppCompatButton
import android.os.Build
import android.content.pm.PackageManager
import com.demo.PocketStore.HomeActivity
import com.demo.PocketStore.MainActivity
import com.demo.PocketStore.SignupActivity
import com.demo.PocketStore.SigninActivity
import com.demo.PocketStore.common.Config.curUser
import com.demo.PocketStore.db.bean.*

class SigninActivity : AppCompatActivity(), View.OnClickListener {
    var inputUsername: EditText? = null
    var inputPassword: EditText? = null
    var btnLogin: AppCompatButton? = null
    var linkSignup: TextView? = null
    private var mUserDataManager: UserDataManager? = null
    private val eventDataManager: EventDataManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        initViews()
        initData()
        if (mUserDataManager == null) {
            mUserDataManager = UserDataManager(this)
            mUserDataManager!!.openDataBase()
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

            for (str in permissions) {
                if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, REQUEST_CODE_CONTACT)
                    return
                }
            }
        }
    }


    fun login() {

        if (!validate()) {
            Toast.makeText(applicationContext, "input is invalid", Toast.LENGTH_SHORT).show()
            return
        }

        val username = inputUsername!!.text.toString().trim { it <= ' ' }
        val password = inputPassword!!.text.toString().trim { it <= ' ' }
        if ("admin" == username && "123" == password) {
            curUser = UserData(0, "admin", "123")
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext, "Admin Login successfully", Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }
        val userData = mUserDataManager!!.findUserByNameAndPwd(username, password)
        if (userData != null && "1" == userData.userStatus) {

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


    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }


    fun validate(): Boolean {

        var valid = true


        val email = inputUsername!!.text.toString().trim { it <= ' ' }
        val password = inputPassword!!.text.toString().trim { it <= ' ' }


        if (email.isEmpty()) {
            inputUsername!!.error = "email is empty"
            valid = false
        } else {
            inputUsername!!.error = null
        }

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