package me.emprzedd.artifactframework.items.RednaBreads;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class BloodBread extends ArtifactItem {
    public BloodBread(String rawName, Material type, String lore) {
        super(rawName, rawName, type, lore);
    }

    public BloodBread() {
        this(Rarity.formatText(Rarity.COMMON, "Blood Bread"), Material.BREAD, "Eternal Fire.");
    }


    ///

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        canTrack = false;
        canPlaceInInventory = true;
        canDropItem = true;
        canPlaceInItemFrame = true;
    }


    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        if (this.isSelectedArtifact(e.getItem())) {
            Player p = e.getPlayer();
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 20, 0));
            p.setFireTicks(Integer.MAX_VALUE - 2);
        }
    }
}