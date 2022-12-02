package me.emprzedd.artifactframework.items;

import jdk.jfr.Timespan;
import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.io.FileReader;
import java.sql.Time;
import java.util.*;

public class FlameOfCreation extends ArtifactItem implements Listener {
    public FlameOfCreation(String rawName, Material type, String lore) {
        super(rawName,rawName, type, lore);
    }
    public FlameOfCreation(){
        this(Rarity.formatText(Rarity.RARE,"Flame Of Creation"), Material.BLAZE_POWDER, "Mortar of the Universe");
    }

    private int cooldownSeconds = 60 * 5;

    private long summonTime = -cooldownSeconds;

    Team team;

    @Override
    protected void init() {
        canTrack = true;
        canPlaceInInventory = false;
        canDropItem = true;
        canPlaceInItemFrame = true;
        canPlace = false;
        canSmite = false;
        canRename = false;

        addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        if(board.getTeam("redGlow") == null){
            team = board.registerNewTeam("redGlow");
        }
        team.setColor(ChatColor.RED);
    }

    @Override
    protected void reloadConfig() {
        cooldownSeconds = getConfig().getInt("FlameOfCreation.cooldownSeconds");
    }

    @EventHandler
    public void summon(PlayerInteractEvent e){
        if(e.getItem()!= null && !e.getItem().getType().isAir() && this.isSelectedArtifact(e.getItem())){
            if(e.getClickedBlock() != null && e.getClickedBlock().getRelative(BlockFace.UP).getType().isAir()){
                screamAtPlayer(e.getPlayer(),"Invalid Spot.");
                return;
            }

            e.getClickedBlock().getRelative(BlockFace.UP).setType(Material.FIRE); // Might destroy top block. Also works around towny

            // Summon Donkey
            if(e.getAction() == Action.LEFT_CLICK_BLOCK){

                long timeRemaining = summonTime + (cooldownSeconds*1000L) - System.currentTimeMillis();
                if(timeRemaining > 0){
                    screamAtPlayer(e.getPlayer(),"A Creation will be ready in : " + timeRemaining/1000 + "s.");
                    return;
                }


                Donkey donkey = (Donkey) e.getClickedBlock().getLocation().getWorld().spawnEntity(
                        e.getClickedBlock().getRelative(BlockFace.UP).getLocation(),
                        EntityType.DONKEY
                );

                donkey.setFireTicks(Integer.MAX_VALUE);
                donkey.setRemoveWhenFarAway(false);
                donkey.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,10,false,false));
                donkey.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,Integer.MAX_VALUE,2,false,false));
                donkey.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,20*2,3,false,false));
                donkey.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,3,false,false));
                //PotionEffect glow = new PotionEffect(PotionEffectType.GLOWING,Integer.MAX_VALUE,10,false,false);
                //donkey.addPotionEffect(glow);


                team.addEntry(donkey.getUniqueId().toString());
                donkey.setGlowing(true);

                donkey.setAdult();
                donkey.setMaxHealth(50);
                donkey.setCollidable(false);
                donkey.setCustomNameVisible(true);
                donkey.setCustomName("Name");
                donkey.setAI(true);
                donkey.setJumpStrength(2);
                donkey.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                donkey.setEatingHaystack(true);
                donkey.setLoveModeTicks(Integer.MAX_VALUE);
                donkey.getLocation().getWorld().strikeLightning(donkey.getLocation());
                donkey.setFireTicks(Integer.MAX_VALUE);

                donkey.setOwner(e.getPlayer());

                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,20*20,10,false,false));
                for (Entity ent : donkey.getNearbyEntities(2,4,2)) {
                    if(ent instanceof Player){
                        ((Player)ent).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,20*3,10,false,false));
                        ((Player)ent).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*5,1,false,false));
                        ((Player)ent).addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,20*5,2,false,false));
                        ent.setFireTicks(20*20);
                    }
                }

                summonTime = System.currentTimeMillis();

            }

        }
    }

    @EventHandler
    public void burnAttacker(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof InventoryHolder))
            return;
        InventoryHolder holder = (InventoryHolder)e.getDamager();
        if(!super.hasInInventory(holder.getInventory()))
            return;

        e.getDamager().setFireTicks(20*4);
    }
}
