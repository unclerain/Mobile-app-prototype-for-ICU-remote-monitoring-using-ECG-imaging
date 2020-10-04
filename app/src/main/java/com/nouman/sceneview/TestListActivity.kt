package com.nouman.sceneview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_test_list.*

class TestListActivity : AppCompatActivity() {

    val versionList = ArrayList<Versions>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_list)


        initData()
        setRecyclerView()


    }

    fun onPatientMonitorClick(view: View) {
        startActivity(
            Intent(
                this,
                SceneViewActivity::class.java
            ).putExtra(SceneViewActivity.Statics.EXTRA_MODEL_TYPE, versionList[0].codename)
        )

    }


    private fun setRecyclerView() {
        val versionAdapter = VersionAdapter(versionList,this)
        recyclerView2.adapter = versionAdapter
        recyclerView2.setHasFixedSize(true)
    }


    private fun initData() {
        versionList.add(
            Versions(
            "Michael",
            "ID: 111111",
            "Age: 50",
            "Sex: M",
            "Notes",
            "1. Heart failure",
            "2. Diabetes"
        )
        )

        versionList.add(
            Versions(
                "John",
                "ID: 222222",
                "Age: 60",
                "Sex: M",
                "Notes",
                "1. Heart failure",
                "2. Diabetes"
            ))

        versionList.add(
            Versions(
                "Mary",
                "ID: 333333",
                "Age: 65",
                "Sex: F",
                "Notes",
                "1. Heart failure",
                "2. Diabetes"
            ))

        versionList.add(
            Versions(
                "Bill",
                "ID: 444444",
                "Age: 48",
                "Sex: M",
                "Notes",
                "1. Heart failure",
                "2. Diabetes"
            ))

        versionList.add(
            Versions(
                "Lisa",
                "ID: 555555",
                "Age: 76",
                "Sex: F",
                "Notes",
                "1. Heart failure",
                "2. Diabetes"
            ))

        versionList.add(
            Versions(
                "Erin",
                "ID: 666666",
                "Age: 57",
                "Sex: F",
                "Notes",
                "1. Heart failure",
                "2. Diabetes"
            ))

        versionList.add(
            Versions(
                "Lindsay",
                "ID: 777777",
                "Age: 67",
                "Sex: F",
                "Notes",
                "1. Heart failure",
                "2. Diabetes"
            ))
    }
}