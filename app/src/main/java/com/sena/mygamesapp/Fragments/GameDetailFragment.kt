package com.sena.mygamesapp.Fragments

import android.os.Bundle
import android.os.Parcelable
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sena.mygamesapp.Adapters.GamesAdapter
import com.sena.mygamesapp.AppConstants.Constants
import com.sena.mygamesapp.Dialogs.CustomDialog
import com.sena.mygamesapp.Interfaces.ApiClient
import com.sena.mygamesapp.Interfaces.ApiInterface
import com.sena.mygamesapp.Models.GameDetailModel
import com.sena.mygamesapp.Models.ResponseModel
import com.sena.mygamesapp.R
import com.sena.mygamesapp.databinding.FragmentGameDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameDetailFragment : Fragment(R.layout.fragment_game_detail) {
    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameDetailBinding.bind(view)
        val gameId = arguments?.getInt("gameId")
        gameId?.let { getGameDetail(it) }
        binding.gameDesc.setOnClickListener {
            binding.gameDesc.maxLines = Int.MAX_VALUE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getGameDetail(gameId:Int){
        //binding.loadingLayout.isVisible = true;
        val serviceGenerator = ApiClient.buildService(ApiInterface::class.java)
        val call = serviceGenerator.getGameDetail(gameId,Constants.API_KEY)
        call.enqueue(object: Callback<GameDetailModel> {
            override fun onResponse(call: Call<GameDetailModel>, response: Response<GameDetailModel>) {
                if(response.isSuccessful) {
                    val gameDetailResponse = response.body()
                    //binding.gameDesc.setText(gameDetailResponse!!.description)
                    binding.gameDesc.setText(HtmlCompat.fromHtml(gameDetailResponse!!.description, HtmlCompat.FROM_HTML_MODE_LEGACY))
                    context?.let {
                        Glide.with(it)  //Library to place the image link from the api inside imageViewGame
                            .load(gameDetailResponse.background_image)
                            .into(binding.gameImage)
                    }
                }
            }

            override fun onFailure(call: Call<GameDetailModel>, t:Throwable) {
                //binding.loadingLayout.isVisible = false;
                t.printStackTrace()
                Log.e("Failure", t.message.toString())
            }
        })
    }
}