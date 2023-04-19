What is this project?
HanGuiBuilder is a Minecraft Spigot plugin API that is designed for developers. It simplifies the process of building GUIs and assigning commands to items within them, and is compatible with all versions of Minecraft.

```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

	<dependency>
	    <groupId>com.github.BingulHan</groupId>
	    <artifactId>HanGuiBuilder</artifactId>
	    <version>1.1</version>
	</dependency>
```
Example:
```

        ItemStack information = new ItemStack(Material.PAPER);
        ItemMeta meta = information.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW+"Info");
        information.setItemMeta(meta);

        HanGuiBuilder guiBuilder = new HanGuiBuilder(HanGuiBuilder.Size.THREE, this).
                setAccessibleOnDragItems(false).
                addPlayer(event.getPlayer()).
                addItem(1, new GuiItem(new ItemStack(Material.GRASS), (p,g,slot) -> {

                    p.sendMessage(ChatColor.GREEN+"You touched the grass!");
                    g.getPlugin().getServer().broadcastMessage(""+p.getName()+" touched the grass!");

                })).
                addItem(2, new GuiItem(information, (p,g,slot) -> {

                    if (g.getData("information").isPresent()) {
                        p.sendMessage(ChatColor.WHITE+g.getData("information").get().getT().toString());
                    }


                }));

        guiBuilder.addData("title", "&a&lEXAMPLE GUI");
        guiBuilder.addData("information", "You can add unlimited items, unlimited players to the GUI. You can also add custom data to the gui. (Any kind of data can be added.)");

        if (guiBuilder.getData("title").isPresent()) {
            guiBuilder.setGuiTitle(String.valueOf(guiBuilder.getData("title").get().getT()));
        }

        guiBuilder.open();
        
```
