package me.emprzedd.artifactframework.items;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.*;

public class FlameOfCreation extends ArtifactItem implements Listener {
    public FlameOfCreation(String rawName, Material type, String lore) {
        super(rawName,rawName, type, lore);
    }
    public FlameOfCreation(){
        this(Rarity.formatText(Rarity.RARE,"Flame Of Creation"), Material.BLAZE_POWDER, "Mortar of the Universe");
    }

    private int cooldownSeconds = 60 * 5;

    private double speedMultiplier = 4;

    private long liveSeconds = 60;

    private long summonTime = -cooldownSeconds;

    // Probably can replace vehicleId with horseReference
    private UUID vehicleId;
    private Horse horseReference;

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
        team = board.getTeam("redGlow") == null ? board.registerNewTeam("redGlow") : board.getTeam("redGlow");
        team.setColor(ChatColor.WHITE);
    }

    @Override
    protected void reloadConfig() {
        cooldownSeconds = getConfig().getInt("FlameOfCreation.cooldownSeconds");
        speedMultiplier = getConfig().getDouble("FlameOfCreation.speedMultiplier");
        liveSeconds = getConfig().getLong("FlameOfCreation.durationSeconds");
    }

    // Jank
    @EventHandler
    public void summon(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        if(item!= null && !item.getType().isAir() && this.isSelectedArtifact(item)){

            // Infinite flint and steel.
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
                if(e.getClickedBlock() == null || !e.getClickedBlock().getRelative(BlockFace.UP).getType().isAir()){
                    screamAtPlayer(e.getPlayer(),"Invalid Spot.");
                    return;
                }
                e.getClickedBlock().getRelative(BlockFace.UP).setType(Material.FIRE); // Might destroy top block. Also works around towny
            }
            // Donkey Summon
            else if(e.getAction() == Action.LEFT_CLICK_BLOCK){

                // Verify proper spot
                Block summonPoint = e.getClickedBlock().getWorld().getHighestBlockAt(e.getClickedBlock().getLocation());

                if(e.getClickedBlock() == null || !summonPoint.getRelative(BlockFace.UP).getType().isAir()){
                    screamAtPlayer(e.getPlayer(),"Invalid Spot.");
                    return;
                }

                long timeRemaining = summonTime + (cooldownSeconds*1000L) - System.currentTimeMillis();
                if(timeRemaining > 0){
                    screamAtPlayer(e.getPlayer(),"Creation will be ready in : " + timeRemaining/1000 + "s.");
                    return;
                }


                // Spot is Valid, begin summon.
                summonPoint.getRelative(BlockFace.UP).setType(Material.FIRE); // Might destroy top block. Also works around towny
                summonPoint.getLocation().getWorld().strikeLightning(summonPoint.getLocation());

                Horse avatar = (Horse) e.getClickedBlock().getLocation().getWorld().spawnEntity(
                        summonPoint.getRelative(BlockFace.UP,16).getLocation(),
                        EntityType.HORSE
                );

                horseReference = avatar;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        killHorse(true);
                    }
                }.runTaskLater(this.getPlugin(), 20L * liveSeconds /*<-- the delay */);

                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,20*20,2,false,false));
                avatar.setVelocity(new Vector(avatar.getLocation().getDirection().multiply(speedMultiplier).getX(), speedMultiplier, avatar.getLocation().getDirection().multiply(speedMultiplier).getZ()));
                avatar.getWorld().setThundering(false);
                vehicleId = avatar.getUniqueId();

                //avatar.setGliding(true);
                avatar.setStyle(Horse.Style.NONE);
                avatar.setColor(Horse.Color.WHITE);
                avatar.setGlowing(true);
                team.addEntry(avatar.getUniqueId().toString());

                avatar.setRemoveWhenFarAway(false);
                avatar.setPersistent(true);

                //avatar.setFireTicks(Integer.MAX_VALUE);
                avatar.setVisualFire(true);

                avatar.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,10,false,false));
                avatar.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,Integer.MAX_VALUE,2,false,false));
                //avatar.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,30,3,false,false));
                avatar.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,3,false,false));
                avatar.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,1,false,false));
                avatar.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,Integer.MAX_VALUE,1,false,false));

                avatar.setAdult();
                avatar.setMaxHealth(50);
                avatar.setFallDistance(-64);
                avatar.setJumpStrength(2);
                avatar.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.27);
                avatar.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                avatar.setEatingHaystack(true);
                avatar.setLoveModeTicks(Integer.MAX_VALUE);
                avatar.setOwner(e.getPlayer());

                //avatar.setCollidable(false);
                avatar.setAI(true);

                avatar.setTarget(e.getPlayer());

                avatar.setCustomNameVisible(true);
                avatar.setCustomName(ChatColor.translateAlternateColorCodes('&',"&6&lAvatar of Solaris Erses"));

                avatar.setPassenger(e.getPlayer());

                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,20*20,10,false,false));
                for (Entity ent : avatar.getNearbyEntities(12,64,12)) {
                    if(ent instanceof Player){
                        Player player = (Player)ent;
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&oThe warmth of the flame inspires you."));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,20*3,10,false,false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*5,1,false,false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,20*5,2,false,false));
                        //player.setFireTicks(20*20);
                        player.playSound(avatar.getLocation(),Sound.ENTITY_HORSE_ANGRY,5,0.75f);

                    }
                    else if(ent instanceof Monster){
                        ent.setFireTicks(20*10);
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

        e.getEntity().setFireTicks(20*5);
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e){

        if(e.getPlayer().isInsideVehicle() && e.getPlayer().getVehicle() instanceof Horse){

            Horse avatar = (Horse)e.getPlayer().getVehicle();
            if(avatar.getUniqueId() == vehicleId){
                for (Entity ent : avatar.getNearbyEntities(16,2,16)) {
                    if(ent instanceof Vehicle && ent instanceof LivingEntity){
                        // Apply slowfall effect
                        ((LivingEntity)ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,20*10,20,false,false));
                        ((LivingEntity)ent).getPassengers().forEach(passenger ->{
                            if(passenger instanceof LivingEntity){
                                ((LivingEntity) passenger).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,20*10,20,false,false));
                            }
                        });
                    }
                    else if(!ent.equals(e.getPlayer()) && ent.getLocation().distance(avatar.getLocation()) < 2 && (ent instanceof Monster || ent instanceof Player)){
                        // Ram Effect
                        e.getPlayer().playSound(avatar.getLocation(),Sound.ENTITY_ENDER_DRAGON_SHOOT,0.75f,1f);
                        ((LivingEntity)ent).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,5,10,false,false));
                        ent.setFireTicks(30);
                        if(ent instanceof Player){
                            ((Player)ent).playSound(avatar.getLocation(),Sound.ENTITY_ENDER_DRAGON_SHOOT,0.75f,1f);
                        }
                    }
                }

                // Rider Effects
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,40,1,false,false));
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,40,1,false,false));
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,40,2,false,false));
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,40,2,false,false));
            }
        }
    }


    @EventHandler
    public void onHorseJump(HorseJumpEvent e){
        if(e.getEntity().getUniqueId() == vehicleId){
            Horse avatar = (Horse) e.getEntity();
            avatar.setFallDistance(-64);
            avatar.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,20*10,10,false,false));
            avatar.getPassengers().forEach( livingEntity -> {
                if(livingEntity instanceof LivingEntity){
                    livingEntity.setFallDistance(-64);
                    ((LivingEntity) livingEntity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,20*10,2,false,false));
                }
            });

        }
    }

    // Drop all items and remove the horse.
    @Override
    public void onDisable(){
        killHorse(false);
        this.getPlugin().getLogger().info("Avatar Horse removed!");
    }

    private void killHorse(boolean playEffects){
        if(horseReference != null) {
            if(playEffects){
                horseReference.getWorld().strikeLightning(horseReference.getLocation());
                horseReference.getNearbyEntities(8,8,8).forEach(entity -> {
                    if(entity instanceof Player){
                        this.screamAtPlayer((Player)entity,"Returning to Aetherium...");
                    }
                });
            }

            horseReference.getInventory().forEach(item ->{
                if (item != null) {
                    horseReference.getWorld().dropItem(horseReference.getLocation(),item);
                }
            });
            horseReference.setHealth(0);
            horseReference.remove();
        }
        else{
            this.getPlugin().getLogger().warning("Attempting to kill a null horse. The only way this is allowed to happen is if there is no Avatar Horse summon on a server close.");
        }
    }
}
