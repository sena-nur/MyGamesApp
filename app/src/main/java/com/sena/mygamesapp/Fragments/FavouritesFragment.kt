package com.sena.mygamesapp.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
    private var gameList: List<FavGameModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // bind to use the layout.
        _binding = FragmentFavouritesBinding.bind(view)
        // Build the room database where the favorite games are kept
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
        //assigning favorite games in the database to a list and displaying the number of games in the title then show list on screen
        lifecycleScope.launch(Dispatchers.IO) {
            gameList = gameDao.getAllGames()
            setFragmentTitle(gameList.size)
            populateFavList(gameList)
        }
    }
    fun setFragmentTitle(gameCount : Int ){
        //if there are no favorite games, the title will not show the number of games
        if(gameCount==0){
            binding.favGamesTitle.setText(getString(R.string.favourites_top))
        } else{
            //If at least one game is in the favourites, the number of games is shown in parentheses next to the title.
            binding.favGamesTitle.setText(getString(R.string.favourites_top) + "(" + gameCount + ")")
        }
    }
    fun populateFavList(games : List<FavGameModel>){
        binding.gamesRecyclerview.apply {
            // main scope to do some ui operations
            MainScope().launch {
                withContext(Dispatchers.Default) {

                }
                //The layout manager is required to display the data listed on the screen.
                layoutManager= LinearLayoutManager(context)
                binding.gamesRecyclerview.layoutManager = LinearLayoutManager(context)
                if(!games.isEmpty()){
                    // if the favorite game list is not empty, set the list to the adapter and define the swipe action
                    adapter= FavGamesAdapter(context, games,this@FavouritesFragment)
                    var itemTouchHelper = getCurrentItemTouchHelper()
                    itemTouchHelper.attachToRecyclerView(binding.gamesRecyclerview)
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
    private fun getCurrentItemTouchHelper() : ItemTouchHelper{
        // set a favorites game to remove from favorites by swiping left and show the dialog
        var itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val favGame: FavGameModel = gameList.get(viewHolder.adapterPosition)
                showDeleteDialog(favGame)
            }
        })
        return itemTouchHelper
    }
    private fun showDeleteDialog(game: FavGameModel){
        //asking the user if they want to remove the favorite game from the list
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.sure_to_delete_title)
        builder.setMessage(R.string.sure_to_delete_game)
        builder.setPositiveButton(R.string.yes) { dialog, which ->
            deleteGameFromFav(game)
        }
        builder.setNegativeButton(R.string.no) { dialog, which ->
            getAllFavGames()
            Toast.makeText(context,R.string.delete_cancelled, Toast.LENGTH_SHORT).show()

        }
        builder.show()
    }
    private fun deleteGameFromFav(game:FavGameModel){
        //The removal of the selected game from favorites is done here,
        // if the operation is successful, the message that the game has been removed is displayed.
        lifecycleScope.launch(Dispatchers.IO) {
            val remove = gameDao.deleteById(game.id)
            if(remove == 1){
                getAllFavGames()
                Snackbar.make(binding.gamesRecyclerview, game.name + " removed from favourites", Snackbar.LENGTH_LONG).show()
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
