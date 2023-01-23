package com.sena.mygamesapp.Adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sena.mygamesapp.Interfaces.FavGameClickListener
import com.sena.mygamesapp.RoomDatabase.FavGameModel
import com.sena.mygamesapp.databinding.RowGameBinding
//GamesAdapter to provide a data flow between the design and the model.
class FavGamesAdapter(var context: Context, var gameList: List<FavGameModel>, private val favGameClickListener: FavGameClickListener) :
    RecyclerView.Adapter<FavGamesAdapter.GamesViewHolder>() //connecting games data to RecyclerView via adapter
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
        if(game.background_image !== null){
            Glide.with(context)  //Library to place the image link from the api inside imageViewGame
                .load(game.background_image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return true
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        holder.viewBinding.imageLayout.isVisible = true
                        holder.viewBinding.loadingLayout.isVisible = false
                        //do something when picture already loaded
                        return false
                    }
                })
                .into(holder.viewBinding.imageViewGame)
        }
        val genreList = game.genres
        holder.viewBinding.textViewGenres.setText(genreList)
        //Redirecting the clicked element in the game list to the detail page by id
        holder.viewBinding.gameRow.setOnClickListener{
            favGameClickListener.onFavGameClickListener(game.id)
        }
    }

    inner class GamesViewHolder(var viewBinding: RowGameBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    }

    override fun getItemCount(): Int {
        return gameList.size //Returns the number of elements of the list.
    }

}