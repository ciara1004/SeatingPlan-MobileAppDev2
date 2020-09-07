package ie.wit.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.ValueEventListener

import ie.wit.R
import ie.wit.main.SeatingPlanApp
import ie.wit.models.PlanModel
import ie.wit.utils.*
import kotlinx.android.synthetic.main.fragment_tablelayout.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

import java.util.HashMap


class TableLayoutFragment : Fragment(), AnkoLogger {

    lateinit var app: SeatingPlanApp
    lateinit var loader: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as SeatingPlanApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_tablelayout, container, false)
        loader = createLoader(activity!!)
        activity?.title = getString(R.string.action_tablelayout)

        setButtonListener(root)
        return root;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TableLayoutFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    fun setButtonListener(layout: View) {
        layout.saveButton.setOnClickListener {
            val tableNumber = Integer.parseInt(layout.tableNo.text.toString())
            val guest01 = layout.guest01.text.toString()
            val guest02 = layout.guest02.text.toString()
            val guest03 = layout.guest03.text.toString()
            val guest04 = layout.guest04.text.toString()
            val guest05 = layout.guest05.text.toString()
            val guest06 = layout.guest06.text.toString()
            val guest07 = layout.guest07.text.toString()
            val guest08 = layout.guest08.text.toString()
            val guest09 = layout.guest09.text.toString()
            val guest10 = layout.guest10.text.toString()
            val guest11 = layout.guest11.text.toString()
            val guest12 = layout.guest12.text.toString()

            writeNewTable(
                PlanModel(
                    tableNo = tableNumber,
                    guest01 = guest01,
                    guest02 = guest02,
                    guest03 = guest03,
                    guest04 = guest04,
                    guest05 = guest05,
                    guest06 = guest06,
                    guest07 = guest07,
                    guest08 = guest08,
                    guest09 = guest09,
                    guest10 = guest10,
                    guest11 = guest11,
                    guest12 = guest12,
                    profilepic = app.userImage.toString(),
                    email = app.auth.currentUser?.email))

        }
    }

    fun writeNewTable(table: PlanModel) {

        showLoader(loader, "Adding Seating Plan to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.auth.currentUser!!.uid
        val key = app.database.child("tables").push().key
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        table.uid = key
        val tableValues = table.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/seatingplan/$key"] = tableValues
        childUpdates["/user-tables/$uid/$key"] = tableValues

        app.database.updateChildren(childUpdates)
        hideLoader(loader)
    }

}
