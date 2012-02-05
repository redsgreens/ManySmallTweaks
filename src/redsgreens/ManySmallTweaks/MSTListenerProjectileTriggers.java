package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;


public class MSTListenerProjectileTriggers implements Listener {

	private MSTPlugin Plugin;

	public MSTListenerProjectileTriggers(MSTPlugin plugin)
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
}
