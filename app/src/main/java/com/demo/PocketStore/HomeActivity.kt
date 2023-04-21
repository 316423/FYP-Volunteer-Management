package com.demo.PocketStore

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.PagerAdapter
import android.widget.LinearLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ListView
import com.demo.PocketStore.UserDataManager
import android.widget.SimpleAdapter
import android.os.Bundle
import android.view.Window
import com.demo.PocketStore.R
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import android.view.LayoutInflater
import android.view.ViewGroup
import com.demo.PocketStore.ManagerListAdapter
import android.content.Intent
import com.demo.PocketStore.HomeActivity
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : Activity(), View.OnClickListener {
    private val REQUEST_CODE_1 = 1
    private val REQUEST_CODE_2 = 2
    private var mViewPager: ViewPager? = null
    private var mAdapter: PagerAdapter? = null
    private val mViews: MutableList<View> = ArrayList()
    private var mTabWeixin: LinearLayout? = null
    private var mTabFrd: LinearLayout? = null
    private var mTabSetting: LinearLayout? = null
    private var mWeixinImg: ImageButton? = null
    private var mFrdImg: ImageButton? = null
    private var mSettingImg: ImageButton? = null
    private var tvWeixin: TextView? = null
    private var tvFrd: TextView? = null
    private var tvSetting: TextView? = null
    private var tvTopTitle: TextView? = null
    private var tvInput: TextView? = null
    private val llNewFriend: LinearLayout? = null
    private val listView: ListView? = null
    private var listManager: ListView? = null
    private var mUserDataManager: UserDataManager? = null
    private var userDataList: MutableList<UserData> = ArrayList()
    private val mHashMap: HashMap<String, Any>? = null
    private val mSimpleAdapter: SimpleAdapter? = null
    var mContext: Context? = null
    private var username: String? = null
    private var loanItems = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_home)
        mContext = this
        username = intent.getStringExtra("username")
        initView()
        initData()
        initEvents()
    }

    private fun initEvents() {
        mTabWeixin!!.setOnClickListener(this)
        mTabFrd!!.setOnClickListener(this)
        mTabSetting!!.setOnClickListener(this)
        //viewpager滑动事件
        mViewPager!!.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(arg0: Int) { //when the viewpager slides，the corresponding image of the bottom navigation button needs to be changed
                val currentItem = mViewPager!!.currentItem
                resetImg()
                when (currentItem) {
                    0 -> {
                        mWeixinImg!!.setImageResource(R.drawable.tab_01_pressed)
                        tvWeixin!!.setTextColor(resources.getColor(R.color.colorAccentBlue))
                        tvTopTitle!!.text = "New Loan"
                    }
                    1 -> {
                        mFrdImg!!.setImageResource(R.drawable.tab_02_pressed)
                        tvFrd!!.setTextColor(resources.getColor(R.color.colorAccentBlue))
                        tvTopTitle!!.text = "My Loans"
                    }
                    2 -> {
                        mSettingImg!!.setImageResource(R.drawable.tab_03_pressed)
                        tvSetting!!.setTextColor(resources.getColor(R.color.colorAccentBlue))
                        tvTopTitle!!.text = "Manage"
                    }
                    else -> {}
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
                // TODO Auto-generated method stub
            }

            override fun onPageScrollStateChanged(arg0: Int) {
                // TODO Auto-generated method stub
            }
        })
    }

    private fun initView() { //Initialise all view
        mViewPager = findViewById<View>(R.id.id_viewpager) as ViewPager
        //tabs
        mTabWeixin = findViewById<View>(R.id.id_tab_weixin) as LinearLayout
        mTabFrd = findViewById<View>(R.id.id_tab_frd) as LinearLayout
        mTabSetting = findViewById<View>(R.id.id_tab_setting) as LinearLayout
        //imagebutton
        mWeixinImg = findViewById<View>(R.id.id_tab_weixin_img) as ImageButton
        mFrdImg = findViewById<View>(R.id.id_tab_frd_img) as ImageButton
        mSettingImg = findViewById<View>(R.id.id_tab_setting_img) as ImageButton
        //Bottom TextView
        tvWeixin = findViewById<View>(R.id.id_tab_weixin_tv) as TextView
        tvFrd = findViewById<View>(R.id.id_tab_frd_tv) as TextView
        tvSetting = findViewById<View>(R.id.id_tab_setting_tv) as TextView
        tvTopTitle = findViewById<View>(R.id.tv_top_title) as TextView

        //Introducing layouts by LayoutInflater and converting them into views
        val mInflater = LayoutInflater.from(this) //create an object of LayoutInflater
        val tab01 = mInflater.inflate(R.layout.tab04, null) //Dynamically loading a layout file by inflate method
        val tab02 = mInflater.inflate(R.layout.tab_friend, null)
        val tab04 = mInflater.inflate(R.layout.tab_friend, null)
        tvInput = tab04.findViewById(R.id.input)
        listManager = tab01.findViewById<View>(R.id.listManager) as ListView
        mViews.add(tab01)
        mViews.add(tab02)
        mViews.add(tab04)
        //Initialise PagerAdapter
        mAdapter = object : PagerAdapter() {
            override fun destroyItem(
                container: ViewGroup, position: Int,
                `object`: Any
            ) {
                // TODO Auto-generated method stub
                container.removeView(mViews[position])
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = mViews[position] // Get the needed view form the list of Views by position
                container.addView(view) //add the above view into ViewGroup
                return view
            }

            override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
                // TODO Auto-generated method stub
                return arg0 === arg1
            }

            override fun getCount(): Int {
                // return the size of views from PagerView
                return mViews.size
            }
        }
        //configure adapter for ViewPager
        mViewPager!!.adapter = mAdapter
    }

    //Initialisation
    private fun initData() {
        if (mUserDataManager == null) {
            mUserDataManager = UserDataManager(this)
            mUserDataManager!!.openDataBase() //open local database
        }
        userDataList.clear()
        mUserDataManager!!.openDataBase()
        userDataList = mUserDataManager!!.allUserDataList.toMutableList()
        val adapter2 = mContext?.let { ManagerListAdapter(userDataList, it) }
        listManager!!.adapter = adapter2
        adapter2?.notifyDataSetChanged()
    }

    override fun onClick(v: View) {
        resetImg() //when click tab, the corresponding color should be brighter,Therefore, before
        // clicking on a specific tab, reset all images to an unchecked state, a dark images

        when (v.id) {
            R.id.id_tab_weixin -> {
                mViewPager!!.currentItem = 0
                mWeixinImg!!.setImageResource(R.drawable.tab_01_pressed) //highlight the color of the button
                tvWeixin!!.setTextColor(resources.getColor(R.color.colorAccentBlue)) //highlight the color of the button
                tvTopTitle!!.text = "Organisation Approval"
            }
            R.id.id_tab_frd -> {
                mViewPager!!.currentItem = 1
                mFrdImg!!.setImageResource(R.drawable.tab_02_pressed)
                tvFrd!!.setTextColor(resources.getColor(R.color.colorAccentBlue)) //highlight the color of the font
                tvTopTitle!!.text = "Sending Message"
            }
            R.id.id_tab_setting -> {
                mViewPager!!.currentItem = 3
                mSettingImg!!.setImageResource(R.drawable.tab_03_pressed)
                tvSetting!!.setTextColor(resources.getColor(R.color.colorAccentBlue)) //highlight the color of the font
                tvTopTitle!!.text = "Resolve Issue"
            }
            else -> {}
        }
    }

    /*
	 * set all images to dark
	 * */
    private fun resetImg() {
        mWeixinImg!!.setImageResource(R.drawable.tab_01_normal)
        mFrdImg!!.setImageResource(R.drawable.tab_02_normal)
        mSettingImg!!.setImageResource(R.drawable.tab_03_normal)
        tvWeixin!!.setTextColor(resources.getColor(R.color.black))
        tvFrd!!.setTextColor(resources.getColor(R.color.black))
        tvSetting!!.setTextColor(resources.getColor(R.color.black))
    }

    //call this function when a return value exists, determine who it belongs to by requestCode
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            REQUEST_CODE_1 -> if (resultCode == RESULT_OK) {
                //get return value
                val rtn1 = data.getStringExtra("result")
                Log.e("xxx", "return str:" + rtn1 + rtn1!!.length)
                if (rtn1.length == 0) {
                    Toast.makeText(mContext, "add items:0", Toast.LENGTH_SHORT).show()
                } else {
                    val arr = rtn1.split("\n").toTypedArray()
                    loanItems = arr.size
                    Toast.makeText(mContext, "add items:$loanItems", Toast.LENGTH_SHORT).show()
                    tvInput!!.text = rtn1
                }
            }
            REQUEST_CODE_2 -> if (resultCode == RESULT_OK) {
                initData()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //      * get the current date time
    private val currentDateTime: String
        private get() {
            val format = SimpleDateFormat("MMM d yyyy")
            return format.format(Date())
        }

    companion object {
        const val RESULT_OK = 0
    }
}