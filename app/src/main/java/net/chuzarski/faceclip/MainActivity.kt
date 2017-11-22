package net.chuzarski.faceclip

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainActivity : Activity(), AdapterView.OnItemClickListener {

    var buttonList: ListView? = null
    var list: Array<String>? = null
    var clipboard: ClipboardManager? = null
    var log: Logger = LoggerFactory.getLogger("MainActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonList = findViewById(R.id.main_list) as ListView
        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        initList()

        log.debug("activity created!")

    }

    private fun initList(): Unit {
        log.debug("Setting up main list")

        // need a list!
        buttonList!!.adapter = object : ArrayAdapter<String>(this, R.layout.lv_text_standard,
                resources.getStringArray(R.array.standard_faces)) {

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                var view = convertView

                if(view == null) {
                    view = layoutInflater.inflate(R.layout.lv_text_standard, parent, false)
                }

                (view!!.findViewById(R.id.face_text) as TextView).setText(getItem(position))

                return view
            }
        }

        buttonList!!.setOnItemClickListener(this)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var currFace = ""
        var clip: ClipData

        currFace = parent!!.getItemAtPosition(position) as String

        // need a clip
        clip = ClipData.newPlainText(currFace, currFace)
        clipboard!!.primaryClip = clip

        Toast.makeText(this, "\" $currFace \" copied to clipboard!", Toast.LENGTH_SHORT).show()

    }

}