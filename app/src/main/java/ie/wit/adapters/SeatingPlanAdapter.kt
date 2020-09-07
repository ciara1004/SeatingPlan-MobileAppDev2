package ie.wit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.R
import ie.wit.models.PlanModel
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_tables.view.*



interface TableListener {
    fun onTableClick(table: PlanModel)
}

class SeatingPlanAdapter constructor(var tables: ArrayList<PlanModel>,
                                  private val listener: TableListener, reportall : Boolean)
    : RecyclerView.Adapter<SeatingPlanAdapter.MainHolder>() {

    val reportAll = reportall

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_tables,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val table = tables[holder.adapterPosition]
        holder.bind(table,listener,reportAll)
    }

    override fun getItemCount(): Int = tables.size

    fun removeAt(position: Int) {
        tables.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(table: PlanModel, listener: TableListener, reportAll: Boolean) {
            itemView.tag = table
            itemView.guests.text = table.tableNo.toString()
            itemView.tableNo.text = table.guest01
            itemView.tableNo.text = table.guest02
            itemView.tableNo.text = table.guest03
            itemView.tableNo.text = table.guest04
            itemView.tableNo.text = table.guest05
            itemView.tableNo.text = table.guest06
            itemView.tableNo.text = table.guest07
            itemView.tableNo.text = table.guest08
            itemView.tableNo.text = table.guest09
            itemView.tableNo.text = table.guest10
            itemView.tableNo.text = table.guest11
            itemView.tableNo.text = table.guest12



            if(!reportAll)
                itemView.setOnClickListener { listener.onTableClick(table) }

            if(!table.profilepic.isEmpty()) {
                Picasso.get().load(table.profilepic.toUri())
                    //.resize(180, 180)
                    .transform(CropCircleTransformation())
                    .into(itemView.imageIcon)
            }
            else
                itemView.imageIcon.setImageResource(R.mipmap.ic_launcher_logo_round)
        }
    }
}