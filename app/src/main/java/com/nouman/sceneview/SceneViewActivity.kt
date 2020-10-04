package com.nouman.sceneview

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer
import com.google.ar.sceneform.ux.TransformationSystem
import com.nouman.sceneview.nodes.DragTransformableNode
import kotlinx.android.synthetic.main.activity_scene_view.*
import kotlinx.android.synthetic.main.bottom_sheet_layout_contact.view.*
import kotlinx.android.synthetic.main.bottom_sheet_layout_log_list.view.*
import java.util.concurrent.CompletionException


class SceneViewActivity : AppCompatActivity() {

    var isOpen = false
    var remoteModelUrl =
        "https://poly.googleusercontent.com/downloads/0BnDT3T1wTE/85QOHCZOvov/Mesh_Beagle.gltf"

    //var localModel = "model.sfb"
    var heartModel = "heartwithColor105.sfb"
    var heartModel2 = "heartwithColor50.sfb"
    var heartModel3 = "heartwithColor80.sfb"
    var heartModel4 = "heartwithColor90.sfb"
    var heartModel5 = "heartwithColor100.sfb"
    //var rocketModel = "rocket.sfb"
    private var heart = ""
    private var count = 0
    //private var stopCount = 0
    //private lateinit var timer: Timer

    //private var cupCakeNode = Node()
    private var dragTransformableNode = Node()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scene_view)


        //get data from putExtra intent
        var intent = intent
        val aName = intent.getStringExtra("iName")
        //set patient name in another activity
        a_name.text = aName
        //bottom sheet showing

        //val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        //bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetDialog2 = BottomSheetDialog(this)
        val bottomSheetDialog3 = BottomSheetDialog(this)
        val bottomSheetDialog4 = BottomSheetDialog(this)
        val bottomSheetDialog5 = BottomSheetDialog(this)
        val bottomSheetDialog6 = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout_camera,null)
        val view2 = layoutInflater.inflate(R.layout.bottom_sheet_layout_vs,null)
        val view3 = layoutInflater.inflate(R.layout.bottom_sheet_layout_chat,null)
        val view4 = layoutInflater.inflate(R.layout.bottom_lsheet_layout_logs,null)
        val view5 = layoutInflater.inflate(R.layout.bottom_sheet_layout_contact,null)
        val view6 = layoutInflater.inflate(R.layout.bottom_sheet_layout_log_list,null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog2.setContentView(view2)
        bottomSheetDialog3.setContentView(view3)
        bottomSheetDialog4.setContentView(view4)
        bottomSheetDialog5.setContentView(view5)
        bottomSheetDialog6.setContentView(view6)


        //feb button animation
        val fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open)
        val fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close)
        val fabRClockwise = AnimationUtils.loadAnimation(this,R.anim.rotate_clockwise)
        val fabRAniClockwise = AnimationUtils.loadAnimation(this,R.anim.rotate_anticlockwise)

        add_button.setOnClickListener {
            if(isOpen){
                vs_button.startAnimation(fabOpen)
                edit_button.startAnimation(fabOpen)
                camera_button.startAnimation(fabOpen)
                chat_button.startAnimation(fabOpen)
                add_button.startAnimation(fabRAniClockwise)

                isOpen = false
            }

            else {
                vs_button.startAnimation(fabClose)
                edit_button.startAnimation(fabClose)
                camera_button.startAnimation(fabClose)
                chat_button.startAnimation(fabClose)
                add_button.startAnimation(fabRClockwise)


                edit_button.isClickable
                camera_button.isClickable
                chat_button.isClickable
                vs_button.isClickable

                isOpen = true
            }

            edit_button.setOnClickListener {
                bottomSheetDialog6.show()

                //open the writing logs button
                view6.date_one.setOnClickListener {
                    bottomSheetDialog4.show()
                }
                view6.date_two.setOnClickListener {
                    bottomSheetDialog4.show()
                }
                view6.date_three.setOnClickListener {
                    bottomSheetDialog4.show()
                }
            }

            camera_button.setOnClickListener {
                bottomSheetDialog.show()
            }

            chat_button.setOnClickListener {
                bottomSheetDialog5.show()

                //open the chat bottom sheet
                view5.doctor_a.setOnClickListener {
                    bottomSheetDialog3.show()
                }
                view5.doctor_b.setOnClickListener {
                    bottomSheetDialog3.show()
                }
                view5.nurse_a.setOnClickListener {
                    bottomSheetDialog3.show()
                }
                view5.nurse_b.setOnClickListener {
                    bottomSheetDialog3.show()
                }
                view5.nurse_c.setOnClickListener {
                    bottomSheetDialog3.show()
                }
            }

            vs_button.setOnClickListener {
                bottomSheetDialog2.show()
            }

        }




        //load 3d heart model
        //val remoteModelUrl = intent.getStringExtra(EXTRA_MODEL_TYPE)
        var remoteModelUrl = aName
        if (remoteModelUrl.equals("remote")) {
            renderRemoteObject()
        } else {
            //load local model
          renderLocalObject()
        }

        var timesClicked = 0
        // get reference to button (kotlin dont need get reference to button)
        //val btn_click_me = findViewById(R.id.button) as Button
