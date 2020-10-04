package com.nouman.sceneview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VersionAdapter(val versionList: List<Versions>,val context: Context) :
        RecyclerView.Adapter<VersionAdapter.VersionVH>(){
    class VersionVH (itemView: View): RecyclerView.ViewHolder(itemView) {

        var codeNameTxt : TextView = itemView.findViewById(R.id.code_name)
        var versionTxt : TextView = itemView.findViewById(R.id.version)
        var apiLevelTxt : TextView = itemView.findViewById(R.id.api_level)
        var descriptionTxt : TextView = itemView.findViewById(R.id.description)
        var notesTxt : TextView = itemView.findViewById(R.id.notes)
        var noteOneTxt : TextView = itemView.findViewById(R.id.note_one)
        var noteTwoTxt : TextView = itemView.findViewById(R.id.note_two)
        var monitorButton : Button = itemView.findViewById(R.id.startmonitor)
        var linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout2)
        var expandableLayout : RelativeLayout = itemView.findViewById(R.id.expandable_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VersionVH {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.rowlist,parent,false)

        return VersionVH(view)
    }

    override fun getItemCount(): Int {
        return versionList.size
    }

    override fun onBindViewHolder(holder: VersionVH, position: Int) {
        val versions: Versions = versionList[position]
        holder.codeNameTxt.text = versions.codename
        holder.versionTxt.text = versions.versions
        holder.apiLevelTxt.text = versions.apiLevel
        holder.descriptionTxt.text = versions.description
        holder.notesTxt.text = versions.notes
        holder.noteOneTxt.text = versions.noteOne
        holder.noteTwoTxt.text = versions.noteTwo

        val isExpandable: Boolean = versionList[position].expandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.linearLayout.setOnClickListener {
            val versions = versionList[position]

            versions.expandable = !versions.expandable
            notifyItemChanged(position)
            //get name of selected item with intent
            var gName : String = versions.codename
            //creat intent in kotlin
            val intent = Intent(context, TestListActivity::class.java)
            //put all items with putExtro intent
            intent.putExtra("iName",gName)
            //context.startActivity(intent)
        }
        holder.monitorButton.setOnClickListener {
            //get name of selected item with intent
            var gName : String = versions.codename
            //creat intent in kotlin
            val intent = Intent(context, SceneViewActivity::class.java)
            //put all items with putExtro intent
            intent.putExtra("iName",gName.subSequence(0,1))
            context.startActivity(intent)
        }
/**
        holder.linearLayout.startmonitor.setOnClickListener {
            //get name of selected item with intent
            var gName : String = versions.codename
            //creat intent in kotlin
            val intent = Intent(context, SceneViewActivity::class.java)
            //put all items with putExtro intent
            intent.putExtra("iName",gName)
            context.startActivity(intent)
        }
        */
    }
}