package com.gok.besinlerkitabi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gok.besinlerkitabi.model.Besin
import com.gok.besinlerkitabi.service.BesinAPIServis
import com.gok.besinlerkitabi.service.BesinDatabase
import com.gok.besinlerkitabi.util.OzelSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class BesinListesiViewModel(application: Application) : BaseViewModel(application) {
    val besinler = MutableLiveData<List<Besin>>()
    val besinHataMesaji = MutableLiveData<Boolean>()
    val besinYukleniyor = MutableLiveData<Boolean>()

    private val besinApiServis = BesinAPIServis()
    private val disposable = CompositeDisposable()
    private val ozelSharedPreferences = OzelSharedPreferences(getApplication())
    private val guncellemeZamani = 10*60*1000*1000*1000L
fun refreshData(){


    val kaydedilmeZamani = ozelSharedPreferences.zamaniAl()
    if (kaydedilmeZamani != null && kaydedilmeZamani != 0L && System.nanoTime() - kaydedilmeZamani < guncellemeZamani){
        //Sqlite'tan çek
        verileriSQLitetanAl()
    } else {
        verileriInternettenAl()
    }

    /*
    var muz = Besin("Muz","100","500","80","70","wwww.aders.com")
    var cilek = Besin("Çilek","39","67","43","68","wwww.aders2.com")
    var elma = Besin("Elma","34","567","46","32","wwww.aders3.com")
    var armut = Besin("Elma","34","567","46","32","wwww.aders3.com")
    var karpuz = Besin("Elma","34","567","46","32","wwww.aders3.com")

    val besinListesi = arrayListOf<Besin>(muz,cilek,elma,armut,karpuz)

    besinler.value = besinListesi
    besinHataMesaji.value = false
    besinYukleniyor.value = false

     */
}

    fun refreshFromInternet(){
        verileriInternettenAl()
    }
    private fun verileriSQLitetanAl(){

        launch {
            val besinListesi = BesinDatabase(getApplication()).besinDao().getAllBesin()

            besinleriGoster(besinListesi)
            Toast.makeText(getApplication(),"Besinleri Room'dan Aldık", Toast.LENGTH_LONG).show()
        }
    }

    private  fun verileriInternettenAl()
    {
        besinYukleniyor.value=true
        disposable.add(
                besinApiServis.getData()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<List<Besin>>()


                        {
                            override fun onSuccess(t: List<Besin>) {
                                sqlLiteSakla(t)
                                Toast.makeText(getApplication(),"Besinleri internetten Aldık",Toast.LENGTH_LONG).show()

                            }

                            override fun onError(e: Throwable) {
                                besinHataMesaji.value=true
                                besinYukleniyor.value=false

                            }


                        })
        )
    }

    private fun sqlLiteSakla(besinListesi : List<Besin>) {
        launch {
            val dao = BesinDatabase(getApplication()).besinDao()
            dao.deleteAllBesin()
            val uuidListesi = dao.insertAll(*besinListesi.toTypedArray())
            var i = 0
            while (i<besinListesi.size)
            {
                besinListesi[i].uuid=uuidListesi[i].toInt()
                i=i+1

            }
            besinleriGoster(besinListesi)
        }
        ozelSharedPreferences.zamaniKaydet(System.nanoTime())

    }

    private fun besinleriGoster(besinlerListesi : List<Besin>){

        besinler.value = besinlerListesi
        besinHataMesaji.value=false
        besinYukleniyor.value=false

    }
}


