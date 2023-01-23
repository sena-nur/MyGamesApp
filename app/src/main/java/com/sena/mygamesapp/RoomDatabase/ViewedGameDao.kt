package com.sena.mygamesapp.RoomDatabase

import androidx.room.*

@Dao
interface ViewedGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: ViewedGameModel)

    @Query("SELECT * FROM viewed_games_table")
    fun getAllGames(): MutableList<ViewedGameModel>

    @Query("SELECT * from viewed_games_table WHERE id= :id")
    fun getItemById(id: Int): MutableList<ViewedGameModel?>?

    fun insertIfNotExists(game: ViewedGameModel){
        //inserts the game if the game has not been viewed before
        val viewedGames = getItemById(game.id)
        if(viewedGames!!.isEmpty()){
            insertGame(game)
        }
    }
}