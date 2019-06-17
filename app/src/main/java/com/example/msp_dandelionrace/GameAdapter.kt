package com.example.msp_dandelionrace

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class GameAdapter(private val context: Context,
                    private val dataSource: ArrayList<GameInstance>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.game_item_layout, parent, false)

        val gameName = rowView.findViewById(R.id.tv_adapter_gameName) as TextView

        val hostMail = rowView.findViewById(R.id.tv_adapter_hostMail) as TextView

        val join = rowView.findViewById(R.id.btn_adapter_joinGame) as Button
        join.setOnClickListener({
            val intent = Intent(context, LobbyActivity::class.java)
            var item = getItem(position) as GameInstance
            intent.putExtra("hostMail", item.hostMail)
            intent.putExtra("name", item.name)
            context.startActivity(intent)
        })


        val gameInstance = getItem(position) as GameInstance

        gameName.text = gameInstance.name
        hostMail.text = gameInstance.hostMail

        return rowView
    }

}