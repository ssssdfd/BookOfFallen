package com.lmmobi.lereade.screens

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.lmmobi.lereade.R
import kotlin.random.Random

class IgraOctivity : AppCompatActivity() {
    private lateinit var picha1: View
    private lateinit var picha2: View
    private lateinit var picha3: View
    private lateinit var picha4: View
    private lateinit var picha5: View
    private lateinit var picha6: View

    private lateinit var fightButton: Button

    private lateinit var llTop: LinearLayout
    private lateinit var llBottom: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.octivity_igra)

        Toast.makeText(this, "Long press on a view to move it", Toast.LENGTH_LONG).show()
        
        llTop = findViewById(R.id.llTop)
        llBottom = findViewById(R.id.llBottom)

        fightButton = findViewById(R.id.fightBtn)

        picha1 = findViewById(R.id.dragView1)
        picha2 = findViewById(R.id.dragView2)
        picha3 = findViewById(R.id.dragView3)
        picha4 = findViewById(R.id.dragView4)
        picha5 = findViewById(R.id.dragView5)
        picha6 = findViewById(R.id.dragView6)

        llTop.setOnDragListener(dragListener)
        llBottom.setOnDragListener(dragListener)

        prepare(picha1)
        prepare(picha2)
        prepare(picha3)
        prepare(picha4)
        prepare(picha5)
        prepare(picha6)

        fightButton.setOnClickListener(){
            fightButton.isEnabled = false
            val r = Random
            when(r.nextInt(3)){
                0->Toast.makeText(this, "There is no winner", Toast.LENGTH_SHORT).show()
                1-> Toast.makeText(this, "The top team won", Toast.LENGTH_SHORT).show()
                2-> Toast.makeText(this, "The bottom team won", Toast.LENGTH_SHORT).show()
            }
            fightButton.isEnabled = true

        }

        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
            }
        })
    }

    private fun prepare(view: View){
        view.setOnLongClickListener{
            val clipText = "Change side"
            val item = ClipData.Item(clipText)
            val mimeType = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeType, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it,0)

            it.visibility = View.VISIBLE
            true
        }
    }

   private val dragListener = View.OnDragListener { view, event ->
       when(event.action){
           DragEvent.ACTION_DRAG_STARTED->{
               event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
           }
           DragEvent.ACTION_DRAG_ENTERED->{
               view.invalidate()
                true
           }
           DragEvent.ACTION_DRAG_LOCATION->true
           DragEvent.ACTION_DRAG_EXITED->{
               view.invalidate()
               true
           }
           DragEvent.ACTION_DROP->{
               val item = event.clipData.getItemAt(0)
               val dragData = item.text
               Toast.makeText(this, dragData, Toast.LENGTH_SHORT).show()

               view.invalidate()

               val v = event.localState as View
               val owner = v.parent as ViewGroup
               owner.removeView(v)
               val destination = view as LinearLayout
               destination.addView(v)
               v.visibility = View.VISIBLE
               true
           }
           DragEvent.ACTION_DRAG_ENDED->{
               view.invalidate()
               true
           }
           else->false
       }
    }
}