package com.sena.mygamesapp.Fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.sena.mygamesapp.Adapters.FavGamesAdapter
import com.sena.mygamesapp.Interfaces.FavGameClickListener
import com.sena.mygamesapp.R
import com.sena.mygamesapp.RoomDatabase.FavGameModel
import com.sena.mygamesapp.RoomDatabase.GameDao
import com.sena.mygamesapp.RoomDatabase.FavGameDatabase
import com.sena.mygamesapp.databinding.FragmentFavouritesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesFragment : Fragment(R.layout.fragment_favourites), FavGameClickListener {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var gameDao: GameDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavouritesBinding.bind(view)
        val db = Room.databaseBuilder(
            requireContext(),
            FavGameDatabase::class.java, "game_database"
        ).build()
        gameDao = db.gameDao()
        getAllFavGames()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun getAllFavGames(){
        lifecycleScope.launch(Dispatchers.IO) {
            val games = gameDao.getAllGames()
            populateFavList(games)
        }
    }
    fun populateFavList(games : List<FavGameModel>){
        binding.gamesRecyclerview.apply {
            //The layout manager is required to display the data listed on the screen.
            MainScope().launch {
                withContext(Dispatchers.Default) {

                }
                layoutManager= LinearLayoutManager(context)
                binding.gamesRecyclerview.layoutManager = LinearLayoutManager(context)
                if(!games.isEmpty()){
                    adapter= FavGamesAdapter(context, games,this@FavouritesFragment)
                    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
                            return false
                        }
                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            val favGame: FavGameModel = games.get(viewHolder.adapterPosition)
                            deleteGameFromFav(favGame)
                            Snackbar.make(binding.gamesRecyclerview, favGame.name + " removed from favourites", Snackbar.LENGTH_LONG).show()
                        }
                    }).attachToRecyclerView(binding.gamesRecyclerview)
                    binding.noFavGames.isVisible = false
                    binding.gamesRecyclerview.isVisible = true
                }
                else{
                    binding.noFavGames.isVisible = true
                    binding.gamesRecyclerview.isVisible = false
                }
            }

        }
    }
    private fun deleteGameFromFav(game:FavGameModel){
        lifecycleScope.launch(Dispatchers.IO) {
            val remove = gameDao.deleteGame(game)
            if(remove == 1){
                getAllFavGames()
            }
        }
    }
    override fun onFavGameClickListener(id : Int) {
        //taking the id of the clicked game in the list and directing it to the detail fragment
        val bundle = Bundle()
        bundle.putInt("gameId",id)
        view?.findNavController()?.navigate(R.id.gameDetailFragment,bundle)
    }
}
