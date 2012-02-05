package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MSTListenerPercentSaddledPigs implements Listener {

	private MSTPlugin Plugin;

	ArrayList<Pig> Pigs = new ArrayList<Pig>();
	Random rand = new Random();
	
	public MSTListenerPercentSaddledPigs(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

    @EventHandler(priority = EventPriority.NORMAL)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.isCancelled()) return;
    	Entity entity = event.getEntity();
    	String worldName = event.getLocation().getWorld().getName();

    	if(entity instanceof Pig && Plugin.Config.getNumericTweakValue(worldName, MSTName.PercentSaddledPigs) > 0)
    	{
			if(rand.nextInt(((Math.round(((Double)(100 / Plugin.Config.getNumericTweakValue(worldName, MSTName.PercentSaddledPigs))).intValue())))) == 0)
			{
    			Pigs.add((Pig)entity);
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
    	
    }
}
