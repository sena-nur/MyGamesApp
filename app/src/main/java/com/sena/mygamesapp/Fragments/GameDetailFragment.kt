package com.sena.mygamesapp.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.bumptech.glide.Glide
import com.sena.mygamesapp.AppConstants.Constants
import com.sena.mygamesapp.Interfaces.ApiInterface
import com.sena.mygamesapp.Models.GameDetailModel
import com.sena.mygamesapp.R
import com.sena.mygamesapp.Retrofit.ApiClient
import com.sena.mygamesapp.RoomDatabase.FavGameDatabase
import com.sena.mygamesapp.RoomDatabase.GameDao
import com.sena.mygamesapp.databinding.FragmentGameDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameDetailFragment : Fragment(R.layout.fragment_game_detail) {
    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!
    //initially assigning the number of clicks to 0
    private var clickNo = 0
    private lateinit var gameDao: GameDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Defining binding to use the layout on the detail page
        _binding = FragmentGameDetailBinding.bind(view)
        //Getting the game id variable sent from the game list page
        val gameId = arguments?.getInt("gameId")
        //Sending the received game id variable as a parameter to the getGameDetail function
        gameId?.let { getGameDetail(it) }
        binding.gameDesc.setOnClickListener {
            if(++clickNo % 2 == 1) //the first click on the game description the read more description is visible
                binding.gameDesc.maxLines = Int.MAX_VALUE
            else // Every 2nd click, 4 rows are displayed as the max value to narrow the description
                binding.gameDesc.maxLines = 4
        }
        binding.backToGames.setOnClickListener {
            requireActivity().onBackPressed()
            //Clicking on Games returns to gamesFragment back
        }
        val favDb = Room.databaseBuilder(
            // Build the room database where the favorite games are kept
            requireContext(),
            FavGameDatabase::class.java, "game_database"
        ).build()
        gameDao = favDb.gameDao()
        lifecycleScope.launch(Dispatchers.IO) {
            //By taking the id of the game, a query is made according to this id in the list of favorite games.
            // If the list is not empty, the favorite text in the upper right becomes visible.
            val gameList = gameDao.getItemById(gameId!!)
            if(!gameList!!.isEmpty()){
                requireActivity().runOnUiThread {
                    binding.favTopText.isVisible = true
                }
            }
        }
    }

    override fun onDestroyView() {
        //clean up any references to the binding class instance in the fragment's
        super.onDestroyView()
        _binding = null
    }

    fun getGameDetail(gameId:Int){
        val serviceGenerator = ApiClient.buildService(ApiInterface::class.java)
        val call = serviceGenerator.getGameDetail(gameId,Constants.API_KEY)
        call.enqueue(object: Callback<GameDetailModel> {
            override fun onResponse(call: Call<GameDetailModel>, response: Response<GameDetailModel>) {
                if(response.isSuccessful) {
                    //If the response from the api is successful, the game detail data is assigned to the variable
                    val gameDetailResponse = response.body()
                    //Parse the html type game description and display it as text
                    binding.gameDesc.setText(HtmlCompat.fromHtml(gameDetailResponse!!.description, HtmlCompat.FROM_HTML_MODE_LEGACY))

                    context?.let {
                        Glide.with(it) //if context is not null-> library to place the image link from the api inside imageViewGame
                            .load(gameDetailResponse.background_image)
                            .into(binding.gameImage)
                    }
                    //set game name above game picture
                    binding.gameName.setText(gameDetailResponse.name)
                    //show the website of the game in the browser when clicked
                    binding.websiteLayout.setOnClickListener{
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data = Uri.parse(gameDetailResponse.website)
                        startActivity(openURL)
                    }
                    //show the game's reddit link in the browser when clicked
                    binding.redditLayout.setOnClickListener{
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data = Uri.parse(gameDetailResponse.reddit_url)
                        startActivity(openURL)
                    }
                }
            }

            override fun onFailure(call: Call<GameDetailModel>, t:Throwable) {
                //error is logged when the call fails
                t.printStackTrace()
                Log.e("Failure", t.message.toString())
            }
        })
    }
}
