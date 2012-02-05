package redsgreens.ManySmallTweaks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class MSTListenerKeepSaddleOnPigDeath implements Listener {

	private MSTPlugin Plugin;

	public MSTListenerKeepSaddleOnPigDeath(MSTPlugin plugin)
	{
		Plugin = plugin;
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

}
