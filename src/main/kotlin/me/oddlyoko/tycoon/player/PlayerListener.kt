package me.oddlyoko.tycoon.player

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.inventory.InventoryPickupArrowEvent
import cn.nukkit.event.inventory.InventoryPickupItemEvent
import cn.nukkit.event.player.PlayerAsyncPreLoginEvent
import cn.nukkit.event.player.PlayerQuitEvent
import me.oddlyoko.tycoon.item.ItemManager

object PlayerListener : Listener {

    @EventHandler
    fun onPlayerJoin(e: PlayerAsyncPreLoginEvent) {
        PlayerManager.registerPlayer(e.player.uniqueId).get()
    }

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        PlayerManager.unregisterPlayer(e.player.uniqueId)
    }

    @EventHandler
    fun onPlayerPickupItem(e: InventoryPickupItemEvent) {
        e.setCancelled()
        val item = e.item.item
        if (!item.hasCompoundTag() || !item.namedTag.contains("tycoon"))
            return
        val itemId = item.namedTag.getString("tycoon_item")
        if (itemId.isEmpty())
            return
        val theItem = ItemManager.getItem(itemId) ?: return
        val holder = e.inventory.holder
        if (holder !is cn.nukkit.Player)
            return
        val thePlayer = PlayerManager[holder.uniqueId] ?: return
        e.item.close()
        thePlayer.addItem(theItem.toPlayerItem())
    }

    @EventHandler
    fun onPlayerPickupArrow(e: InventoryPickupArrowEvent) {
        e.setCancelled()
    }
}
