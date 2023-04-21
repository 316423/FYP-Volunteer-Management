package com.demo.PocketStore

import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import android.widget.TextView
import com.demo.PocketStore.UserDataManager
import android.os.Bundle
import com.demo.PocketStore.R
import android.widget.Toast
import android.content.Intent
import com.demo.PocketStore.SigninActivity

class SignupActivity : AppCompatActivity(), View.OnClickListener {
    //@BindView(R.id.input_name)
    var _nameText: EditText? = null
    var _emailText: EditText? = null
    var _phoneText: EditText? = null

    //@BindView(R.id.input_password)
    var _passwordText: EditText? = null

    // @BindView(R.id.input_reEnterPassword)
    var _reEnterPasswordText: EditText? = null
    var _signupButton: AppCompatButton? = null
    var _linkSignin: TextView? = null
    private var mUserDataManager //the class for user's data management
            : UserDataManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        initViews()
    }

    private fun initViews() {
        _nameText = findViewById<View>(R.id.input_username) as EditText
        _emailText = findViewById<View>(R.id.input_email) as EditText
        _phoneText = findViewById<View>(R.id.input_phone) as EditText
        _reEnterPasswordText = findViewById<View>(R.id.input_reEnterPassword) as EditText
        _passwordText = findViewById<View>(R.id.input_password) as EditText
        _signupButton = findViewById<View>(R.id.btn_signup) as AppCompatButton
        _linkSignin = findViewById<View>(R.id.link_login) as TextView
        _signupButton!!.setOnClickListener(this)
        _linkSignin!!.setOnClickListener(this)
    }

    fun signup() {
        //determine whether it is valid or not
        if (!validate()) {
            onSignupFailed(0)
            return
        }
        _signupButton!!.isEnabled = false

        //get input data
        val username = _nameText!!.text.toString()
        val email = _emailText!!.text.toString()
        val phone = _phoneText!!.text.toString()
        val password = _passwordText!!.text.toString()
        //String repwd = _passwordText.getText().toString();
        val mUser = UserData(username, password, email, phone)
        if (mUserDataManager == null) {
            mUserDataManager = UserDataManager(this)
            mUserDataManager!!.openDataBase() //open the database
        }
        if (!mUserDataManager!!.checkUserDataValid(username, email)) {
            Toast.makeText(
                applicationContext,
                "register failed: username or email is already registered", Toast.LENGTH_SHORT
            ).show()
            return
        }
        val flag = mUserDataManager!!.insertUserData(mUser) //the information of the register user
        if (flag == -1L) {
            Toast.makeText(applicationContext, "register failed...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                applicationContext,
                "register successfully,please login!",
                Toast.LENGTH_SHORT
            ).show()
            //Toast.makeText(getApplicationContext(),"save file successfully", Toast.LENGTH_SHORT).show();
            val intent = Intent(this, SigninActivity::class.java)
            intent.putExtra("name", username)
            intent.putExtra("pwd", password)
            startActivity(intent)
            finish()
        }
    }

    /**
     * sign up failed, enabled the buttton
     * different parameters run different toasts
     */
    fun onSignupFailed(i: Int) {
        if (i == 1) {
            Toast.makeText(baseContext, "email is existed", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(baseContext, "register failed", Toast.LENGTH_LONG).show()
        }
        _signupButton!!.isEnabled = true
    }

    /**
     * @return input data is valid ot not
     */
    fun validate(): Boolean {
        var valid = true
        //    get data
        val name = _nameText!!.text.toString()
        val email = _nameText!!.text.toString()
        val phone = _passwordText!!.text.toString()
        val password = _passwordText!!.text.toString()
        val reEnterPassword = _reEnterPasswordText!!.text.toString()
        //determine the account is valid or not
        if (name.isEmpty()) {
            _nameText!!.error = "name is empty"
            valid = false
        } else {
            _nameText!!.error = null
        }
        if (email.isEmpty()) {
            _emailText!!.error = "email is empty"
            valid = false
        } else {
            _emailText!!.error = null
        }
        if (phone.isEmpty()) {
            _phoneText!!.error = "phone is empty"
            valid = false
        } else {
            _phoneText!!.error = null
        }
        //determine password
        if (password.isEmpty()) {
            _passwordText!!.error = "password is empty"
            valid = false
        } else {
            _passwordText!!.error = null
        }
        //determine re-password
        if (reEnterPassword.isEmpty() || reEnterPassword != password) {
            _reEnterPasswordText!!.error = "password is different"
            valid = false
        } else {
            _reEnterPasswordText!!.error = null
        }
        return valid
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_signup -> signup()
            R.id.link_login -> {
                //click login, switch to login page
                val intent = Intent(applicationContext, SigninActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}