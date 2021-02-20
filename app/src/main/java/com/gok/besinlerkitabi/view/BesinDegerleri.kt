package com.gok.besinlerkitabi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gok.besinlerkitabi.R
import com.gok.besinlerkitabi.adapter.BesinRecyclerAdapter
import com.gok.besinlerkitabi.util.gorselIndir
import com.gok.besinlerkitabi.util.placeholderYap
import com.gok.besinlerkitabi.viewmodel.BesinDegerleriViewModel
import com.gok.besinlerkitabi.viewmodel.BesinListesiViewModel
import kotlinx.android.synthetic.main.fragment_besin_degerleri.*


class BesinDegerleri : Fragment() {
    private lateinit var viewModel: BesinDegerleriViewModel
    private var besinId = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_besin_degerleri, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            besinId = BesinDegerleriArgs.fromBundle(it).besinId

        }
        viewModel = ViewModelProviders.of(this).get(BesinDegerleriViewModel::class.java)

        viewModel.roomVerisiAl(besinId)
        observeLiveData()
    }


    fun observeLiveData(){

        viewModel.besinLiveData.observe(viewLifecycleOwner, Observer {besin ->

            besin?.let {

                besinIsim.text = it.besinIsim
                besinKalori.text = it.besinKalori
                besinKarbonhidrat.text = it.besinKarbonhidrat
                besinProtein.text = it.besinProtein
                besinYag.text = it.besinYag
                context?.let {
                    besin_gorseli.gorselIndir(besin.besinGorsel, placeholderYap(it))
                }



            } })
    }
}