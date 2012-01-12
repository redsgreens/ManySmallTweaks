package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class MSTBlockListener extends BlockListener {

	ArrayList<Block> DeletedLadders = new ArrayList<Block>();
	
	private MSTPlugin Plugin;
	public MSTBlockListener(MSTPlugin plugin)
	{
		Plugin = plugin;
	}
	
	@Override
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		if(event.isCancelled()) return;

		final Block block = event.getBlock();
		String worldName = block.getLocation().getWorld().getName();

		if(Plugin.Config.Worlds.containsKey(worldName))
		{

			MSTConfigWorld config = Plugin.Config.Worlds.get(worldName);
			
			Material material = block.getType();

			switch(material)
			{
				case LADDER:
					if(config.FloatingLadders)
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
					if(config.FloatingHatch)
						event.setCancelled(true);
					break;
					
				case STONE_BUTTON:
				case LEVER:
					if(config.ButtonsOnMoreBlocks)
						if(Plugin.getBlockBehindButton(block).getType() != Material.AIR)
							event.setCancelled(true);
					break;
					
				case RAILS:
				case DETECTOR_RAIL:
					if(config.FloatingRails)
						if(block.getRelative(BlockFace.DOWN).getType() == Material.AIR)
							event.setCancelled(true);
					break;

			}
		
		}		
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(event.isCancelled()) return;

		Block block = event.getBlock();
		String worldName = block.getLocation().getWorld().getName();

		if(Plugin.Config.Worlds.containsKey(worldName))
		{
			MSTConfigWorld config = Plugin.Config.Worlds.get(worldName);
			
			if(config.FloatingLadders)
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
	}
	
	@Override
	public void onBlockRedstoneChange(BlockRedstoneEvent event)
	{
		Block block = event.getBlock();
		
		// return if its not netherrack
		if(block.getType() != Material.NETHERRACK) return;
		
		int newCurrent = event.getNewCurrent();
		
		if(newCurrent > 0)
		{
			// set block on fire
			Block blockAbove = block.getRelative(BlockFace.UP);
			if(blockAbove.getType() == Material.AIR)
				blockAbove.setType(Material.FIRE);
		}
		else if(newCurrent == 0)
		{
			// extinguish block
			Block blockAbove = block.getRelative(BlockFace.UP);
			if(blockAbove.getType() == Material.FIRE)
				blockAbove.setType(Material.AIR);
		}
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled()) return;
		
		Block block = event.getBlock();
		if(block.getType() != Material.NETHERRACK) return;

		
		if(block.getBlockPower() > 0)
		{
			// set block on fire
			Block blockAbove = block.getRelative(BlockFace.UP);
			if(blockAbove.getType() == Material.AIR)
				blockAbove.setType(Material.FIRE);
		}

	}

	
	ArrayList<Block> MovingBlocks = new ArrayList<Block>();
	
	@Override
	public void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		if(event.isCancelled()) return;

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

	@Override
	public void onBlockPistonRetract(BlockPistonRetractEvent event)
	{
		if(event.isCancelled()) return;

		if(event.isSticky())
		{
			MovingBlocks.add(event.getBlock().getRelative(event.getDirection()));
		
			Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
			    public void run() {
			    	checkNetherrack();
			    }
			}, 5);

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
