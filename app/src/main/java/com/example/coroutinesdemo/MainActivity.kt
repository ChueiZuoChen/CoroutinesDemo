package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {
    private var count = 0
    lateinit var job1: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job1 = CoroutineScope(Dispatchers.Main).launch {
            downloadData()
        }
        button1.setOnClickListener {
            job1.cancel()
        }
        button2.setOnClickListener {

            when {
                job1.isActive -> {
                    textView.text = "Active"
                }
                job1.isCancelled -> {
                    textView.text = "Canceld"
                }
                job1.isCompleted -> {
                    textView.text = "Completed"
                }
            }
        }

//        Async1()
//        CoroutineScope1()
    }

    private suspend fun downloadData(){
        withContext(IO){
            repeat(30){
                delay(1000)
                Log.i("MyTage","repeating $it")
            }
        }
    }

    private fun Async1() {
        CoroutineScope(Main).launch {
            Log.i("MyTag", "Calculation started...")

            val stock1 = async(IO) {
                getStock1()
            }
            val stock2 = async(IO) {
                getStock2()
            }
            val total = stock1.await() + stock2.await()
            Toast.makeText(applicationContext, "Total is $total", Toast.LENGTH_LONG).show()
            Log.i("MyTag", "Total is $total")

        }
    }

    private fun CoroutineScope1() {
        button1.setOnClickListener {
            textView.text = count++.toString()
        }
        button2.setOnClickListener {

            CoroutineScope(IO).launch {
                downloadUserData()
            }

        }
    }

    private suspend fun downloadUserData() {

        for (i in 1..200000) {
            withContext(Dispatchers.Main) {
                textView.text = "Downloading user $i in ${Thread.currentThread().name}"
            }
            delay(3000)
        }
    }
}

private suspend fun getStock1(): Int {
    delay(10000)
    Log.i("MyTag", "stock 1 returned ")
    return 10
}

private suspend fun getStock2(): Int {
    delay(8000)
    Log.i("MyTag", "stock 2 returned ")
    return 10
}


