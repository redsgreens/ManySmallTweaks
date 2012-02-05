package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MSTListenerPercentColorSheep implements Listener {

	private MSTPlugin Plugin;

	ArrayList<Sheep> Sheeps = new ArrayList<Sheep>();
	Random rand = new Random();
	
	public MSTListenerPercentColorSheep(MSTPlugin plugin)
	{
		Plugin = plugin;
	}


    @EventHandler(priority = EventPriority.NORMAL)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.isCancelled()) return;
    	Entity entity = event.getEntity();
    	String worldName = event.getLocation().getWorld().getName();

    	if(entity instanceof Sheep && Plugin.Config.getNumericTweakValue(worldName, MSTName.PercentColorSheep) > 0)
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
