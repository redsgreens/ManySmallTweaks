package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;
import org.bukkit.DyeColor;

public class MSTEntityListener implements Listener {

	private MSTPlugin Plugin;
	public MSTEntityListener(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event)
    {
    	Entity entity = event.getEntity();
    	Location location = entity.getLocation();
    	String worldName = location.getWorld().getName();
    	
    	if(Plugin.Config.isTweakEnabled(worldName, MSTName.ProjectileTriggers))
		{
	    	ArrayList<Block> blocks = Plugin.getCloseBlocks(location);
	    	Iterator<Block> itr = blocks.iterator();
	    	
	    	while(itr.hasNext())
	    	{
	    		Block block = itr.next();
	    		Material material = block.getType();
	    		
    	    	if(material == Material.STONE_BUTTON)
    	    	{
    	    		final BlockState bs = block.getState();
    	    		MaterialData md = bs.getData();
    	    		if(md instanceof Button)
    	    		{
    	    			final Button button = (Button)md;
    	    			button.setPowered(true);
    	    			bs.setData(button);
    	    			bs.update();
    	    			entity.remove();
    	    			
    	    			Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
    	    			    public void run() {
    	    			    	button.setPowered(false);
    	    			    	bs.setData(button);
    	    			    	bs.update();
    	    			    }
    	    			}, 15);
    	    		}

    	    	}
    	    	else if(material == Material.LEVER)
    	    	{
    	    		BlockState bs = block.getState();
    	    		MaterialData md = bs.getData();
    	    		if(md instanceof Lever)
    	    		{
    	    			Lever l = (Lever)md;
    	    			l.setPowered(!l.isPowered());
    	    			bs.setData(l);
    	    			bs.update();
    	    			entity.remove();
    	    		}

    	    	} 
	    	}
			
		}

    }
    
    private ArrayList<Pig> Pigs = new ArrayList<Pig>();
    private ArrayList<Location> BabyPigLocations = new ArrayList<Location>();
    
    private ArrayList<Sheep> Sheeps = new ArrayList<Sheep>();
    private Random rand = new Random();
    
    @EventHandler(priority = EventPriority.NORMAL)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.isCancelled()) return;
    	Entity entity = event.getEntity();

		if(entity instanceof Pig || entity instanceof Sheep) {
	    	String worldName = entity.getLocation().getWorld().getName();

    		if(entity instanceof Pig)
    		{
    			Pig pig = (Pig)entity;

    			if(Plugin.Config.isTweakEnabled(worldName, MSTName.PercentSaddledPigs))
    			{
	    			if(rand.nextInt(((Math.round(((Double)(100 / Plugin.Config.getNumericTweakValue(worldName, MSTName.PercentSaddledPigs))).intValue())))) == 0)
	    			{
		    			Pigs.add(pig);
		    			Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
		    			    public void run() {
		    			    	while(Pigs.size() > 0){
		    			    		Pig p = Pigs.get(0);
		    			    		p.setSaddle(true);
		    			    		Pigs.remove(0);
		    			    	}
		    			    }
		    			}, 0);
	    			}
    			}

    			if(Plugin.Config.isTweakEnabled(worldName, MSTName.PigsReproduceQuick))
    			{
    				// see if this is a baby pig
    				if(pig.getAge() == -24000)
    				{
    					final Location loc = pig.getLocation();

    					if(!BabyPigLocations.contains(loc))
    					{
	    					BabyPigLocations.add(loc);

	    					World world = loc.getWorld();
	    					Integer numSiblings = rand.nextInt(3);		    					

	    					for(int n=0; n<numSiblings; n++)
	    					{
	    						Pig bp = (Pig) world.spawnCreature(loc, CreatureType.PIG);
	    						bp.setAge(-24000);
	    					}

			    			Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
			    			    public void run() {
			    			    	BabyPigLocations.remove(loc);
			    			    }
			    			}, 5);
    					}
    				}
    			}
    		}
    		else if(entity instanceof Sheep && Plugin.Config.isTweakEnabled(worldName, MSTName.PercentColorSheep))
    		{
    			if(rand.nextInt(((Math.round(((Double)(100 / Plugin.Config.getNumericTweakValue(worldName, MSTName.PercentColorSheep))).intValue())))) == 0)
    			{
    				Sheep sheep = (Sheep)entity;
    				Sheeps.add(sheep);
	    			Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
	    			    public void run() {
	    			    	while(Sheeps.size() > 0){
	    			    		Sheep s = Sheeps.get(0);
	    			    		s.setColor(DyeColor.values()[(new Random()).nextInt(DyeColor.values().length)]);
	    			    		Sheeps.remove(0);
	    			    	}
	    			    }
	    			}, 0);
    				
    			}
    		}
	    		
		}
		
	}
	
    @EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event)
	{
    	Entity entity = event.getEntity();
    	
    	if(entity instanceof Pig)
    	{
    		Pig pig = (Pig)entity;
    		if(pig.hasSaddle())
    		{
        		Location location = entity.getLocation();
        		World world = location.getWorld();
    	    	String worldName = world.getName();

	    		if(Plugin.Config.isTweakEnabled(worldName, MSTName.KeepSaddleOnPigDeath))
	    		{
	    			ItemStack saddle = new ItemStack(Material.SADDLE);
	    			saddle.setAmount(1);
	    			world.dropItemNaturally(location, saddle);
	    		}
    		}
    	}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPaintingBreak(PaintingBreakEvent event)
	{
		if(event.getCause() != RemoveCause.PHYSICS || event.isCancelled()) return;
		if(Plugin.Config.isTweakEnabled(event.getPainting().getWorld().getName(), MSTName.FloatingPaintings))
			event.setCancelled(true);
	}

}
