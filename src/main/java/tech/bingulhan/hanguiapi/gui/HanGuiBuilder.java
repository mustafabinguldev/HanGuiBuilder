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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import tech.bingulhan.hanguiapi.gui.data.GuiData;
import tech.bingulhan.hanguiapi.gui.item.GuiItem;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author BingulHan
 */
public final class HanGuiBuilder implements Listener {

    private int id;
    private Inventory inventory;

    private CloseAttraction closeAttraction;

    private HashMap<Integer, GuiItem> itemHashMap;

    private Random random;

    @Setter
    private String guiTitle = "";

    private Size size;

    private boolean accessibleOnDragItems = false;

    private Set<OfflinePlayer> players;

    @Getter
    private JavaPlugin plugin;

    private List<GuiData> dataList;


    public final void setCloseAttraction(CloseAttraction attraction) {
        this.closeAttraction = attraction;
    }

    public HanGuiBuilder(@NotNull HanGuiBuilder.Size size, @NotNull String guiTitle, @NotNull JavaPlugin pl) {

        random = new Random();
        this.size = size;
        this.guiTitle = guiTitle;
        this.players = new LinkedHashSet<>();
        this.itemHashMap = new HashMap<>();

        this.plugin = pl;


        dataList = new ArrayList<>();

    }

    public HanGuiBuilder(@NotNull HanGuiBuilder.Size size, @NotNull JavaPlugin pl) {

        random = new Random();
        this.size = size;
        this.players = new LinkedHashSet<>();
        this.itemHashMap = new HashMap<>();

        this.plugin = pl;

        dataList = new ArrayList<>();

    }



    public Optional<GuiData> getData(@NotNull String key) {

        for (GuiData guiData : dataList) {
            if (guiData.getKey().equals(key)) {
                return Optional.of(guiData);
            }
        }

        return Optional.empty();
    }



    public <T> boolean addData(@NotNull String key,@NotNull T t) {
        if (dataList.stream().anyMatch(guiData -> guiData.getT().equals(key))) {
            Bukkit.getLogger().info("d");
            return false;
        }

        dataList.add(new GuiData<T>(key, t));

        return true;
    }

    public <T> boolean replaceData(@NotNull String key,@NotNull T t) {
        if (dataList.stream().anyMatch(guiData -> guiData.getT().equals(key))) {
            removeData(key);
            addData(key, t);
            return true;
        }

        dataList.add(new GuiData<T>(key, t));

        return false;
    }


    public boolean removeData(@NotNull String key) {
        if (!getData(key).isPresent()) {
            return false;
        }
        dataList.remove(getData(key).get());
        return true;
    }

    public int getDataSıze() {
        return dataList.size();
    }

    public List<GuiData> getDataList() {
        return dataList;
    }

    public List getDataList(Class type) {
        return dataList.stream().filter(guiData -> guiData.getT().equals(type)).collect(Collectors.toList());

    }


    public final HanGuiBuilder setAccessibleOnDragItems(@NotNull boolean value) {
        accessibleOnDragItems = value;
        return this;
    }

    protected void loadGui() {

        Objects.nonNull(guiTitle);
        Objects.nonNull(this.plugin);

        id = this.random.nextInt(1000);
        inventory = Bukkit.createInventory(null,this.size.size, ChatColor.translateAlternateColorCodes('&', guiTitle));

        inventory.setMaxStackSize(id);

        Bukkit.getServer().getPluginManager().registerEvents(this, this.plugin);

    }

    public final HanGuiBuilder addItem(@NotNull int slot, @NotNull GuiItem item) {

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

    public final HanGuiBuilder addPlayer(@NotNull OfflinePlayer player) {
        players.add(player);
        return this;
    }

    public final HanGuiBuilder removePlayer(@NotNull OfflinePlayer player) {
        players.remove(player);
        return this;
    }
    public final void open() {
        loadGui();

        for (int slot : itemHashMap.keySet()) {
            if (slot > size.size) {
                continue;
            }

            GuiItem item = itemHashMap.get(slot);

            ItemStack itemStack = new ItemStack(item.getItem());

            inventory.setItem(slot, itemStack);
        }


        for (OfflinePlayer player : players) {
            if (player.isOnline()) {
                player.getPlayer().openInventory(this.inventory);
            }else {
                players.remove(player);
            }
        }
    }

    public final void update() {
        inventory.clear();
        for (int slot : itemHashMap.keySet()) {
            if (slot > size.size) {
                continue;
            }

            GuiItem item = itemHashMap.get(slot);

            ItemStack itemStack = new ItemStack(item.getItem());

            inventory.setItem(slot, itemStack);
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
    public void onClickEvent(@NotNull InventoryClickEvent event) {
        if (event.getInventory().getMaxStackSize() == id) {

            if (!this.accessibleOnDragItems) {
                event.setCancelled(true);
            }

            int slot = event.getSlot();
            if (itemHashMap.get(slot) != null) {
                GuiItem item = itemHashMap.get(slot);
                item.getAttraction().execute(((Player) event.getWhoClicked()), this, slot);
            }

        }
    }

    @EventHandler
    public void onCloseEvent(@NotNull InventoryCloseEvent event) {

        if (event.getInventory().getMaxStackSize() == id) {
            players.remove(event.getPlayer());

            if (players.size()<1) {
                HandlerList.unregisterAll(this);
            }

            if (closeAttraction!=null) {
                closeAttraction.run(((Player) event.getPlayer()), this);
            }
        }

    }
}
