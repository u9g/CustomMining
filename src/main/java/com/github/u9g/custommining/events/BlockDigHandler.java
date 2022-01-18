package com.github.u9g.custommining.events;

import com.github.u9g.betterblockbreaking.events.PlayerDigBlockEvent;
import com.github.u9g.custommining.Constants;
import com.github.u9g.custommining.Main;
import com.github.u9g.u9gutils.NBTUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class BlockDigHandler implements Listener {
    private final Main plugin;
    private final Random rand;
    public BlockDigHandler(Main plugin) {
        this.plugin = plugin;
        this.rand = new Random();
    }
    @EventHandler
    public void onBlockDig (PlayerDigBlockEvent e) {
        if (!e.getPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999999, -1));
        }
        ItemStack hand = e.getPlayer().getEquipment().getItemInMainHand();
        if (!hand.getType().equals(Material.DIAMOND_PICKAXE)) return;
        var im = hand.getItemMeta();
        var speed = NBTUtil.getAsFloat(im, Constants.MINING_SPEED);
        if (speed.isPresent()) {
            e.setTickSize(speed.get());
        }
        // get 3x3 cube
        var frac = NBTUtil.getAsInt(im, Constants.FRACTURE_LEVEL);
        if (frac.isPresent() && frac.get() > 0) {
            int m = (int)Math.floor((frac.get()+1)/2.0);
            int r = Math.min(m, 3);
            for (int x = r*-1; x <= r; x++) {
                for (int y = r*-1; y <= r; y++) {
                    for (int z = r*-1; z <= r; z++) {
                        if (rand.nextDouble() > 0.5) {
                            Location loc = e.getLocation().clone().add(x,y,z);
                            Material type = e.getPlayer().getWorld().getBlockAt(loc).getType();
                            if (plugin.blockBreakManager.unbreakableBlocks.contains(type)) continue;
                            plugin.blockBreakManager.tickBlock(e.getPlayer(), loc, rand.nextFloat(0.1f, (0.1f*frac.get())+0.01f));
                        }
                    }
                }
            }
        }
    }
}