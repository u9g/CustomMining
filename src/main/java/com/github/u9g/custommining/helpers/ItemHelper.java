package com.github.u9g.custommining.helpers;

import com.github.u9g.custommining.Constants;
import com.github.u9g.u9gutils.ItemBuilder;
import com.github.u9g.u9gutils.NBTUtil;
import com.github.u9g.u9gutils.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemHelper {
    public static void remakeLore (ItemStack item) {
        var im = item.getItemMeta();
        var miningSpeed= NBTUtil.getAsFloat(im, Constants.MINING_SPEED);
        var fractureLevel= NBTUtil.getAsInt(im, Constants.FRACTURE_LEVEL);
        var veinminerLevel= NBTUtil.getAsInt(im, Constants.VEIN_MINER);
        if (fractureLevel.isPresent() && fractureLevel.get() == 0) im.getPersistentDataContainer().remove(Constants.FRACTURE_LEVEL);
        List<Component> comps = new ArrayList<>();
        if (miningSpeed.isPresent()) comps.add(Util.mm("Speed: <aqua>"+miningSpeed.get()));
        if (fractureLevel.isPresent()) {
            if (fractureLevel.get() == 0) im.getPersistentDataContainer().remove(Constants.FRACTURE_LEVEL);
            else {
                comps.add(Util.mm("Fracture: <green>"+fractureLevel.get()));
            }
        }
        if (veinminerLevel.isPresent()) {
            if (veinminerLevel.get() == 0) im.getPersistentDataContainer().remove(Constants.VEIN_MINER);
            else {
                comps.add(Util.mm("Vein Miner Blocks: <color:#ff36f5>"+formatInt(veinminerLevel.get())));
            }
        }
        var materialKeys = im.getPersistentDataContainer().getKeys().stream().filter(key -> key.namespace().equals("stats")).toList();
        if (materialKeys.size() > 0) {
            comps.add(Component.space());
            for (var key : materialKeys) {
                comps.add(Component.translatable(key.getKey()).append(Component.text(": " + formatInt(NBTUtil.getAsInt(im, key).get()), NamedTextColor.YELLOW)));
            }
        }
        item.setItemMeta(im);
        ItemBuilder.from(item).lore(comps);
        item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    private static String formatInt(int num) {
        return NumberFormat.getInstance().format(num);
    }
}
