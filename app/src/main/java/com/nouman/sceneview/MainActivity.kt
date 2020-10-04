package com.nouman.sceneview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        setSupportActionBar(toolbar2)
        var  toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar2,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }


    fun onLocalModelClick(view: View) {
        startActivity(
            Intent(
                this,
                SceneViewActivity::class.java
            ).putExtra(SceneViewActivity.Statics.EXTRA_MODEL_TYPE, "local")
        )

    }

    fun onPatientListClick(view: View) {
        startActivity(
            Intent(
                this,
                PatientViewActivity::class.java
            )
        )
    }

    fun onExpandableListClick(view: View) {
        startActivity(
            Intent(
                this,
                TestListActivity::class.java
            )
        )
    }
    fun onLoginPageClick(view: View) {
        startActivity(
            Intent(
                this,
                LoginActivity::class.java
            )
        )
    }
    fun onRemoteModelClick(view: View) {
        startActivity(
            Intent(
                this,
                SceneViewActivity::class.java
            ).putExtra(SceneViewActivity.Statics.EXTRA_MODEL_TYPE, "remote")
        )
    }
}
