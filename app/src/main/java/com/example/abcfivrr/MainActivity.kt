package com.example.abcfivrr

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abcfivrr.utilities.UserTracker

class MainActivity : AppCompatActivity() {
    var abc: RecyclerView? = null
    var myAdapter: MyAdapter? = null
    var linearLayoutManager = LinearLayoutManager(this)
    var userTracker: UserTracker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        abc = findViewById(R.id.recycler)
        abc?.setLayoutManager(linearLayoutManager)
        myAdapter = MyAdapter()
        abc?.setLayoutManager(linearLayoutManager)
        abc?.setAdapter(myAdapter)
        userTracker = UserTracker(abc)
        userTracker!!.startTracking()
    }

    override fun onPause() {
        super.onPause()
        userTracker!!.stopTracking()
        var max = 0
        var output = ""
        for (i in 0 until userTracker!!.returnDetail().size)
        {
            if(max.compareTo(userTracker!!.returnDetail()[i].viewDuration)==0)
            {
               output = userTracker!!.returnDetail()[i].viewId.toString()
            }
        }
        output = (output.toInt() +1).toString()
        Toast.makeText(applicationContext, "Longest time on screen $output box",Toast.LENGTH_SHORT).show()

    }
}