package redsgreens.ManySmallTweaks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PoweredRail;

public class MSTListenerFloatingRails implements Listener {

	private MSTPlugin Plugin;

	Set<Material> AllowedRailMaterials = new HashSet<Material>(Arrays.asList(Material.AIR, Material.GLASS, Material.GLOWSTONE, Material.THIN_GLASS, Material.IRON_FENCE, Material.PISTON_BASE, Material.PISTON_STICKY_BASE, Material.PISTON_EXTENSION, Material.PISTON_MOVING_PIECE, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.STEP, Material.COBBLESTONE_STAIRS, Material.BRICK_STAIRS, Material.WOOD_STAIRS, Material.SMOOTH_STAIRS, Material.NETHER_BRICK_STAIRS));

	public MSTListenerFloatingRails(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

	// prevent rails from breaking
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		if(event.isCancelled()) return;

		Block block = event.getBlock();
		if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.FloatingRails))
		{
			Material material = block.getType();
			
			if(event.getChangedType() == Material.SNOW && (material == Material.RAILS || material == Material.DETECTOR_RAIL || material == Material.POWERED_RAIL))
				event.setCancelled(true);
			else if(material == Material.RAILS || material == Material.DETECTOR_RAIL)
			{
				if(AllowedRailMaterials.contains(block.getRelative(BlockFace.DOWN).getType()))
						event.setCancelled(true);
			}
			else if(material == Material.POWERED_RAIL)
			{
System.out.println(block.getRelative(BlockFace.DOWN).getType());
				
				if(AllowedRailMaterials.contains(block.getRelative(BlockFace.DOWN).getType()))
				{
System.out.println("2");
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

		if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.FloatingRails))
		{
			Player player = event.getPlayer();
			
			// return if they can't build here
			if(!Plugin.canBuild(player, block)) return;

			Material blockMaterial = block.getType();
			BlockFace blockFace = event.getBlockFace();
			ItemStack itemInHand = player.getItemInHand();
			Material itemInHandMaterial = itemInHand.getType();

	    	// if they clicked a rail
	    	if(blockMaterial == Material.RAILS || blockMaterial == Material.DETECTOR_RAIL || blockMaterial == Material.POWERED_RAIL)
	    	{
	        	// return if they're not holding a rail
	        	if(itemInHandMaterial != Material.RAILS && itemInHandMaterial != Material.POWERED_RAIL && itemInHandMaterial != Material.DETECTOR_RAIL) return;

	        	// return if blockface is not a side
	        	if(blockFace != BlockFace.EAST && blockFace != BlockFace.WEST && blockFace != BlockFace.NORTH && blockFace != BlockFace.SOUTH) return;

	        	// place another rail in the adjacent block
	    		Block block2 = block.getRelative(blockFace);
	    		if(block2.getType() != Material.AIR) return;
	    		block2.setType(itemInHandMaterial);

	    		Plugin.takeItemInHand(player);
	    	}
	    	else if(AllowedRailMaterials.contains(blockMaterial) && blockFace == BlockFace.UP && (itemInHandMaterial == Material.RAILS || itemInHandMaterial == Material.POWERED_RAIL || itemInHandMaterial == Material.DETECTOR_RAIL))
	    	{
	    		// they tried to place a rail somewhere it normally won't go
	    		Block blockAbove = block.getRelative(BlockFace.UP);
	    		
	    		if(blockAbove.getType() == Material.AIR)
	    		{
	    			blockAbove.setType(itemInHandMaterial);
	    			Plugin.takeItemInHand(player);
	    		}
	    	}
		}
    }
    
}
