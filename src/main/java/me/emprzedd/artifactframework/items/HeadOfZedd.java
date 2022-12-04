package me.emprzedd.artifactframework.items;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class HeadOfZedd extends ArtifactItem {
    public HeadOfZedd(String rawName, Material type, String lore) {
        super(rawName,rawName, type, lore);
    }
    public HeadOfZedd(){
        this(Rarity.formatText(Rarity.DIVINE,"Head of Zedd"), Material.PLAYER_HEAD, "Head of the Mad God.\nEquipping this will give you schizophrenia in real life.");
    }

    @Override
    protected void init() {

        ItemMeta newMeta = this.getItemMeta();
        newMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        SkullMeta skull = (SkullMeta) newMeta;
        String zeddID = "56b0d6ac-9d55-4c3e-abcf-86fb718e3345";
        skull.setOwnerProfile(Bukkit.getOfflinePlayer(UUID.fromString(zeddID)).getPlayerProfile());

        newMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(),"asss",0.04, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        newMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(),"asss",-20.0, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HEAD));
        this.setItemMeta(newMeta);

        canTrack = true;
        canPlaceInInventory = false;
        canDropItem = true;
        canPlaceInItemFrame = true;
        canPlace = true;
        canSmite = false;
        canRename = false;
    }

    @Override
    protected void onReload() {

    }


}
