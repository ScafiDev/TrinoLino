package com.example.trinolino.Object

import com.example.trinolino.Classi.CartItem
import com.example.trinolino.Classi.MenuItem

object Cart {
    private val items: MutableList<CartItem> = mutableListOf()

    fun addItem(item: MenuItem, quantity: Int, ticketId: Int) {
        val existingItem = items.find { it.item.id == item.id && it.ticketId == ticketId }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            items.add(CartItem(item, quantity, ticketId))
        }
    }

    fun removeItem(cartItem: CartItem) {
        items.remove(cartItem)
    }

    fun clear() {
        items.clear()
    }

    fun getItems(): List<CartItem> {
        return items
    }

    fun getTotalPrice(): Int {
        return items.sumBy { it.totalPrice }
    }
}