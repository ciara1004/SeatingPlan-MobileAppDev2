package ie.wit.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.wit.R
import ie.wit.adapters.SeatingPlanAdapter
import ie.wit.adapters.TableListener
import ie.wit.main.SeatingPlanApp
import ie.wit.models.PlanModel
import ie.wit.utils.*
import kotlinx.android.synthetic.main.fragment_tableplan.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class TablePlanFragment : Fragment(), AnkoLogger,
    TableListener {

    lateinit var app: SeatingPlanApp
    lateinit var loader : AlertDialog
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as SeatingPlanApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_tableplan, container, false)
        activity?.title = getString(R.string.action_tableplan)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = root.recyclerView.adapter as SeatingPlanAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                deleteTable((viewHolder.itemView.tag as PlanModel).uid)
                deleteUserTable(app.auth.currentUser!!.uid,
                                  (viewHolder.itemView.tag as PlanModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onTableClick(viewHolder.itemView.tag as PlanModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.recyclerView)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TablePlanFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    open fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
                getAllTables(app.auth.currentUser!!.uid)
            }
        })
    }

    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }

    fun deleteUserTable(userId: String, uid: String?) {
        app.database.child("user-tables").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Seating Plan error : ${error.message}")
                    }
                })
    }

    fun deleteTable(uid: String?) {
        app.database.child("seatingplan").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Seating Plan error : ${error.message}")
                    }
                })
    }

    override fun onTableClick(table: PlanModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditFragment.newInstance(table))
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        if(this::class == TablePlanFragment::class)
            getAllTables(app.auth.currentUser!!.uid)
    }

    fun getAllTables(userId: String?) {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading Tables from Firebase")
        val tablesList = ArrayList<PlanModel>()
        app.database.child("user-tables").child(userId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Seating Plan error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val table = it.
                            getValue<PlanModel>(PlanModel::class.java)

                        tablesList.add(table!!)
                        root.recyclerView.adapter =
                            SeatingPlanAdapter(tablesList, this@TablePlanFragment,false)
                        root.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.database.child("user-tables").child(userId)
                            .removeEventListener(this)
                    }
                }
            })
    }
}
