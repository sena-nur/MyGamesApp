package com.sena.mygamesapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sena.mygamesapp.Models.GameModel
import com.sena.mygamesapp.databinding.RowGameBinding

class GamesAdapter(var context: Context, var gameList: MutableList<GameModel>) :
    RecyclerView.Adapter<GamesAdapter.HobbiesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbiesViewHolder {
        val view = RowGameBinding.inflate(LayoutInflater.from(context) , parent,false)
        return HobbiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: HobbiesViewHolder, position: Int) {
        val game = gameList.get(position)
        holder.viewBinding.textViewGameName.setText(game.name)
        holder.viewBinding.textViewMetaCriticPoint.setText(game.metacritic.toString())
        if(game.backgroundImage !== null){
            Glide.with(context)
                .load(game.backgroundImage)
                .into(holder.viewBinding.imageViewGame)
        }
    }

    inner class HobbiesViewHolder(var viewBinding: RowGameBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

}