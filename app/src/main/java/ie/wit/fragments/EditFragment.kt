package ie.wit.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.wit.R
import ie.wit.main.SeatingPlanApp
import ie.wit.models.PlanModel
import ie.wit.utils.createLoader
import ie.wit.utils.hideLoader
import ie.wit.utils.showLoader
import kotlinx.android.synthetic.main.fragment_edit.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditFragment : Fragment(), AnkoLogger {

    lateinit var app: SeatingPlanApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var editTable: PlanModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as SeatingPlanApp

        arguments?.let {
            editTable = it.getParcelable("edittable")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_edit, container, false)
        activity?.title = getString(R.string.action_edit)
        loader = createLoader(activity!!)

        root.tableNo.setText(editTable!!.tableNo.toString())
        root.guest01.setText(editTable!!.guest01)
        root.guest02.setText(editTable!!.guest02)
        root.guest03.setText(editTable!!.guest03)
        root.guest04.setText(editTable!!.guest04)
        root.guest05.setText(editTable!!.guest05)
        root.guest06.setText(editTable!!.guest06)
        root.guest07.setText(editTable!!.guest07)
        root.guest08.setText(editTable!!.guest08)
        root.guest09.setText(editTable!!.guest09)
        root.guest10.setText(editTable!!.guest10)
        root.guest11.setText(editTable!!.guest11)
        root.guest12.setText(editTable!!.guest12)

        root.saveEditButton.setOnClickListener {
            showLoader(loader, "Updating Table Plan on Server...")
            updateTableData()
            updateTable(editTable!!.uid, editTable!!)
            updateUserTable(app.auth.currentUser!!.uid,
                               editTable!!.uid, editTable!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(table: PlanModel) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("edittable",table)
                }
            }
    }

    fun updateTableData() {
        editTable!!.tableNo = root.tableNo.text.toString().toInt()
        editTable!!.guest01 = root.guest01.text.toString()
        editTable!!.guest02 = root.guest02.text.toString()
        editTable!!.guest03 = root.guest03.text.toString()
        editTable!!.guest04 = root.guest04.text.toString()
        editTable!!.guest05 = root.guest05.text.toString()
        editTable!!.guest06 = root.guest06.text.toString()
        editTable!!.guest07 = root.guest07.text.toString()
        editTable!!.guest08 = root.guest08.text.toString()
        editTable!!.guest09 = root.guest09.text.toString()
        editTable!!.guest10 = root.guest10.text.toString()
        editTable!!.guest11 = root.guest11.text.toString()
        editTable!!.guest12 = root.guest12.text.toString()

    }

    fun updateUserTable(userId: String, uid: String?, table: PlanModel) {
        app.database.child("user-tables").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(table)
                        activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFrame, TablePlanFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Table Plan error : ${error.message}")
                    }
                })
    }

    fun updateTable(uid: String?, table: PlanModel) {
        app.database.child("seatingplan").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(table)
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Seating Plan error : ${error.message}")
                    }
                })
    }
}
