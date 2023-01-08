package com.sena.mygamesapp.Interfaces

import com.sena.mygamesapp.Models.GameModel

//Create click interface for view Game Detail
//Send from gameId from adapter class and get id in Fragment class
interface GameClickListener {
    fun onGameClickListener(game : GameModel)
}


