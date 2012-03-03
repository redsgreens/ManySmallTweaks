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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MSTListenerTest implements Listener {

	private MSTPlugin Plugin;

	public MSTListenerTest(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
    	// return if the event is cancelled
    	if(event.isCancelled()) return;

		// return if the event is not a right-click-block action
		Action action = event.getAction();
		if(action != Action.RIGHT_CLICK_BLOCK) return;

		Block block = event.getClickedBlock();

		Player player = event.getPlayer();
		
		// return if they can't build here
		if(!Plugin.canBuild(player, block)) return;

		Material blockMaterial = block.getType();
//		ItemStack itemInHand = player.getItemInHand();
//		Material itemInHandMaterial = itemInHand.getType();

		if(blockMaterial == Material.WOOD_PLATE)
		{
			Byte d = block.getData();

			if(d == 0)
				block.setData((byte)1);
			else if (d == 1)
				block.setData((byte)0);
           
		}
    }


    
	
//	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		Block block = event.getBlock();
		if(block.getType() == Material.WATER_LILY)
		{
			event.setCancelled(true);
			System.out.println("WATER_LILY, Cancelled=" + event.isCancelled());
		}
	}

//	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityShootBow(EntityShootBowEvent event)
	{
		final Entity e = event.getProjectile();
		e.setFireTicks(100);

	}
	
//	@EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event)
	{
	     final Entity entity = event.getEntity();
	     
//	     if(entity.getFireTicks() == 0) return;

	     final Location location = entity.getLocation();
	     String worldName = location.getWorld().getName();
	    
	     if(Plugin.Config.isTweakEnabled(worldName, MSTName.ProjectileTriggers))
	     {

		     Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
				    public void run() {

				    	 ArrayList<Block> blocks = getCloseBlocks(location);
				    	 Iterator<Block> itr = blocks.iterator();
				    	 while(itr.hasNext())
				    	 {
					    	 Block block = itr.next();
					    	 if(block.getType() == Material.TNT)
					    	 {
					    		 CraftWorld world = (CraftWorld)block.getWorld();
					    		 EntityTNTPrimed tnt = new EntityTNTPrimed(world.getHandle(), block.getX() + 0.5D, block.getY() + 0.5D, block.getZ() + 0.5D);
					    		 world.getHandle().addEntity(tnt);
					    		 block.setType(Material.AIR);
					    	 }
				    	 }

				    }
				}, 5);


	     }

	}
	
	ArrayList<Block> getCloseBlocks(Location loc)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		World world = loc.getWorld();
		ArrayList<Location> locations = new ArrayList<Location>();
		
		loc = new Location(world, StrictMath.round(loc.getX()), StrictMath.round(loc.getY()), StrictMath.round(loc.getZ()));
		
		locations.add(loc);
		locations.add(loc.clone().add(-1.0, -1.0, -1.0));
		locations.add(loc.clone().add(-1.0, -1.0, 0));
		locations.add(loc.clone().add(-1.0, -1.0, 1.0));
		locations.add(loc.clone().add(-1.0, 0, -1.0));
		locations.add(loc.clone().add(-1.0, 0, 0));
		locations.add(loc.clone().add(-1.0, 0, 1.0));
		locations.add(loc.clone().add(-1.0, 1.0, -1.0));
		locations.add(loc.clone().add(-1.0, 1.0, 0));
		locations.add(loc.clone().add(-1.0, 1.0, 1.0));
		locations.add(loc.clone().add(0, -1.0, -1.0));
		locations.add(loc.clone().add(0, -1.0, 0));
		locations.add(loc.clone().add(0, -1.0, 1.0));
		locations.add(loc.clone().add(0, 0, -1.0));
		locations.add(loc.clone().add(0, 0, 0));
		locations.add(loc.clone().add(0, 0, 1.0));
		locations.add(loc.clone().add(0, 1.0, -1.0));
		locations.add(loc.clone().add(0, 1.0, 0));
		locations.add(loc.clone().add(0, 1.0, 1.0));
		locations.add(loc.clone().add(1.0, -1.0, -1.0));
		locations.add(loc.clone().add(1.0, -1.0, 0));
		locations.add(loc.clone().add(1.0, -1.0, 1.0));
		locations.add(loc.clone().add(1.0, 0, -1.0));
		locations.add(loc.clone().add(1.0, 0, 0));
		locations.add(loc.clone().add(1.0, 0, 1.0));
		locations.add(loc.clone().add(1.0, 1.0, -1.0));
		locations.add(loc.clone().add(1.0, 1.0, 0));
		locations.add(loc.clone().add(1.0, 1.0, 1.0));


		Iterator<Location> itr = locations.iterator();
		while(itr.hasNext())
		{
			Location l = itr.next();
			Block block = world.getBlockAt(l);
			if(!blocks.contains(block))
			{
				blocks.add(block);
//System.out.println(block.getLocation());
			}
		}
		
		return blocks;
	}
	
//	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Block block = event.getBlock();
		
		if(block.getType() == Material.TNT)
			System.out.println("TNT: " + block.getLocation());
	}
}
