package com.gok.besinlerkitabi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.gok.besinlerkitabi.R
import com.gok.besinlerkitabi.model.Besin
import com.gok.besinlerkitabi.util.gorselIndir
import com.gok.besinlerkitabi.util.placeholderYap
import com.gok.besinlerkitabi.view.BesinListesiDirections
import kotlinx.android.synthetic.main.basin_recycler_row.view.*

class BesinRecyclerAdapter(val besinListesi : ArrayList<Besin>) : RecyclerView.Adapter<BesinRecyclerAdapter.BesinViewHolder>() {
    class BesinViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BesinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.basin_recycler_row,parent,false)
        return BesinViewHolder(view)
    }

    override fun onBindViewHolder(holder: BesinViewHolder, position: Int) {
        holder.itemView.besin.text = besinListesi.get(position).besinIsim
        holder.itemView.kalori.text = besinListesi.get(position).besinKalori


        holder.itemView.setOnClickListener {
            val action = BesinListesiDirections.actionBesinListesiToBesinDegerleri(besinListesi.get(position).uuid)
            Navigation.findNavController(it).navigate(action)
        }
        holder.itemView.imageView.gorselIndir(besinListesi.get(position).besinGorsel, placeholderYap(holder.itemView.context))
    }

    fun besinListesiniGuncelle(yeniBesinListesi : List<Besin>)
    {

        besinListesi.clear()
        besinListesi.addAll(yeniBesinListesi)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return  besinListesi.size
    }
}