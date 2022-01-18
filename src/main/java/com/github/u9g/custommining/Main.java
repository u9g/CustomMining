package com.github.u9g.custommining;

import com.github.u9g.betterblockbreaking.BlockBreakManager;
import com.github.u9g.custommining.events.BlockBreakHandler;
import com.github.u9g.custommining.events.BlockDigHandler;
import com.github.u9g.custommining.events.RollbackHandler;
import com.github.u9g.safereset.RollbackManager;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.CommandParser;

public final class Main extends JavaPlugin {
    public BlockBreakManager blockBreakManager;
    public RollbackManager rollbackManager;
    private WorldGuardPlatform worldguard;
    private RegionManager regionManager;
    private World mineWorld;
    private static final String WORLD_NAME = "mining_village";
    @Override
    public void onEnable() {
        prepWorld(WORLD_NAME);
        rollbackManager = new RollbackManager(mineWorld);
        blockBreakManager = new BlockBreakManager(this, 2000);
        worldguard = WorldGuard.getInstance().getPlatform();
        regionManager = worldguard.getRegionContainer().get(worldguard.getMatcher().getWorldByName(WORLD_NAME));
        new CommandParser(this.getResource("commands.rdcml")).parse().register("cmds", new Commands(this));
        Bukkit.getPluginManager().registerEvents(new BlockDigHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new RollbackHandler(), this);
        Bukkit.getPluginManager().registerEvents(rollbackManager, this);
    }

    private void prepWorld(String worldName) {
        mineWorld = WorldCreator.name(worldName).createWorld();
        if (mineWorld == null) throw new Error("Can't make the mine world.");
        mineWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        mineWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        mineWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        mineWorld.setTime(6000);
    } // y = 63

    private void ensureRegion() {
        if (regionManager.getRegion("mine") != null) return;
        ProtectedRegion pr = new ProtectedCuboidRegion("mine", BlockVector3.at(-8, -64, -251), BlockVector3.at(89, 319, -164));
        // allow
        pr.setFlag(Flags.BUILD, StateFlag.State.ALLOW);
        pr.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
        pr.setFlag(Flags.BLOCK_PLACE, StateFlag.State.ALLOW);
        pr.setFlag(Flags.INTERACT, StateFlag.State.ALLOW);
        pr.setFlag(Flags.BLOCK_PLACE, StateFlag.State.ALLOW);
        pr.setFlag(Flags.DAMAGE_ANIMALS, StateFlag.State.ALLOW);
        pr.setFlag(Flags.PVP, StateFlag.State.ALLOW);
        pr.setFlag(Flags.ENDERPEARL, StateFlag.State.ALLOW);
        pr.setFlag(Flags.EXIT_VIA_TELEPORT, StateFlag.State.ALLOW);
        pr.setFlag(Flags.POTION_SPLASH, StateFlag.State.ALLOW);
        pr.setFlag(Flags.USE, StateFlag.State.DENY);
        // deny
        pr.setFlag(Flags.LAVA_FLOW, StateFlag.State.DENY);
        pr.setFlag(Flags.CHEST_ACCESS, StateFlag.State.DENY);
        pr.setFlag(Flags.EXIT, StateFlag.State.DENY);
        pr.setFlag(Flags.FALL_DAMAGE, StateFlag.State.DENY);
        pr.setFlag(Flags.TRAMPLE_BLOCKS, StateFlag.State.DENY);
        pr.setFlag(Flags.TNT, StateFlag.State.DENY);
        // misc
        pr.setFlag(Flags.MIN_FOOD, 1);
        regionManager.addRegion(pr);
    }

    @Override
    public void onDisable() {
        rollbackManager.rollback(); // rollback when shutting down
    }
}
