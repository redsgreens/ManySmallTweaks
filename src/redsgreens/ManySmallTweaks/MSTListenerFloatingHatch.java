package redsgreens.ManySmallTweaks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MSTListenerFloatingHatch implements Listener {

	private MSTPlugin Plugin;

	public MSTListenerFloatingHatch(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

	// prevent hatches from breaking
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		if(event.isCancelled()) return;

		Block block = event.getBlock();
		
		if(block.getType() == Material.TRAP_DOOR)
			if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.FloatingHatch))
			{
				Block blockBehind = getBlockBehindHatch(block); 

				if(blockBehind != null)
					if(Plugin.isTransparent(blockBehind))
						event.setCancelled(true);
			}
		
	}
	
	// allow a hatch to be placed above a ladder easily
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
    	// return if the event is cancelled
    	if(event.isCancelled()) return;

		// return if the event is not a right-click-block action
		Action action = event.getAction();
		if(action != Action.RIGHT_CLICK_BLOCK) return;

		Block block = event.getClickedBlock();

		if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.FloatingHatch))
		{
			Player player = event.getPlayer();
			
			Material itemInHandMaterial = player.getItemInHand().getType();

	    	// return if item in hand is not a hatch
	    	if(itemInHandMaterial != Material.TRAP_DOOR) return;

			Material blockMaterial = block.getType();
			BlockFace blockFace = event.getBlockFace();

			Block b;
	    	if(blockMaterial == Material.LADDER)
	    		b = block;
	    	else
	    		b = block.getRelative(blockFace);
	    	
	    	if(b.getType() != Material.LADDER) return;

	    	Block blockAbove = b.getRelative(BlockFace.UP);
	    	
	    	// return if block above ladder is not air
	    	if(blockAbove.getType() != Material.AIR) return;

			// return if they can't build here
			if(!Plugin.canBuild(player, block)) 
			{
				event.setCancelled(true);
				return;
			}

	    	blockAbove.setType(Material.TRAP_DOOR);
	    	
	    	Byte newData = 0;
	    	switch(b.getData())
	    	{
	    	case 2: 
	    		newData = 1; 
	    		break;
	    		
	    	case 3:
	    		newData = 0; 
	    		break;
	    		
	    	case 4: 
	    		newData = 3;
	    		break;
	    		
	    	case 5:
	    		newData = 2; 
	    		break;
	    		
	    	}
	    	blockAbove.setData(newData);
	    	
	    	Plugin.takeItemInHand(player);

		}
    }
    
    Block getBlockBehindHatch(Block block)
    {
    	
    	switch(block.getData())
    	{
    		case 4: 
    		case 0: return block.getRelative(BlockFace.WEST);
    		
    		case 5:
    		case 1: return block.getRelative(BlockFace.EAST);
    		
    		case 6:
    		case 2: return block.getRelative(BlockFace.SOUTH);
    		
    		case 7:
    		case 3: return block.getRelative(BlockFace.NORTH);
    		default: 
//    			System.out.println(block.getData());
//   			System.out.println("NORTH:" + block.getRelative(BlockFace.NORTH).getType());
//    			System.out.println("SOUTH:" + block.getRelative(BlockFace.SOUTH).getType());
//    			System.out.println("EAST:" + block.getRelative(BlockFace.EAST).getType());
//    			System.out.println("WEST:" + block.getRelative(BlockFace.WEST).getType());
    			return null;
    	}
    	
    }
}
