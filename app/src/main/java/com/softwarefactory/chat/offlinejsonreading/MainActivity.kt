package com.softwarefactory.chat.offlinejsonreading

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import org.json.JSONArray
import java.io.InputStream
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.*



class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var recyclerView:RecyclerView
    private lateinit var busList: MutableList<Bus>
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerViewAdapter:RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        busList = ArrayList()
        layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = RecyclerViewAdapter(busList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerViewAdapter
        readJson()
    }
    fun readJson(){
        val json: String?
        try {
            val inputStream:InputStream = assets.open("bus_stops.json")
            json = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(json)
            for (i in 0 until json.length){
                val jsonObject = jsonArray.getJSONObject(i)
                val bus = Bus()
                bus.name = jsonObject.getString("name")
                bus.lat = jsonObject.getString("lat")
                bus.lng = jsonObject.getString("lng")
                busList.add(bus)
            }

        }catch (e:Exception){

        }
    }

    override fun onQueryTextSubmit(s: String): Boolean {
        return false
    }

    override fun onQueryTextChange(s: String): Boolean {
        var s = s
        s = s.toLowerCase()
        val newList = ArrayList<Bus>()
        for (stories in busList) {
            val name = stories.name
            if (name!!.contains(s)) {
                newList.add(stories)

            }
        }
        recyclerViewAdapter.setFilter(newList)
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu.findItem(R.id.app_bar_search)
        val searchView = MenuItemCompat.getActionView(search) as SearchView
        searchView.setOnQueryTextListener(this@MainActivity)
        return true
    }

}

class RecyclerViewAdapter(private var list:List<Bus>): RecyclerView.Adapter<RecyclerViewAdapter.MyHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list,parent,false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val bus = list[position]
        holder.tvName.text = bus.name
        holder.tvLat.text = bus.lat
        holder.tvLng.text = bus.lng
        holder.tvBus.text = bus.bus
    }

    class MyHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_name) as TextView
        var tvLat:TextView = itemView.findViewById(R.id.tv_lat) as TextView
        var tvLng:TextView = itemView.findViewById(R.id.tv_lng) as TextView
        var tvBus:TextView = itemView.findViewById(R.id.tv_bus) as TextView
    }

    fun setFilter(newList: ArrayList<Bus>) {
        list = ArrayList()
        (list as ArrayList<Bus>).addAll(newList)
        notifyDataSetChanged()
    }

}

data class Bus(var id:String? = null,var name:String?=null,var lat:String?=null,var lng:String?=null,var bus:String?=null){
//    @Throws(JSONException::class)
//    constructor(json: JSONObject) : this() {
//        id = json.getString("id")
//        title = json.getString("name")
//        body = json.getString("lat")
//
//    }
}