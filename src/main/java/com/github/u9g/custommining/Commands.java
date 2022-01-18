package com.github.u9g.custommining;

import com.github.u9g.custommining.helpers.ItemHelper;
import com.github.u9g.u9gutils.ItemBuilder;
import com.github.u9g.u9gutils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.commandmanager.CommandHook;

public record Commands (Main plugin) {
    @CommandHook("givepick")
    public void onPick (final Player p, final float miningSpeed, final int fractureLevel, final int veinMinerLevel) {
        ItemStack is = ItemBuilder
                .of(Material.DIAMOND_PICKAXE)
                .set(Constants.MINING_SPEED, miningSpeed)
                .set(Constants.FRACTURE_LEVEL, fractureLevel)
                .set(Constants.VEIN_MINER, veinMinerLevel)
                .build();
        ItemHelper.remakeLore(is);
        p.getInventory().addItem(is);
    }

    @CommandHook("fly")
    public void onFly (final Player p) {
        p.setAllowFlight(!p.getAllowFlight());
        p.setFlying(p.getAllowFlight());
    }
}
