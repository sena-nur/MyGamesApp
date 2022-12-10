package com.sena.mygamesapp.Fragments

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sena.mygamesapp.Adapters.GamesAdapter
import com.sena.mygamesapp.AppConstants.Constants
import com.sena.mygamesapp.Interfaces.ApiClient
import com.sena.mygamesapp.Interfaces.ApiInterface
import com.sena.mygamesapp.Interfaces.GameClickListener
import com.sena.mygamesapp.Models.GameModel
import com.sena.mygamesapp.Models.ResponseModel
import com.sena.mygamesapp.R
import com.sena.mygamesapp.databinding.FragmentGamesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GamesFragment : Fragment(R.layout.fragment_games), GameClickListener {
    private var _binding: FragmentGamesBinding? = null // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    var currentPage = 1;
    val mutableAllGameList = mutableListOf<GameModel>()
    var searchText = ""

    //To set up an instance of the binding class for use with an activity, perform the following steps in the activity's onCreate() method
    //called to do initial creation of the fragment.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) //onCreateView() method is called and assuming that we provided our Fragment with a non-null view(via view binding) the view returned from this method will be the one shown to the user.
    {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGamesBinding.bind(view) //instance of the binding class for the fragment to use.
        binding.gamesRecyclerview.layoutManager = LinearLayoutManager(context) // set a LinearLayoutManager to handle Android //RecyclerView behavior
        //Başta liste hiç sürüklenmediği için state null olarak başlatıyoruz
        getGameList(10,1,null,searchText);
        binding.gamesRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener()
        //Setting the games list in the recycler view as a dynamic structure that can scroll
        //Sayfanın en altına gidilip gidilmediğini kontrol eder en alta gidildiyse page++ olur ve method diğer sayfa için çağrılır
        {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)){
                    val recyclerViewState = binding.gamesRecyclerview.getLayoutManager()?.onSaveInstanceState();
                    currentPage++
                    getGameList(Constants.PAGE_SIZE,currentPage,recyclerViewState,searchText)
                }
            }

        })
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener

        {

            override fun onQueryTextChange(newText: String): Boolean //Called when the query text is changed by the user.
            {
                if(newText.equals("")){
                    this.onQueryTextSubmit("");
                } else if(newText.length>3){
                    this.onQueryTextSubmit(newText) //if text length more than 3 it should search for that string
                }
                return false //false if the SearchView should perform the default action of showing any suggestions
            }

            override fun onQueryTextSubmit(query: String): Boolean //the query text that is to be submitted
            {
                currentPage = 1
                getGameList(Constants.PAGE_SIZE,currentPage,null,query)
                return false //false to let the SearchView perform the default action
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView() //This method will get called when the host Activity is in the DESTROYED state
        // clean up any references to the binding class instance in the fragment's
        _binding = null //_binding = null, which is  manually setting View Binding object to null to prevent memory leaks and to ensure that each call of onCreateView() returns a fresh and newly updated View.
    }
    fun getGameList(pageSize:Int, pageIndex:Int, state: Parcelable?, searchText:String){
        //oyunları çekmet methodu çağrırıldığı an ekrana yükleniyor diyaloğunu getir
        //bu diyalog ekrandayken başka bir şeye tıklatmıyor
        binding.loadingLayout.isVisible = true;
        //ApiInterface classı içinde tanımladığım methodları kullanabilmek için ApiClient
        //içerisindeki buildService methoduna ApiInterface classımı yolluyorum
        val serviceGenerator = ApiClient.buildService(ApiInterface::class.java)
        val call = serviceGenerator.getGamesList(pageSize,pageIndex,searchText,Constants.API_KEY)
        call.enqueue(object: Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful) {
                    binding.loadingLayout.isVisible = false;
                    //recyclerview içerisine başarılı dönen response verilerini yazdırma
                    binding.gamesRecyclerview.apply {
                        //layout manager ekrana verileri listeli gösterebilmek için gereklidir
                        layoutManager= LinearLayoutManager(context)
                        binding.gamesRecyclerview.layoutManager = LinearLayoutManager(context)
                        //ilk sayfa için listeyi temizliyoruz çünkü önceki sayfası yok
                        // gelen 10 veriyi direkt olarak ekleyip gösteriyoruz
                        if(pageIndex.equals(1)){
                            mutableAllGameList.clear()
                            //response body null değilse mutableallgameliste tüm resultları ekle
                            response.body()?.let { mutableAllGameList.addAll(it.results) }
                            adapter= GamesAdapter(context, mutableAllGameList,this@GamesFragment)
                            // sayfa 1 değilse daha önce oyunları içeren listeyi temizlemiyoruz
                            // çünkü dah aönceki verilerin altına bu yeni gelenleri ekledik
                            //alt alta binen veriler toplandı
                        } else if(state != null){
                            response.body()?.let { mutableAllGameList.addAll(it.results) }
                            adapter?.notifyDataSetChanged()
                            // listenin tam olarak neresinde olduğumuzu alıp kaldığımız yerden devam etmemizi sağladı
                            binding.gamesRecyclerview.getLayoutManager()?.onRestoreInstanceState(state);
                        } else{
                            response.body()?.let { mutableAllGameList.addAll(it.results) }
                            adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t:Throwable) {
                binding.loadingLayout.isVisible = false;
                t.printStackTrace()
                Log.e("Failure", t.message.toString())
            }
        })
    }

    override fun onGameClickListener(gameId : Int) {
        Toast.makeText(context,"DENEME",Toast.LENGTH_LONG).show()
        val bundle = Bundle()
        bundle.putInt("gameId",gameId)
        view?.findNavController()?.navigate(R.id.action_games_dest_to_gameDetailFragment,bundle)

    }
}