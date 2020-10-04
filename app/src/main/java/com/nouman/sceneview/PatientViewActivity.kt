package com.nouman.sceneview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_patient_view.*
import java.util.*
import kotlin.collections.ArrayList


class PatientViewActivity : AppCompatActivity() {

    val arrayList = ArrayList<Model>()
    val displayList = ArrayList<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_view)

        setSupportActionBar(findViewById(R.id.my_toolbar))

        arrayList.add(Model("Patient1","this is patient description", R.drawable.ic_user))
        arrayList.add(Model("Patient2","this is patient description", R.drawable.ic_user))
        arrayList.add(Model("Patient3","this is patient description", R.drawable.ic_user))
        arrayList.add(Model("Patient4","this is patient description", R.drawable.ic_user))
        arrayList.add(Model("Patient5","this is patient description", R.drawable.ic_user))
        arrayList.add(Model("Patient6","this is patient description", R.drawable.ic_user))
        arrayList.add(Model("Patient7","this is patient description", R.drawable.ic_user))
        displayList.addAll(arrayList) //for search function

        val myAdapter = MyAdapter(displayList,this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myAdapter

    }



    /** create search function for patient list*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.searchmenu, menu)
    val menuItem = menu!!.findItem(R.id.search)

    if (menuItem != null) {
        val searchView = menuItem.actionView as SearchView

        val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        editText.hint = "Search..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText!!.isNotEmpty()){
                    displayList.clear()
                    val search = newText.toLowerCase(Locale.getDefault())
                    arrayList.forEach {

                        if (it.title.toLowerCase(Locale.getDefault()).contains(search)){
                                displayList.add(it)
                            }
                    }

                    recyclerView.adapter!!.notifyDataSetChanged()

                }

                else{
                    displayList.clear()
                    displayList.addAll(arrayList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return true
            }

        })
    }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
    }
