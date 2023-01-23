package com.sena.mygamesapp.RoomDatabase

import androidx.room.*

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: FavGameModel)

    @Query("SELECT * FROM games_table")
    fun getAllGames(): List<FavGameModel>

    @Query("SELECT * from games_table WHERE id= :id")
    fun getItemById(id: Int): MutableList<FavGameModel?>?

    @Update
    fun updateGame(game: FavGameModel)

    @Delete
    fun deleteGame(game: FavGameModel) : Int

    @Query("DELETE FROM games_table WHERE id = :id")
    fun deleteById(id:Int): Int

    fun insertIfNotExists(game: FavGameModel) : Boolean{
        //If the game has not been added to favorites before, the insert operation is performed,
        // if it is added, it returns false.
        val favGames = getItemById(game.id)
        if(favGames!!.isEmpty()){
            insertGame(game)
            return true
        } else{
            return false
        }
    }
}