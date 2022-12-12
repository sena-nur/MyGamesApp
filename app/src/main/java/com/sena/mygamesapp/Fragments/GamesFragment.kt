package com.sena.mygamesapp.Fragments

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sena.mygamesapp.Adapters.GamesAdapter
import com.sena.mygamesapp.AppConstants.Constants
import com.sena.mygamesapp.Retrofit.ApiClient
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
    var currentPage = 1
    val mutableAllGameList = mutableListOf<GameModel>()
    var searchText = ""
    var adapter: GamesAdapter? = null

    //To set up an instance of the binding class for use with an activity, perform the following steps in the activity's onCreate() method
    //called to do initial creation of the fragment.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        //onViewCreated() method is called and assuming that we provided our Fragment with a non-null
        // view(via view binding) the view returned from this method will be the one shown to the user.
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGamesBinding.bind(view)
        //instance of the binding class for the fragment to use.
        binding.gamesRecyclerview.layoutManager = LinearLayoutManager(context) // set a LinearLayoutManager to handle Android //RecyclerView behavior
        //Initially state is initialized to null because the list is never dragged
        getGameList(10,1,null,searchText) //getGameList() called
        binding.gamesRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener()
        //Setting the games list in the recycler view as a dynamic structure that can scroll
        {
            //It checks if the page has gone to the bottom, if it is to the bottom,
            // it becomes page++ and the method is called for the other page
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
                    this.onQueryTextSubmit("") //pull list unfiltered if search string is empty
                } else if(newText.length>3){
                    this.onQueryTextSubmit(newText) //if text length more than 3 it should search for that string
                }
                return false //false if the SearchView should perform the default action of showing any suggestions
            }

            override fun onQueryTextSubmit(query: String): Boolean //the query text that is to be submitted
            {
                //If the search string length is in the range of 0-4, the game list is cleared,
                // the adapter is updated, and the text indicating that there is no game searched is displayed on the screen.
                currentPage = 1
                if(query.length > 0 && query.length < 4){
                    mutableAllGameList.clear()
                    adapter?.notifyDataSetChanged()
                    binding.noSearchLayout.isVisible = true
                } else{
                    //When the length of the search string is 0 or greater than 3,
                    // the game not searched text will be invisible and the getGameList method will be called.
                    binding.noSearchLayout.isVisible = false
                    getGameList(Constants.PAGE_SIZE,currentPage,null,query)
                }
                return false //false to let the SearchView perform the default action
            }

        })
    }
    override fun onDestroyView() {
        super.onDestroyView() //This method will get called when the host Activity is in the DESTROYED state
        // clean up any references to the binding class instance in the fragment's
        _binding = null //_binding = null, which is  manually setting View Binding object to null to prevent memory
        // leaks and to ensure that each call of onCreateView() returns a fresh and newly updated View.
    }
    fun getGameList(pageSize:Int, pageIndex:Int, state: Parcelable?, searchText:String){
        //Get the loading dialog when the getGameList method is called
        //this dialog does not allow clicking anything else on the screen.
        binding.loadingLayout.isVisible = true;
        //ApiClient to use the methods defined in the ApiInterface class
        //ApiInterface class is sent to the buildService method in it
        val serviceGenerator = ApiClient.buildService(ApiInterface::class.java)
        // To pull game list, call api with service generator
        val call = serviceGenerator.getGamesList(pageSize,pageIndex,searchText,Constants.API_KEY)
        call.enqueue(object: Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful) {
                    binding.loadingLayout.isVisible = false;
                    //printing the successful response data into the recyclerview
                    binding.gamesRecyclerview.apply {
                        //The layout manager is required to display the data listed on the screen.
                        layoutManager= LinearLayoutManager(context)
                        binding.gamesRecyclerview.layoutManager = LinearLayoutManager(context)
                        if(pageIndex.equals(1)){
                            //the list is cleared for the first page because it has no previous page
                            mutableAllGameList.clear()
                            //If response body is not null add all results in mutableAllGamelist
                            response.body()?.let { mutableAllGameList.addAll(it.results) }
                            adapter= GamesAdapter(context, mutableAllGameList,this@GamesFragment)

                        } else if(state != null){
                            //If the page is not 1, the list containing the previous games is not clear.
                            // because we added this newcomer below the previous data
                            //collect overlapping data
                            response.body()?.let { mutableAllGameList.addAll(it.results) }
                            adapter?.notifyDataSetChanged()
                            //allowed us to pick up exactly where we were on the list and pick up where we left off
                            binding.gamesRecyclerview.getLayoutManager()?.onRestoreInstanceState(state);
                        } else{
                            response.body()?.let { mutableAllGameList.addAll(it.results) }
                            adapter?.notifyDataSetChanged() //If state is null, reload the list and warn the adapter
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
        //taking the id of the clicked game in the list and directing it to the detail fragment
        val bundle = Bundle()
        bundle.putInt("gameId",gameId)
        view?.findNavController()?.navigate(R.id.action_games_dest_to_gameDetailFragment,bundle)
    }
}