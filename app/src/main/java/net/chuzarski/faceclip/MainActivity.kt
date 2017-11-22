package net.chuzarski.faceclip

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainActivity : Activity(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
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

        buttonList!!.onItemClickListener = this
        buttonList!!.onItemLongClickListener = this

    }

    private fun getCurrFace(parent: AdapterView<*>?, position: Int): String {
        if (parent != null) {
            return parent.getItemAtPosition(position) as String
        } else {
            return ""
        }
    }

    private fun handleHelpMenu(): Unit {;
        var alertbuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        alertbuilder
                .setTitle(resources.getString(R.string.help_dialog_title))
                .setMessage(resources.getString(R.string.help_dialog_content))
                .setNeutralButton(R.string.gotcha, { dialog: DialogInterface, i: Int ->
                    dialog.dismiss()
                })
        alertbuilder.create().show()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var currFace: String = ""
        var clip: ClipData

        currFace = getCurrFace(parent, position)

        // need a clip
        clip = ClipData.newPlainText(currFace, currFace)
        clipboard!!.primaryClip = clip

        Toast.makeText(this, "\" $currFace \" copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        var currFace: String = getCurrFace(parent, position)
        var textIntent: Intent = Intent()

        if(currFace.isEmpty()) {
            return false
        }

        textIntent.setAction(Intent.ACTION_SEND)
        textIntent.putExtra(Intent.EXTRA_TEXT, currFace)
        textIntent.setType("text/plain")
        startActivity(Intent.createChooser(textIntent, currFace))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null) {
            when(item.itemId) {
                R.id.help_menu_action -> { handleHelpMenu(); return true }
                else -> return false
            }
        } else {
            return false
        }
    }

}