// set on-click listener (kotlin can directly use the id button here)
        /**
         * 运行连续现实动态效果
         */
        button2.setOnClickListener{renderNextObjectCycle(0)}
        button3.setOnClickListener {sceneView.scene.removeChild(dragTransformableNode)}
        button.setOnClickListener {
            timesClicked += 1
            textView.text = timesClicked.toString()
            Toast.makeText(this@SceneViewActivity, "You clicked me.", Toast.LENGTH_SHORT).show()

            if (sceneView != null) {

                if (timesClicked == 1){

                    renderNextObjectB()

                }else if (timesClicked == 2){

                    renderNextObjectC()
                }else if (timesClicked == 3){
                    renderNextObjectD()
                }else if (timesClicked == 4){
                    renderNextObjectE()
                }else if (timesClicked == 5){
                    sceneView.scene.removeChild(dragTransformableNode)
                    renderNextObject()
                    timesClicked = 7
                }

            }
        }
    }

   fun onBackPatientListClick(view: View) {
        startActivity(
            Intent(
                this,
                TestListActivity::class.java
            )
        )

    }


    /**
     * 定义并初始化定时器任务
     */
   private fun taskRun(){
        count++
        if(count<=5){
            textView.text = count.toString()
            Handler().postDelayed({taskRun()},3000)
        }
    }

    private fun renderNextObjectCycle(runningHeart: Int) {
           count++
           if (count == 1) {
               heart = "heartwithColor105.sfb"
           } else if (count == 2) {
               heart = "heartwithColor106.sfb"
           } else if (count == 3) {
               heart = "heartwithColor107.sfb"
           } else if (count == 4) {
               heart = "heartwithColor108.sfb"
           } else if (count == 5) {
               heart = "heartwithColor109.sfb"
           } else if (count == 6) {
               heart = "heartwithColor110.sfb"
           } else if (count == 7) {
               heart = "heartwithColor111.sfb"
           } else if (count == 8) {
               heart = "heartwithColor112.sfb"
               count = 10
           }
           ModelRenderable.builder()
               .setSource(this, Uri.parse(heart))
               .setRegistryId(heart)
               .build()
               .thenAccept { modelRenderable: ModelRenderable ->
                   skuProgressBar.setVisibility(View.GONE)
                   val node = addNodeToScene(modelRenderable)
               }

               .exceptionally { throwable: Throwable? ->
                   var message: String?
                   message = if (throwable is CompletionException) {
                       skuProgressBar.setVisibility(View.GONE)
                       "Internet is not working"
                   } else {
                       skuProgressBar.setVisibility(View.GONE)
                       "Can't load Model"
                   }
                   val mainHandler = Handler(Looper.getMainLooper())
                   val finalMessage: String = message
                   val myRunnable = Runnable {
                       AlertDialog.Builder(this)
                           .setTitle("Error")
                           .setMessage(finalMessage + "")
                           .setPositiveButton("Retry") { dialogInterface: DialogInterface, _: Int ->
                               renderLocalObject()
                               dialogInterface.dismiss()
                           }
                           .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                           .show()
                   }
                   mainHandler.post(myRunnable)
                   null
               }
           if (count <= 8) {
               textView.text = count.toString()
               Handler().postDelayed({ renderNextObjectCycle(0) }, 1000)
           }

    }




    private fun renderRemoteObject() {

        skuProgressBar.setVisibility(View.VISIBLE)
        ModelRenderable.builder()
            .setSource(
                this, RenderableSource.Builder().setSource(
                    this,
                    Uri.parse(remoteModelUrl),
                    RenderableSource.SourceType.GLTF2
                ).setScale(0.01f)
                    .setRecenterMode(RenderableSource.RecenterMode.CENTER)
                    .build()
            )
            .setRegistryId(remoteModelUrl)
            .build()
            .thenAccept { modelRenderable: ModelRenderable ->
                skuProgressBar.setVisibility(View.GONE)
                addNodeToScene(modelRenderable)
            }
            .exceptionally { throwable: Throwable? ->
                var message: String?
                message = if (throwable is CompletionException) {
                    skuProgressBar.setVisibility(View.GONE)
                    "Internet is not working"
                } else {
                    skuProgressBar.setVisibility(View.GONE)
                    "Can't load Model"
                }
                val mainHandler = Handler(Looper.getMainLooper())
                val finalMessage: String = message
                val myRunnable = Runnable {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(finalMessage + "")
                        .setPositiveButton("Retry") { dialogInterface: DialogInterface, _: Int ->
                            renderRemoteObject()
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                        .show()
                }
                mainHandler.post(myRunnable)
                null
            }
    }

    private fun renderLocalObject() {

        skuProgressBar.setVisibility(View.VISIBLE)
        ModelRenderable.builder()
            .setSource(this, Uri.parse(heartModel))
            .setRegistryId(heartModel)
            .build()
            .thenAccept { modelRenderable: ModelRenderable ->
                skuProgressBar.setVisibility(View.GONE)
                addNodeToScene(modelRenderable)
            }
            .exceptionally { throwable: Throwable? ->
                var message: String?
                message = if (throwable is CompletionException) {
                    skuProgressBar.setVisibility(View.GONE)
                    "Internet is not working"
                } else {
                    skuProgressBar.setVisibility(View.GONE)
                    "Can't load Model"
                }
                val mainHandler = Handler(Looper.getMainLooper())
                val finalMessage: String = message
                val myRunnable = Runnable {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(finalMessage + "")
                        .setPositiveButton("Retry") { dialogInterface: DialogInterface, _: Int ->
                            renderLocalObject()
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                        .show()
                }
                mainHandler.post(myRunnable)
                null
            }
    }

    private fun renderNextObjectB() {

        /**skuProgressBar.setVisibility(View.VISIBLE)*/
        ModelRenderable.builder()
            .setSource(this, Uri.parse(heartModel2))
            .setRegistryId(heartModel2)
            .build()
            .thenAccept { modelRenderable: ModelRenderable ->
                skuProgressBar.setVisibility(View.GONE)
                addNodeToScene(modelRenderable)
            }
            .exceptionally { throwable: Throwable? ->
                var message: String?
                message = if (throwable is CompletionException) {
                    skuProgressBar.setVisibility(View.GONE)
                    "Internet is not working"
                } else {
                    skuProgressBar.setVisibility(View.GONE)
                    "Can't load Model"
                }
                val mainHandler = Handler(Looper.getMainLooper())
                val finalMessage: String = message
                val myRunnable = Runnable {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(finalMessage + "")
                        .setPositiveButton("Retry") { dialogInterface: DialogInterface, _: Int ->
                            renderLocalObject()
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                        .show()
                }
                mainHandler.post(myRunnable)
                null
            }
    }

    private fun renderNextObjectC() {


        ModelRenderable.builder()
            .setSource(this, Uri.parse(heartModel3))
            .setRegistryId(heartModel3)
            .build()
            .thenAccept { modelRenderable: ModelRenderable ->
                skuProgressBar.setVisibility(View.GONE)
                addNodeToScene(modelRenderable)
            }
            .exceptionally { throwable: Throwable? ->
                var message: String?
                message = if (throwable is CompletionException) {
                    skuProgressBar.setVisibility(View.GONE)
                    "Internet is not working"
                } else {
                    skuProgressBar.setVisibility(View.GONE)
                    "Can't load Model"
                }
                val mainHandler = Handler(Looper.getMainLooper())
                val finalMessage: String = message
                val myRunnable = Runnable {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(finalMessage + "")
                        .setPositiveButton("Retry") { dialogInterface: DialogInterface, _: Int ->
                            renderLocalObject()
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                        .show()
                }
                mainHandler.post(myRunnable)
                null
            }
    }

    private fun renderNextObjectD() {


        ModelRenderable.builder()
            .setSource(this, Uri.parse(heartModel4))
            .setRegistryId(heartModel4)
            .build()
            .thenAccept { modelRenderable: ModelRenderable ->
                skuProgressBar.setVisibility(View.GONE)
                addNodeToScene(modelRenderable)
            }
            .exceptionally { throwable: Throwable? ->
                var message: String?
                message = if (throwable is CompletionException) {
                    skuProgressBar.setVisibility(View.GONE)
                    "Internet is not working"
                } else {
                    skuProgressBar.setVisibility(View.GONE)
                    "Can't load Model"
                }
                val mainHandler = Handler(Looper.getMainLooper())
                val finalMessage: String = message
                val myRunnable = Runnable {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(finalMessage + "")
                        .setPositiveButton("Retry") { dialogInterface: DialogInterface, _: Int ->
                            renderLocalObject()
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                        .show()
                }
                mainHandler.post(myRunnable)
                null
            }
    }

    private fun renderNextObjectE() {

        ModelRenderable.builder()
            .setSource(this, Uri.parse(heartModel5))
            .setRegistryId(heartModel5)
            .build()
            .thenAccept { modelRenderable: ModelRenderable ->
                skuProgressBar.setVisibility(View.GONE)
                addNodeToScene(modelRenderable)
            }
            .exceptionally { throwable: Throwable? ->
                var message: String?
                message = if (throwable is CompletionException) {
                    skuProgressBar.setVisibility(View.GONE)
                    "Internet is not working"
                } else {
                    skuProgressBar.setVisibility(View.GONE)
                    "Can't load Model"
                }
                val mainHandler = Handler(Looper.getMainLooper())
                val finalMessage: String = message
                val myRunnable = Runnable {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(finalMessage + "")
                        .setPositiveButton("Retry") { dialogInterface: DialogInterface, _: Int ->
                            renderLocalObject()
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                        .show()
                }
                mainHandler.post(myRunnable)
                null
            }
    }


    private fun renderNextObject() {

        ModelRenderable.builder()
            .setSource(this, Uri.parse(heartModel))
            .setRegistryId(heartModel)
            .build()
            .thenAccept { modelRenderable: ModelRenderable ->
                skuProgressBar.setVisibility(View.GONE)
               val node = addNodeToScene(modelRenderable)
            }

            .exceptionally { throwable: Throwable? ->
                var message: String?
                message = if (throwable is CompletionException) {
                    skuProgressBar.setVisibility(View.GONE)
                    "Internet is not working"
                } else {
                    skuProgressBar.setVisibility(View.GONE)
                    "Can't load Model"
                }
                val mainHandler = Handler(Looper.getMainLooper())
                val finalMessage: String = message
                val myRunnable = Runnable {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(finalMessage + "")
                        .setPositiveButton("Retry") { dialogInterface: DialogInterface, _: Int ->
                            renderLocalObject()
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                        .show()
                }
                mainHandler.post(myRunnable)
                null
            }
    }

    override fun onPause() {
        super.onPause()
        sceneView.pause()
    }

    private fun addNodeToScene(model: ModelRenderable){
        if (sceneView != null) {
            val transformationSystem = makeTransformationSystem()
            val node = Node()
            dragTransformableNode = DragTransformableNode(1f, transformationSystem)
            dragTransformableNode?.renderable = model
            sceneView.scene.addChild(dragTransformableNode)
            (dragTransformableNode as DragTransformableNode)?.select()
            sceneView.scene
                .addOnPeekTouchListener { hitTestResult: HitTestResult?, motionEvent: MotionEvent? ->
                    transformationSystem.onTouch(
                        hitTestResult,
                        motionEvent
                    )
                }

        }

    }



    private fun makeTransformationSystem(): TransformationSystem {
        val footprintSelectionVisualizer = FootprintSelectionVisualizer()
        return TransformationSystem(resources.displayMetrics, footprintSelectionVisualizer)
    }





    override fun onResume() {
        super.onResume()
        try {
            sceneView.resume()
        } catch (e: CameraNotAvailableException) {
            e.printStackTrace()
        }
    }

    object Statics {
        var EXTRA_MODEL_TYPE = "modelType"
    }

    override fun onDestroy() {
        super.onDestroy()
        try {

            sceneView.destroy()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


