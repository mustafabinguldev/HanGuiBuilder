package tech.bingulhan.hanguiapi.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import tech.bingulhan.hanguiapi.gui.item.GuiItem;

import java.util.*;

/**
 * @author BingulHan
 */
public final class HanGuiBuilder implements Listener {

    private int id;
    private Inventory inventory;

    private HashMap<Integer, GuiItem> itemHashMap;

    private Random random;

    private String guiTitle;

    private Size size;

    private boolean accessibleOnDragItems = false;

    private Set<OfflinePlayer> players;

    @Getter
    private JavaPlugin plugin;


    public HanGuiBuilder(@NotNull HanGuiBuilder.Size size, @NotNull String guiTitle, JavaPlugin pl) {

        random = new Random();
        this.size = size;
        this.guiTitle = guiTitle;
        this.players = new LinkedHashSet<>();
        this.itemHashMap = new HashMap<>();

        this.plugin = pl;

        loadGui();
    }

    public final HanGuiBuilder setAccessibleOnDragItems(boolean value) {
        accessibleOnDragItems = value;
        return this;
    }

    protected void loadGui() {

        id = this.random.nextInt(1000);
        inventory = Bukkit.createInventory(null,this.size.size, ChatColor.translateAlternateColorCodes('&', guiTitle));

        inventory.setMaxStackSize(id);

        Bukkit.getServer().getPluginManager().registerEvents(this, this.plugin);

    }

    public final HanGuiBuilder addItem(int slot, GuiItem item) {

        if (slot > size.size) {
            return this;
        }

        if (itemHashMap.get(slot) != null) {
            itemHashMap.replace(slot, item);
        }else{
            itemHashMap.put(slot, item);
        }
        return this;
    }

    public final HanGuiBuilder addPlayer(OfflinePlayer player) {
        players.add(player);
        return this;
    }

    public final HanGuiBuilder removePlayer(OfflinePlayer player) {
        players.remove(player);
        return this;
    }
    public final void open() {

        for (int slot : itemHashMap.keySet()) {
            if (slot > size.size) {
                continue;
            }

            GuiItem item = itemHashMap.get(slot);
            inventory.setItem(slot, item.getItem());
        }


        for (OfflinePlayer player : players) {
            if (player.isOnline()) {
                player.getPlayer().openInventory(this.inventory);
            }else {
                players.remove(player);
            }
        }
    }

    public enum Size {
        ONE(9),DOUBLE(18),THREE(27),FOUR(36),FIVE(45),SIX(54);
        public int size;
        Size(int size) {
            this.size = size;
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getMaxStackSize() == id) {
            if (!this.accessibleOnDragItems) {
                event.setCancelled(true);
            }

            int slot = event.getSlot();
            if (itemHashMap.get(slot) != null) {
                GuiItem item = itemHashMap.get(slot);
                item.getAttraction().execute(((Player) event.getWhoClicked()), this);
            }

        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        if (event.getInventory().getMaxStackSize() == id) {
            players.remove(event.getPlayer());

            if (players.size()<1) {
                HandlerList.unregisterAll(this);
            }
        }

    }


}
