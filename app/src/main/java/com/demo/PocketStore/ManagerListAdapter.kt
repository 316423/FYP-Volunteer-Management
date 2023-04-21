package com.demo.PocketStore

import android.content.Context
import android.widget.BaseAdapter
import android.widget.ListView
import com.demo.PocketStore.UserDataManager
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import com.demo.PocketStore.R
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.Toast

class ManagerListAdapter(private val list: List<UserData>, private val mContext: Context) :
    BaseAdapter() {
    private var listview: ListView? = null
    private var mUserDataManager: UserDataManager? = null
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        if (listview == null) {
            listview = parent as ListView
        }
        var holder: ViewHolder? = null
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context).inflate(
                R.layout.manager_list_item, null
            )
            holder = ViewHolder()
            holder!!.iv = convertView.findViewById<View>(R.id.item_iv_cover) as ImageView
            holder.item_tv_id = convertView.findViewById<View>(R.id.item_tv_id) as TextView
            holder.item_tv_title = convertView.findViewById<View>(R.id.item_tv_title) as TextView
            holder.item_tv_desc = convertView.findViewById<View>(R.id.item_tv_desc) as TextView
            holder.btnOK = convertView.findViewById<View>(R.id.btnOK) as Button
            holder.btnNO = convertView.findViewById<View>(R.id.btnNO) as Button
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val meal = list[position]
        holder!!.item_tv_id!!.text = meal.userId.toString() + ""
        holder.item_tv_title!!.text = meal.userName + ""
        holder.item_tv_desc!!.text = meal.userStatus + ""
        holder.btnOK!!.setOnClickListener {
            meal.userStatus = "1"
            modifyDataBase(meal)
        }
        holder.btnNO!!.setOnClickListener {
            meal.userStatus = "-1"
            modifyDataBase(meal)
        }
        return convertView
    }

    internal inner class ViewHolder {
        var iv: ImageView? = null
        var item_tv_id: TextView? = null
        var item_tv_title: TextView? = null
        var item_tv_type: TextView? = null
        var item_tv_desc: TextView? = null
        var item_tv_price: TextView? = null
        var item_tv_date: TextView? = null
        var btnOK: Button? = null
        var btnNO: Button? = null
    }

    fun modifyDataBase(mUser: UserData) {
        if (mUserDataManager == null) {
            mUserDataManager = UserDataManager(mContext)
            mUserDataManager!!.openDataBase()
        }
        mUserDataManager!!.openDataBase()
        val flag = mUserDataManager!!.updateUserDataById(mUser, mUser.userId)
        if (flag) {
            Toast.makeText(mContext, "modify successfully", Toast.LENGTH_SHORT).show()
        } else {
        }
    }
}