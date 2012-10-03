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

public class MSTListenerFloatingLilyPads implements Listener {

	private MSTPlugin Plugin;

	public MSTListenerFloatingLilyPads(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

	// prevent hatches from breaking
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		if(event.isCancelled()) return;

		Block block = event.getBlock();
		
		if(block.getType() == Material.WATER_LILY)
			if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.FloatingLilyPads))
				event.setCancelled(true);
		
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

		if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.FloatingLilyPads))
		{
			Player player = event.getPlayer();
			
			BlockFace blockFace = event.getBlockFace();
			Material itemInHandMaterial = player.getItemInHand().getType();

	    	if(blockFace == BlockFace.UP && itemInHandMaterial == Material.WATER_LILY)
	    	{
	    		Block blockAbove = block.getRelative(BlockFace.UP);
	    		if(blockAbove.getType() == Material.AIR)
	    		{

	    			// return if they can't build here
	    			if(!Plugin.canBuild(player, blockAbove))
	    			{
	    				event.setCancelled(true);
	    				return;
	    			}

	    			blockAbove.setType(Material.WATER_LILY);
	    			Plugin.takeItemInHand(player);
	    		}
	    	}

		}
		
    }
	
}
