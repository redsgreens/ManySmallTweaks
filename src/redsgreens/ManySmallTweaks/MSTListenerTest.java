package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.server.EntityTNTPrimed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class MSTListenerTest implements Listener {

	private MSTPlugin Plugin;

	public MSTListenerTest(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		Block block = event.getBlock();
		if(block.getType() == Material.WATER_LILY)
		{
			event.setCancelled(true);
			System.out.println("WATER_LILY, Cancelled=" + event.isCancelled());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityShootBow(EntityShootBowEvent event)
	{
		final Entity e = event.getProjectile();
		e.setFireTicks(100);

	}
	
	@EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event)
	{
	     Entity entity = event.getEntity();
	     
//	     if(entity.getFireTicks() == 0) return;
System.out.println("Arrow: " + entity.getLocation());	     
	     Location location = entity.getLocation();
	     String worldName = location.getWorld().getName();
	    
	     if(Plugin.Config.isTweakEnabled(worldName, MSTName.ProjectileTriggers))
	     {
	    	 ArrayList<Block> blocks = getCloseBlocks(location);
	    	 Iterator<Block> itr = blocks.iterator();
System.out.println(blocks.size());
	    	 while(itr.hasNext())
	    	 {
		    	 Block block = itr.next();
System.out.println(block.getType());
		    	 if(block.getType() == Material.TNT)
		    	 {
		    		 CraftWorld world = (CraftWorld)block.getWorld();
		    		 EntityTNTPrimed tnt = new EntityTNTPrimed(world.getHandle(), block.getX() + 0.5D, block.getY() + 0.5D, block.getZ() + 0.5D);
		    		 world.getHandle().addEntity(tnt);
		    		 block.setType(Material.AIR);
		    	 }


	    	 }
	     }
	}
	
	ArrayList<Block> getCloseBlocks(Location loc)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		World world = loc.getWorld();
		ArrayList<Location> locations = new ArrayList<Location>();
		
		locations.add(new Location(world, ((Double)loc.getX()).intValue(), ((Double)loc.getY()).intValue(), ((Double)loc.getZ()).intValue()));
/*		
		locations.add(loc.clone().add(-0.25, -0.25, -0.25));
		locations.add(loc.clone().add(-0.25, -0.25, 0));
		locations.add(loc.clone().add(-0.25, -0.25, 0.25));
		locations.add(loc.clone().add(-0.25, 0, -0.25));
		locations.add(loc.clone().add(-0.25, 0, 0));
		locations.add(loc.clone().add(-0.25, 0, 0.25));
		locations.add(loc.clone().add(-0.25, 0.25, -0.25));
		locations.add(loc.clone().add(-0.25, 0.25, 0));
		locations.add(loc.clone().add(-0.25, 0.25, 0.25));
		locations.add(loc.clone().add(0, -0.25, -0.25));
		locations.add(loc.clone().add(0, -0.25, 0));
		locations.add(loc.clone().add(0, -0.25, 0.25));
		locations.add(loc.clone().add(0, 0, -0.25));
		locations.add(loc.clone().add(0, 0, 0));
		locations.add(loc.clone().add(0, 0, 0.25));
		locations.add(loc.clone().add(0, 0.25, -0.25));
		locations.add(loc.clone().add(0, 0.25, 0));
		locations.add(loc.clone().add(0, 0.25, 0.25));
		locations.add(loc.clone().add(0.25, -0.25, -0.25));
		locations.add(loc.clone().add(0.25, -0.25, 0));
		locations.add(loc.clone().add(0.25, -0.25, 0.25));
		locations.add(loc.clone().add(0.25, 0, -0.25));
		locations.add(loc.clone().add(0.25, 0, 0));
		locations.add(loc.clone().add(0.25, 0, 0.25));
		locations.add(loc.clone().add(0.25, 0.25, -0.25));
		locations.add(loc.clone().add(0.25, 0.25, 0));
		locations.add(loc.clone().add(0.25, 0.25, 0.25));
*/

		Iterator<Location> itr = locations.iterator();
		while(itr.hasNext())
		{
			Location l = itr.next();
System.out.println("Loc: " + l);
			Block block = world.getBlockAt(l);
			if(!blocks.contains(block))
				blocks.add(block);
		}
		
		return blocks;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Block block = event.getBlock();
		
		if(block.getType() == Material.TNT)
			System.out.println("TNT: " + block.getLocation());
	}
}
