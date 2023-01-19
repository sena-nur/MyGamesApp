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

    fun insertIfNotExists(game: FavGameModel){
        val favGames = getItemById(game.id)
        if(favGames!!.isEmpty()){
            insertGame(game)
        }
    }
}