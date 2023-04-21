package com.demo.PocketStore

import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Button
import com.demo.PocketStore.EventDataManager
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.view.Window
import com.demo.PocketStore.R
import android.app.Activity
import android.content.Intent
import android.util.Log
import com.demo.PocketStore.EventData
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

//add
class AddEventActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "AddEventActivity"
    var toolbar: Toolbar? = null
    var input_title: EditText? = null
    var input_desc: EditText? = null
    var input_date: EditText? = null
    var input_max: EditText? = null
    var input_cur: EditText? = null
    var input_dur: EditText? = null
    var input_loc: EditText? = null
    var input_skills: EditText? = null
    private val modifyid = -1
    private var btnOk: Button? = null
    private var mUserDataManager: EventDataManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        // Config the direction of screen of the Activity
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // Hide ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addevent)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar!!.title = "Add Event"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { // return message and update the data of the previous Activity
            setResult(RESULT_OK, Intent())
            finish()
        }
        initViews()
        if (mUserDataManager == null) {
            mUserDataManager = EventDataManager(this)
            mUserDataManager!!.openDataBase() // create local database
        }
    }

    private fun initViews() {
        btnOk = findViewById<View>(R.id.btn_ok) as Button
        input_title = findViewById(R.id.input_title)
        input_cur = findViewById(R.id.input_cur)
        input_dur = findViewById(R.id.input_dur)
        input_date = findViewById(R.id.input_date)
        input_desc = findViewById(R.id.input_desc)
        input_loc = findViewById(R.id.input_loc)
        input_max = findViewById(R.id.input_max)
        input_skills = findViewById(R.id.input_skills)
      //  input_date.setOnClickListener(this)
        btnOk!!.setOnClickListener(this)
        val intent = intent
        val getid = intent.getStringExtra("userid")
        if (getid != null) {
            initdata(intent)
        }
    }

    private fun initdata(intent: Intent) {}
    override fun onClick(v: View) {
        if (modifyid == -1) {
            Log.e(TAG, "current:" + input_cur!!.text.toString())
            val mUser = EventData(
                input_title!!.text.toString(),
                input_desc!!.text.toString(),
                input_date!!.text.toString(),
                Config.curUser!!.userId,
                Integer.valueOf(input_max!!.text.toString()),
                Integer.valueOf(input_cur!!.text.toString()),
                input_dur!!.text.toString(),
                input_loc!!.text.toString(),
                input_skills!!.text.toString()
            )
            mUserDataManager!!.openDataBase()
            val flag = mUserDataManager!!.insertData(mUser)
            if (flag == -1L) {
                Toast.makeText(applicationContext, "add failed....", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "add successfully!", Toast.LENGTH_SHORT).show()
                //  startActivity(new Intent(this, ShowUserInfoActivity.class));
                finish()
            }
        } else {
            val mUser = EventData(
                input_title!!.text.toString(),
                input_desc!!.text.toString(),
                input_date!!.text.toString(),
                Config.curUser!!.userId,
                Integer.valueOf(input_max!!.text.toString()),
                Integer.valueOf(input_cur!!.text.toString()),
                input_dur!!.text.toString(),
                input_loc!!.text.toString(),
                input_skills!!.text.toString()
            )
            mUserDataManager!!.openDataBase()
            val flag = mUserDataManager!!.updateDataById(mUser, modifyid)
            if (flag) {
                Toast.makeText(applicationContext, "update successfully!", Toast.LENGTH_SHORT)
                    .show()
                //  startActivity(new Intent(this, ShowUserInfoActivity.class));
                finish()
                // Toast.makeText(getApplicationContext(),"update failed", Toast.LENGTH_SHORT).show();
            } else {
            }
        }
    }
}