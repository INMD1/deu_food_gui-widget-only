package com.inmd1.deu_food_gui

class widgetItem(room: String, menu: String) {
    var room: String? = room
    var menu: String? = menu


    fun get_room(): String? {
        return room
    }


    fun set_room(_id: String?) {
        this.room = room
    }


    fun getmenu(): String? {
        return menu
    }

    fun setmenu(menu: String) {
        this.menu = menu
    }
}