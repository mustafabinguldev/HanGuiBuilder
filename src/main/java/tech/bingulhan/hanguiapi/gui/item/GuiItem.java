package tech.bingulhan.hanguiapi.gui.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import tech.bingulhan.hanguiapi.gui.ClickAttraction;

/**
 * @author BingulHan
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class GuiItem {

    private ItemStack item;
    private ClickAttraction attraction;

}
