package com.meituan.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.meituan.robust.patch.RobustModify
import com.meituan.robust.patch.annotaion.Modify

class ThirdActivity : AppCompatActivity() {

    var test = 9
//    @Modify
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        val tv: TextView = findViewById(R.id.tv) as TextView
        val btn: Button = findViewById(R.id.btn) as Button
        Log.d("chlog","12345")
        tv.setOnClickListener {
//            RobustModify.modify()
            tv.text="thrid tv111"
        }
        SecondActivity.test2 = 3
        val df = SecondActivity.test2
        val df1 = test
        tv.apply {
            text="8907"
            textSize=50f
        }
    }
//
//    @Add
//    fun df2():String{
//        return "lalalaa"
//    }
}