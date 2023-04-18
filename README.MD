What is this project?
HanGuiBuilder is a Minecraft Spigot plugin API that is designed for developers. It simplifies the process of building GUIs and assigning commands to items within them, and is compatible with all versions of Minecraft.

Example:
```
new HanGuiBuilder(HanGuiBuilder.Size.THREE, "&b&lExample Gui", this).setAccessibleOnDragItems(false).addPlayer(event.getPlayer()).
                addItem(1, new GuiItem(new ItemStack(Material.GRASS), (p,g) -> {

                    p.sendMessage(ChatColor.GREEN+"You touched the grass!");
                    g.getPlugin().getServer().broadcastMessage(""+p.getName()+" touched the grass!");
                    
                    })).open();
```