package com.github.u9g.custommining;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {
    public static NamespacedKey MINING_SPEED = NamespacedKey.fromString("custommining:mining_speed");
    public static NamespacedKey VEIN_MINER = NamespacedKey.fromString("custommining:vein_miner");
    public static NamespacedKey FRACTURE_LEVEL = NamespacedKey.fromString("custommining:fracture_level");
    public static Map<Material, NamespacedKey> material2Key = new LinkedHashMap<>();

    static {
        for (var val : Material.values()) {
            material2Key.put(val, NamespacedKey.fromString("stats:"+val.translationKey()));
        }
    }
}
