package com.softwarefactory.chat.offlinejsonreading

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView:RecyclerView
    private lateinit var postList: MutableList<Bus>
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerViewAdapter:RecyclerViewAdapter
    var arrList  = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        postList = ArrayList()
        layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = RecyclerViewAdapter(postList)
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
                bus.title = jsonObject.getString("name")
                bus.body = jsonObject.getString("lat")
                postList.add(bus)
            }

        }catch (e:Exception){

        }
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
        val posts = list[position]
        holder.tvTitle.text = posts.title
        holder.tvBody.text = posts.id
    }

    class MyHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var tvBody:TextView
        init {
            tvTitle = itemView.findViewById(R.id.tv_title) as TextView
            tvBody = itemView.findViewById(R.id.tv_body) as TextView

        }
    }

}

data class Bus(var id:String? = null,var title:String?=null,var body:String?=null){
    @Throws(JSONException::class)
    constructor(json: JSONObject) : this() {
        id = json.getString("id")
        title = json.getString("name")
        body = json.getString("lat")

    }
}