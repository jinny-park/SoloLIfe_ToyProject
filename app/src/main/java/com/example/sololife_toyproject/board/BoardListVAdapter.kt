package com.example.sololife_toyproject.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.sololife_toyproject.R
import org.w3c.dom.Text

class BoardListVAdapter(val boardList : MutableList<BoardModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView

        if (view == null) {

            view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item, parent, false)

        }

        val title = view?.findViewById<TextView>(R.id.titleArea)
        title!!.text = boardList[position].title

        val content = view?.findViewById<TextView>(R.id.contentArea)
        content!!.text = boardList[position].content

        val time = view?.findViewById<TextView>(R.id.timeArea)
        time!!.text = boardList[position].time

        return view!!
    }
}