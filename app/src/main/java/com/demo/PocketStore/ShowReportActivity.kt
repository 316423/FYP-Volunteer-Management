package com.demo.PocketStore

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
import android.content.Context
import android.content.Intent
import android.util.Log
import com.demo.PocketStore.db.bean.EventData
import android.widget.Toast
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import com.demo.PocketStore.db.manager.AppDataManager
import com.demo.PocketStore.db.bean.AppData
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
import com.demo.PocketStore.db.bean.RatingData
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
import com.demo.PocketStore.db.bean.MsgData
import com.demo.PocketStore.adapter.MsgListAdapter
import com.demo.PocketStore.db.manager.IssueDataManager
import com.demo.PocketStore.db.bean.IssueData
import com.demo.PocketStore.adapter.IssueListAdapter
import android.widget.Spinner
import com.demo.PocketStore.db.bean.Volunteer
import com.demo.PocketStore.db.manager.VolDataManager
import android.widget.ArrayAdapter
import java.util.stream.Collectors
import java.util.Locale
import androidx.appcompat.widget.AppCompatButton
import android.os.Build
import android.content.pm.PackageManager
import androidx.appcompat.widget.Toolbar
import com.demo.PocketStore.HomeActivity
import com.demo.PocketStore.MainActivity
import com.demo.PocketStore.SignupActivity
import com.demo.PocketStore.SigninActivity
import com.demo.PocketStore.common.Config.myEventList

class ShowReportActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    private var mContext: Context? = null
    var username: TextView? = null
    var carname: TextView? = null
    private var mDataManager: AppDataManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_showreport)
        mContext = this
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar!!.title = "Reports"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { //update return last activity
            setResult(RESULT_OK, Intent())
            finish()
        }
        username = findViewById(R.id.tv_table_name)
        carname = findViewById(R.id.tv_table_carname)
        username?.setText(myEventList.size.toString() + "")
        if (mDataManager == null) {
            mDataManager = AppDataManager(this)
            mDataManager!!.openDataBase()
        }
        val appDataList = mDataManager!!.allDataList
        val myAppList: MutableList<AppData> = ArrayList()
        for (eventData in myEventList) {
            for (appData in appDataList) {
                if (appData.event_id == eventData!!.id) {
                    myAppList.add(appData)
                }
            }
        }
        carname?.setText(myAppList.size.toString() + "")
    }
}