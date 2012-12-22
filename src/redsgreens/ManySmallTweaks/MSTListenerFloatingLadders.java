package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MSTListenerFloatingLadders implements Listener {

	private MSTPlugin Plugin;

	ArrayList<Block> DeletedLadders = new ArrayList<Block>();

	public MSTListenerFloatingLadders(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

	// prevent hatches from breaking
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		if(event.isCancelled()) return;

		Block block = event.getBlock();
		
		if(block.getType() != Material.LADDER) return;

		if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.FloatingLadders))
		{
			Block behindLadder = getBlockBehindLadder(block);

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
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
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
				Block behindLadder = getBlockBehindLadder(block);

				if(behindLadder.getType() == Material.LADDER)
				{
					// return if they can't build here
					if(!Plugin.canBuild(event.getPlayer(), block))
					{
						event.setCancelled(true);
						return;
					}
					
					DeletedLadders.add(behindLadder);
					behindLadder.setType(Material.AIR);
					DeletedLadders.remove(behindLadder);
				}
			}

		}

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

		if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.FloatingLadders))
		{
			Player player = event.getPlayer();
			
			Material blockMaterial = block.getType();
			BlockFace blockFace = event.getBlockFace();
			ItemStack itemInHand = player.getItemInHand();
			Material itemInHandMaterial = itemInHand.getType();

	    	// return if it's not a ladder
	    	if(blockMaterial != Material.LADDER) return;
	    	
	    	// return if they're not holding a ladder
	    	if(itemInHandMaterial != Material.LADDER) return;

	    	// return if blockface is neither up nor down
	    	if(blockFace != BlockFace.UP && blockFace != BlockFace.DOWN) return;

			// return if they can't build here
			if(!Plugin.canBuild(player, block))
			{
				event.setCancelled(true);
				return;
			}

	    	// place another ladder in the adjacent block
			Block block2 = block.getRelative(blockFace);
			if(block2.getType() != Material.AIR) return;
			Byte data = block.getData();
			block2.setType(Material.LADDER);
			block2.setData(data);
			
			Block behindLadder = getBlockBehindLadder(block2);
			if(behindLadder != null && behindLadder.getType() == Material.AIR)
			{
				behindLadder.setType(Material.LADDER);
				
				if(data % 2 == 0)
					data++;
				else
					data--;

				behindLadder.setData(data);
			}

			Plugin.takeItemInHand(player);
		}
    }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		if(event.isCancelled()) return;

		String worldName = event.getBlock().getWorld().getName();
		
		if(Plugin.Config.isTweakEnabled(worldName, MSTName.FloatingLadders))
		{
			Block nextBlock = event.getBlock().getRelative(event.getDirection(), event.getLength()+1);

			if(nextBlock.getType() == Material.LADDER)
			{
				Block behindLadder = getBlockBehindLadder(nextBlock);

				if(behindLadder != null && behindLadder.getType() == Material.LADDER)
					DeletedLadders.add(behindLadder);
					behindLadder.setType(Material.AIR);
					DeletedLadders.remove(behindLadder);

			}


		}
	}
	
	Block getBlockBehindLadder(Block ladder)
	{
		Integer data = ((Byte)ladder.getData()).intValue();

		switch(data)
		{
		case 2: 
			return ladder.getRelative(BlockFace.SOUTH);
			
		case 3: 
			return ladder.getRelative(BlockFace.NORTH);
			
		case 4: 
			return ladder.getRelative(BlockFace.EAST);

		case 5: 
			return ladder.getRelative(BlockFace.WEST);
		}

		return null;
	}
}
