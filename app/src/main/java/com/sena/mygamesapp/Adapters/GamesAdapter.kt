package com.sena.mygamesapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sena.mygamesapp.Interfaces.GameClickListener
import com.sena.mygamesapp.Models.GameModel
import com.sena.mygamesapp.R
import com.sena.mygamesapp.RoomDatabase.ViewedGameModel
import com.sena.mygamesapp.databinding.RowGameBinding
//GamesAdapter to provide a data flow between the design and the model.
class GamesAdapter(var context: Context, var gameList: MutableList<GameModel>, var viewedGameList:MutableList<ViewedGameModel> ,private val gameClickListener: GameClickListener) :
    RecyclerView.Adapter<GamesAdapter.GamesViewHolder>() //connecting games data to RecyclerView via adapter
   {
       //Import 3 main methods of RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesViewHolder {
        val view = RowGameBinding.inflate(LayoutInflater.from(context) , parent,false)
        return GamesViewHolder(view) // This method is called to initialize the viewHolder when the Adapter is created.
    }

    override fun onBindViewHolder(holder: GamesViewHolder, position: Int) //Method to bind data returned from onCreateViewHolder() method
     {
        //selecting the game in the relevant position from the list and assigning it to the variable
        val game = gameList.get(position)
        holder.viewBinding.textViewGameName.setText(game.name)
        holder.viewBinding.textViewMetaCriticPoint.setText(game.metacritic.toString())
        if(game.backgroundImage !== null){
            Glide.with(context)  //Library to place the image link from the api inside imageViewGame
                .load(game.backgroundImage)
                .into(holder.viewBinding.imageViewGame)
        }
         val genreList = game.genres
         var genreListTxt = ""
         //Assign the elements in the genre list to a string variable, separated by commas
         for(i in 0 until genreList.size){
             if(i.equals(0))
                 genreListTxt = genreListTxt + genreList.get(i).name
             else{
                 genreListTxt = genreListTxt + ", " + genreList.get(i).name.lowercase()
             }
         }
         if(hasGameViewedBefore(game)){
             holder.viewBinding.gameRowLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.backgroundColor))
         } else{
             holder.viewBinding.gameRowLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
         }
         holder.viewBinding.textViewGenres.setText(genreListTxt)
         //Redirecting the clicked element in the game list to the detail page by id
         holder.viewBinding.gameRow.setOnClickListener{
             gameClickListener.onGameClickListener(game)
         }
    }

       fun hasGameViewedBefore(game : GameModel) : Boolean{
           for(i in 0 until viewedGameList.size){
               if(game.name.equals(viewedGameList.get(i).name)){
                   return true
               }
           }
           return false
       }

       inner class GamesViewHolder(var viewBinding: RowGameBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    }

    override fun getItemCount(): Int {
        return gameList.size //Returns the number of elements of the list.
    }

}