package com.sena.mygamesapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sena.mygamesapp.Adapters.GamesAdapter
import com.sena.mygamesapp.AppConstants.Constants
import com.sena.mygamesapp.Interfaces.ApiClient
import com.sena.mygamesapp.Interfaces.ApiInterface
import com.sena.mygamesapp.Models.GameModel
import com.sena.mygamesapp.Models.ResponseModel
import com.sena.mygamesapp.R
import com.sena.mygamesapp.databinding.FragmentGamesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GamesFragment : Fragment(R.layout.fragment_games) {
    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGamesBinding.bind(view)
        binding.gamesRecyclerview.layoutManager = LinearLayoutManager(context)
        val serviceGenerator = ApiClient.buildService(ApiInterface::class.java)
        val call = serviceGenerator.getGamesList(10,1,Constants.API_KEY)
        call.enqueue(object: Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful) {
                    binding.gamesRecyclerview.apply {
                        layoutManager= LinearLayoutManager(context)
                        binding.gamesRecyclerview.layoutManager = LinearLayoutManager(context)
                        adapter= response.body()?.let { GamesAdapter(context, it.results) }
                    }

                }
            }

            override fun onFailure(call: Call<ResponseModel>, t:Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_games, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}