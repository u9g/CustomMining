package com.github.u9g.custommining.events;

import com.github.u9g.safereset.RollbackEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RollbackHandler implements Listener {
    @EventHandler
    private void onRollback (RollbackEvent e) {
        Bukkit.broadcast(Component.text("Doing rollback!"));
    }
}
