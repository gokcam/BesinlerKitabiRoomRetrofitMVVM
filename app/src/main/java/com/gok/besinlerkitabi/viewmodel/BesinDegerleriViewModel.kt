package com.gok.besinlerkitabi.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gok.besinlerkitabi.model.Besin
import com.gok.besinlerkitabi.service.BesinDatabase
import kotlinx.coroutines.launch

class BesinDegerleriViewModel(application: Application) : BaseViewModel(application) {
    val besinLiveData = MutableLiveData<Besin>()





    fun roomVerisiAl(uuid : Int){
       launch {
           val dao = BesinDatabase(getApplication()).besinDao()
           val besin = dao.getBesin(uuid)
           besinLiveData.value = besin
       }
    }
}