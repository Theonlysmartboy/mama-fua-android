package org.tridzen.mamafua.ui.home.launcher.settings

data class Settings(
    val title: String,
    val settings: ArrayList<String>?,
    val image: Int,
    val description: String,
    val options: ArrayList<String>?,
    val option: Boolean?
)
