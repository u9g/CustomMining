package com.github.u9g.custommining.events;

import com.github.u9g.betterblockbreaking.events.PlayerBreakBlockEvent;
import com.github.u9g.custommining.helpers.BlockHelper;
import com.github.u9g.custommining.Constants;
import com.github.u9g.custommining.helpers.ItemHelper;
import com.github.u9g.custommining.Main;
import com.github.u9g.u9gutils.NBTUtil;
import com.github.u9g.u9gutils.Util;
import com.google.common.base.Stopwatch;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockBreakHandler implements Listener {
    Main plugin;
    public BlockBreakHandler (Main plugin) {
        this.plugin = plugin;
    }
    private static final Set<Material> DONT_VEIN_MINER = new HashSet<>(List.of(
            Material.AIR,
            Material.WATER//,
//            Material.STONE,
//            Material.BLACKSTONE
    ));

    @EventHandler
    public void onBreak (PlayerBreakBlockEvent e) {
        // Since we don't register block break events ourselves, we can just manually add the broken block to the rollback manager
        plugin.rollbackManager.addBrokenBlock(e.getLocation().getBlock());
        ItemStack hand = e.getPlayer().getEquipment().getItemInMainHand();
        if (hand.getType().isEmpty()) return;
        Block block = e.getPlayer().getWorld().getBlockAt(e.getLocation());
        tickItemsBlockBreakCounter(hand, block.getType(), 1);
        var data = NBTUtil.getAsInt(hand.getItemMeta(), Constants.VEIN_MINER);
        boolean hasVM = data.isPresent() && data.get() > 0;
        if (hasVM && !DONT_VEIN_MINER.contains(block.getType())) {
            var blocks = BlockHelper.getConnectedBlocks(block, data.get());
            tickItemsBlockBreakCounter(hand, block.getType(), blocks.size());
            blocks.forEach(b -> b.setType(Material.AIR));
        }
    }

    private void tickItemsBlockBreakCounter(ItemStack hand, Material type, int tickSize) {
        var im = hand.getItemMeta();
        if (type.equals(Material.AIR)) return;
        var key = Constants.material2Key.get(type);
        var data = NBTUtil.getAsInt(im, key);
        int count = data.orElse(0)+tickSize;
        NBTUtil.set(im, key, count);
        hand.setItemMeta(im);
        ItemHelper.remakeLore(hand);
    }
}
