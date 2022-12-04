package me.emprzedd.artifactframework.items.RednaBreads;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlackBread extends ArtifactItem {
    public BlackBread(String rawName, Material type, String lore) {
        super(rawName, rawName, type, lore);
    }

    public BlackBread() {
        this(Rarity.formatText(Rarity.COMMON, "Black Bread"), Material.BREAD, "Preserved only for the evilest of evils.");
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
            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 60, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20 * 60, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 60, 0));
        }
    }
}