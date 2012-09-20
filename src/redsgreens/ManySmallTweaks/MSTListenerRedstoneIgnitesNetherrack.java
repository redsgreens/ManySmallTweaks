package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class MSTListenerRedstoneIgnitesNetherrack implements Listener {

	private MSTPlugin Plugin;

	ArrayList<Block> MovingBlocks = new ArrayList<Block>();

	public MSTListenerRedstoneIgnitesNetherrack(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockRedstoneChange(BlockRedstoneEvent event)
	{
		Block block = event.getBlock();
		Material material = block.getType();
		String worldName = block.getWorld().getName();
		
		if(material == Material.NETHERRACK)
			if(Plugin.Config.isTweakEnabled(worldName, MSTName.RedstoneIgnitesNetherrack))
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
	}
	
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
