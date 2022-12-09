package com.sena.mygamesapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sena.mygamesapp.Models.GameModel
import com.sena.mygamesapp.databinding.RowGameBinding
//GamesAdapter to provide a data flow between the design and the model.
class GamesAdapter(var context: Context, var gameList: MutableList<GameModel>) :
    RecyclerView.Adapter<GamesAdapter.HobbiesViewHolder>() //connecting games data to RecyclerView via adapter
   {
       //Import 3 main methods of RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbiesViewHolder {
        val view = RowGameBinding.inflate(LayoutInflater.from(context) , parent,false)
        return HobbiesViewHolder(view) // This method is called to initialize the viewHolder when the Adapter is created.
    }

    override fun onBindViewHolder(holder: HobbiesViewHolder, position: Int) //Method to bind data returned from onCreateViewHolder() method
     {
        val game = gameList.get(position)
        holder.viewBinding.textViewGameName.setText(game.name)
        holder.viewBinding.textViewMetaCriticPoint.setText(game.metacritic.toString())
        if(game.backgroundImage !== null){
            Glide.with(context)  //Library to place the image link from the api inside imageViewGame
                .load(game.backgroundImage)
                .into(holder.viewBinding.imageViewGame)
        }
    }

    inner class HobbiesViewHolder(var viewBinding: RowGameBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    }

    override fun getItemCount(): Int {
        return gameList.size //Returns the number of elements of the list.
    }

}