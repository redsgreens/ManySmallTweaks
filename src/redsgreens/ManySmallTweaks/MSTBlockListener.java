package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PoweredRail;

public class MSTBlockListener implements Listener {

	ArrayList<Block> DeletedLadders = new ArrayList<Block>();
	
	private MSTPlugin Plugin;
	public MSTBlockListener(MSTPlugin plugin)
	{
		Plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		if(event.isCancelled()) return;

		final Block block = event.getBlock();
		String worldName = block.getLocation().getWorld().getName();
		Material material = block.getType();

		switch(material)
		{
			case LADDER:
				if(Plugin.Config.isTweakEnabled(worldName, MSTName.FloatingLadders))
				{
					Block behindLadder = Plugin.getBlockBehindLadder(block);

					if(behindLadder != null && behindLadder.getType() == Material.AIR && !DeletedLadders.contains(behindLadder))
					{
						behindLadder.setType(Material.LADDER);
						Byte data = block.getData();
						
						if(data % 2 == 0)
							data++;
						else
							data--;

						behindLadder.setData(data);
					}
					
					event.setCancelled(true);
				}
				break;
				
			case TRAP_DOOR:
				if(Plugin.Config.isTweakEnabled(worldName, MSTName.FloatingHatch))
					event.setCancelled(true);
				break;
				
			case STONE_BUTTON:
			case LEVER:
				if(Plugin.Config.isTweakEnabled(worldName, MSTName.ButtonsOnMoreBlocks))
					if(Plugin.getBlockBehindButton(block).getType() != Material.AIR)
						event.setCancelled(true);
				break;

			case WOOD_PLATE:
			case STONE_PLATE:
				if(Plugin.Config.isTweakEnabled(worldName, MSTName.ButtonsOnMoreBlocks))
					if(block.getRelative(BlockFace.DOWN).getType() != Material.AIR)
						event.setCancelled(true);
				break;

			case RAILS:
			case DETECTOR_RAIL:
				if(Plugin.Config.isTweakEnabled(worldName, MSTName.FloatingRails))
					if(Plugin.AllowedRailMaterials.contains(block.getRelative(BlockFace.DOWN).getType()))
						event.setCancelled(true);
				break;

			case POWERED_RAIL:
				if(Plugin.Config.isTweakEnabled(worldName, MSTName.FloatingRails))
					if(Plugin.AllowedRailMaterials.contains(block.getRelative(BlockFace.DOWN).getType()))
					{
						event.setCancelled(true);

						BlockState bs = block.getState();
			    		MaterialData md = bs.getData();
			    		PoweredRail pr = (PoweredRail)md;

			    		if(block.getBlockPower() > 0)
							pr.setPowered(true);
			    		else
			    			pr.setPowered(false);
			    		
			    		bs.setData(pr); 
			    		bs.update();
					}	    		
				break;
		}

	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(event.isCancelled()) return;

		Block block = event.getBlock();
		String worldName = block.getLocation().getWorld().getName();

		if(Plugin.Config.isTweakEnabled(worldName, MSTName.FloatingLadders))
		{
			Material material = block.getType();
			
			if(material == Material.LADDER)
			{
				Block behindLadder = Plugin.getBlockBehindLadder(block);
				if(behindLadder.getType() == Material.LADDER)
				{
					DeletedLadders.add(behindLadder);
					behindLadder.setType(Material.AIR);
					DeletedLadders.remove(behindLadder);
				}
			}

		}

	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockRedstoneChange(BlockRedstoneEvent event)
	{
		Block block = event.getBlock();
		Material material = block.getType();
		String worldName = block.getWorld().getName();
		
		if(material == Material.NETHERRACK && Plugin.Config.isTweakEnabled(worldName, MSTName.RedstoneIgnitesNetherrack))
		{
			if(event.getNewCurrent() > 0)
			{
				// set block on fire
				Block blockAbove = block.getRelative(BlockFace.UP);
				if(blockAbove.getType() == Material.AIR)
					blockAbove.setType(Material.FIRE);
			}
			else
			{
				// extinguish block
				Block blockAbove = block.getRelative(BlockFace.UP);
				if(blockAbove.getType() == Material.FIRE)
					blockAbove.setType(Material.AIR);
			}
			
		}
		else if((material == Material.PUMPKIN || material == Material.JACK_O_LANTERN) && Plugin.Config.isTweakEnabled(worldName, MSTName.RedstoneIgnitesPumpkins))
		{
			Byte data = block.getData();
			if(event.getNewCurrent() > 0)
			{
				block.setType(Material.JACK_O_LANTERN);
				block.setData(data);
			}
			else
			{
				block.setType(Material.PUMPKIN);
				block.setData(data);
			}

		}
		
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled()) return;
		
		Block block = event.getBlock();
		Material material = block.getType();
		String worldName = block.getWorld().getName();

		if(material == Material.NETHERRACK && Plugin.Config.isTweakEnabled(worldName, MSTName.RedstoneIgnitesNetherrack))
		{
			if(block.getBlockPower() > 0)
			{
				// set block on fire
				Block blockAbove = block.getRelative(BlockFace.UP);
				if(blockAbove.getType() == Material.AIR)
					blockAbove.setType(Material.FIRE);
			}
		}
		else if(material == Material.PUMPKIN && Plugin.Config.isTweakEnabled(worldName, MSTName.RedstoneIgnitesPumpkins))
		{
			// replace with jackolantern
			if(block.getBlockPower() > 0)
			{
				Byte data = block.getData();
				block.setType(Material.JACK_O_LANTERN);
				block.setData(data);				
			}
			
		}
		
	}

	
	ArrayList<Block> MovingBlocks = new ArrayList<Block>();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		if(event.isCancelled()) return;

		String worldName = event.getBlock().getWorld().getName();
		
		if(Plugin.Config.isTweakEnabled(worldName, MSTName.RedstoneIgnitesNetherrack))
		{
			BlockFace direction = event.getDirection();
			
			Iterator<Block> itr = event.getBlocks().iterator();
			while(itr.hasNext())
				MovingBlocks.add(itr.next().getRelative(direction));
			
			Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
			    public void run() {
			    	checkNetherrack();
			    }
			}, 5);

		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPistonRetract(BlockPistonRetractEvent event)
	{
		if(event.isCancelled()) return;

		if(event.isSticky())
		{
			String worldName = event.getBlock().getWorld().getName();
			
			if(Plugin.Config.isTweakEnabled(worldName, MSTName.RedstoneIgnitesNetherrack))
			{
				MovingBlocks.add(event.getBlock().getRelative(event.getDirection()));
				
				Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
				    public void run() {
				    	checkNetherrack();
				    }
				}, 5);

			}
		}		
	}

	
	void checkNetherrack()
	{
		while(MovingBlocks.size() > 0)
		{
			Block block = MovingBlocks.get(0);
			if(block.getType() == Material.NETHERRACK && block.getBlockPower() > 0)
			{
				// set block on fire
				Block blockAbove = block.getRelative(BlockFace.UP);
				if(blockAbove.getType() == Material.AIR)
					blockAbove.setType(Material.FIRE);
			}
			else
			{
				// extinguish block
				Block blockAbove = block.getRelative(BlockFace.UP);
				if(blockAbove.getType() == Material.FIRE)
					blockAbove.setType(Material.AIR);
			}

			MovingBlocks.remove(0);
		}
	}
}
