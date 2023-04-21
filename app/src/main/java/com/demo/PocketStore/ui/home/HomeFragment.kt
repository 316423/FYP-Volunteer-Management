package com.demo.PocketStore.ui.home

import android.app.AlertDialog
import android.widget.AdapterView.OnItemLongClickListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demo.PocketStore.EventDataManager
import com.demo.PocketStore.EventData
import java.util.ArrayList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.demo.PocketStore.ui.home.HomeViewModel
import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import com.demo.PocketStore.AddEventActivity
import com.demo.PocketStore.R
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.demo.PocketStore.EventListAdapter
import android.content.DialogInterface
import android.widget.*
import androidx.fragment.app.Fragment
import com.demo.PocketStore.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), OnItemLongClickListener {
    private var binding: FragmentHomeBinding? = null
    private var listManager: ListView? = null
    private var searchView: SearchView? = null
    private var swipe: SwipeRefreshLayout? = null
    private var btnok: Button? = null
    private var mDataManager: EventDataManager? = null
    private var dataList: MutableList<EventData> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val homeViewModel = ViewModelProvider(this).get(
            HomeViewModel::class.java
        )
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        listManager = binding!!.list
        listManager!!.onItemLongClickListener = this
        searchView = binding!!.searchview
        btnok = binding!!.btnOk
        btnok!!.setOnClickListener { startActivity(Intent(activity, AddEventActivity::class.java)) }
        swipe = binding!!.swipe
        //改变加载显示的颜色
        swipe!!.setColorSchemeColors(
            resources.getColor(R.color.red),
            resources.getColor(R.color.red)
        )
        //设置向下拉多少出现刷新
        swipe!!.setDistanceToTriggerSync(200)
        //设置刷新出现的位置
        swipe!!.setProgressViewEndTarget(false, 200)
        swipe!!.setOnRefreshListener {
            swipe!!.isRefreshing = false
            initData()
        }
        initData()
        return root
    }

    //初始化
    private fun initData() {
        if (mDataManager == null) {
            mDataManager = EventDataManager(activity)
            mDataManager!!.openDataBase()
        }
        dataList.clear()
        dataList = mDataManager!!.allDataList.toMutableList()
        val adapter2 = activity?.let { EventListAdapter(dataList, it) }
        listManager!!.adapter = adapter2
        adapter2?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onItemLongClick(
        parent: AdapterView<*>,
        view: View,
        position: Int,
        id: Long
    ): Boolean {
        val eventData = parent.adapter.getItem(position) as EventData
        AlertDialog.Builder(activity).setIcon(R.mipmap.ic_launcher).setTitle("delete")
            .setMessage("delete this event?")
            .setPositiveButton("confirm") { dialogInterface, i -> //ToDo: 你想做的事情
                mDataManager!!.openDataBase()
                mDataManager!!.deleteUserData(eventData.id)
                initData()
                Toast.makeText(activity, "delete successfully", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("cancel", null).show()
        return true
    }
}