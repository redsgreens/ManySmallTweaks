package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MSTListenerPigsReproduceQuick implements Listener {

	private MSTPlugin Plugin;

	ArrayList<Location> BabyPigLocations = new ArrayList<Location>();
	Random rand = new Random();
	
	public MSTListenerPigsReproduceQuick(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

    @EventHandler(priority = EventPriority.NORMAL)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.isCancelled()) return;
    	Entity entity = event.getEntity();
    	
    	String worldName = entity.getLocation().getWorld().getName();

		if(entity instanceof Pig)
		{
			Pig pig = (Pig)entity;

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
    						Pig bp = (Pig) world.spawnEntity(loc, EntityType.PIG);
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

    }
}
