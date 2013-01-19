package redsgreens.ManySmallTweaks;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
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
import org.bukkit.inventory.ItemStack;

public class MSTListenerButtonsOnMoreBlocks implements Listener {

	class dPoint
	{
		public double r;
		public double s;
		
		dPoint(double sr, double ss)
		{
			r = sr;
			s = ss;
		}
	}
	
	private MSTPlugin Plugin;

	Set<Material> AllowedButtonMaterials = new HashSet<Material>(Arrays.asList(Material.PISTON_BASE, Material.PISTON_STICKY_BASE, Material.FURNACE, Material.COMMAND));

	public MSTListenerButtonsOnMoreBlocks(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		if(event.isCancelled()) return;

		Block block = event.getBlock();
		
		if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.ButtonsOnMoreBlocks))
		{
			Material material = block.getType();
			
			if(material == Material.STONE_BUTTON || material == Material.LEVER || material == Material.WOOD_BUTTON)
			{
				if(Plugin.getBlockBehindButton(block).getType() != Material.AIR)
					event.setCancelled(true);
			}
			else if(material == Material.WOOD_PLATE || material == Material.STONE_PLATE)
			{
				if(block.getRelative(BlockFace.DOWN).getType() != Material.AIR)
					event.setCancelled(true);
			}
		}
	}
	
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
    	// return if the event is cancelled
//    	if(event.isCancelled()) return;

    	Block block;
    	BlockFace blockFace;    	
    	Player player = event.getPlayer();
    	
		// return if the event is not a right-click-block action
		Action action = event.getAction();
		if(action == Action.RIGHT_CLICK_BLOCK)
		{
			block = event.getClickedBlock();
			blockFace = event.getBlockFace();
		}
		else if(action == Action.RIGHT_CLICK_AIR)
		{
			try
			{
				block = player.getTargetBlock(null, 5);
				blockFace = getClosestBlockFace(block, player);
				if(block == null)
					return;
				else if(block.getType() == Material.AIR)
					return;
				else if(block.getLocation().distance(player.getLocation()) > 4)
					return;
			}
			catch(Exception e)
			{
				return;
			}
		}
		else return;
		
		if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.ButtonsOnMoreBlocks))
		{
			
			Material blockMaterial = block.getType();
			// return if the block is not allowed
//			if(!Plugin.isTransparent(block) && !AllowedButtonMaterials.contains(blockMaterial)) return;
			if(!block.getType().isTransparent() && !AllowedButtonMaterials.contains(blockMaterial)) return;

			if(blockMaterial == Material.STONE_BUTTON || blockMaterial == Material.LEVER || blockMaterial == Material.WOOD_BUTTON) return;
			
			ItemStack itemInHand = player.getItemInHand();
			Material itemInHandMaterial = itemInHand.getType();

			// return if they don't have a button or lever in hand
			if(itemInHandMaterial != Material.STONE_BUTTON && itemInHandMaterial != Material.LEVER && itemInHandMaterial != Material.WOOD_PLATE && itemInHandMaterial != Material.STONE_PLATE && itemInHandMaterial != Material.WOOD_BUTTON) return;

			// return if they can't build here
			if(!Plugin.canBuild(player, block))
			{
				event.setCancelled(true);
				return;
			}

			// determine which face was clicked and attach the corresponding button
			Block button;
			switch(blockFace)
			{
			case NORTH: 
				button = block.getRelative(BlockFace.NORTH); 
				if(button.getType() == Material.AIR)
				{
					button.setType(itemInHandMaterial);
					button.setData((byte)4);
				}
				break;
			case SOUTH:
				button = block.getRelative(BlockFace.SOUTH); 
				if(button.getType() == Material.AIR)
				{
					button.setType(itemInHandMaterial);
					button.setData((byte)3);
				}
				break;
			case WEST:
				button = block.getRelative(BlockFace.WEST); 
				if(button.getType() == Material.AIR)
				{
					button.setType(itemInHandMaterial);
					button.setData((byte)2);
				}
				break;
			case EAST: 
				button = block.getRelative(BlockFace.EAST); 
				if(button.getType() == Material.AIR)
				{
					button.setType(itemInHandMaterial);
					button.setData((byte)1);
				}
				break;
			case UP:
				if(itemInHandMaterial == Material.STONE_BUTTON || itemInHandMaterial == Material.WOOD_BUTTON) return; // buttons can't go on the top
				button = block.getRelative(BlockFace.UP); 
				if(button.getType() == Material.AIR)
				{
					button.setType(itemInHandMaterial);
					if(itemInHandMaterial == Material.LEVER)
						button.setData((byte)5);
				}
				break;
			case DOWN:
				if(itemInHandMaterial == Material.STONE_BUTTON || itemInHandMaterial == Material.WOOD_BUTTON) return; // buttons can't go on the bottom
				button = block.getRelative(BlockFace.DOWN); 
				if(button.getType() == Material.AIR)
				{
					button.setType(itemInHandMaterial);
					if(itemInHandMaterial == Material.LEVER)
						button.setData((byte)7);
				}
				break;
			default: // bottom was clicked, do nothing
				return;			
			}
			Plugin.takeItemInHand(player);

			event.setCancelled(true);
			
		}
		
    }
    
    BlockFace getClosestBlockFace(Block block, Player player)
    {
/*
    	System.out.println("Block.location=" + block.getLocation());
    	System.out.println("Player.location=" + player.getLocation());
    	System.out.println();
*/    	

    	// find closest blockface in vertical plane
    	dPoint vPoint = new dPoint(player.getLocation().getX() - block.getX(), player.getLocation().getY() - block.getY());
    	if(Math.abs(vPoint.r) > Math.abs(vPoint.s))
    	{
    		vPoint.s = vPoint.s / Math.abs(vPoint.r);
    		vPoint.r = vPoint.r / Math.abs(vPoint.r);
    	}
    	else
    	{
    		vPoint.r = vPoint.r / Math.abs(vPoint.s);
    		vPoint.s = vPoint.s / Math.abs(vPoint.s);
    	}
    	
    	if(pointInTriangle(vPoint, new dPoint(-10,-10), new dPoint(0,0), new dPoint(10,-10)))
    		return BlockFace.DOWN;
    	else if(pointInTriangle(vPoint, new dPoint(-10,10), new dPoint(0,0), new dPoint(10,10)))
    		return BlockFace.UP;

    	
    	// find closest blockface in horizontal plane
    	dPoint hPoint = new dPoint(player.getLocation().getX() - block.getX(), player.getLocation().getZ() - block.getZ());
    	if(Math.abs(hPoint.r) > Math.abs(hPoint.s))
    	{
    		hPoint.s = hPoint.s / Math.abs(hPoint.r);
    		hPoint.r = hPoint.r / Math.abs(hPoint.r);
    	}
    	else
    	{
    		hPoint.r = hPoint.r / Math.abs(hPoint.s);
    		hPoint.s = hPoint.s / Math.abs(hPoint.s);
    	}
    	
    	if(pointInTriangle(hPoint, new dPoint(-10,10), new dPoint(0,0), new dPoint(-10,-10)))
    		return BlockFace.WEST;
    	else if(pointInTriangle(hPoint, new dPoint(-10,-10), new dPoint(0,0), new dPoint(10,-10)))
    		return BlockFace.NORTH;
    	else if(pointInTriangle(hPoint, new dPoint(10,10), new dPoint(0,0), new dPoint(10,-10)))
    		return BlockFace.EAST;
    	else
    		return BlockFace.SOUTH;
    }
    
    double sign(dPoint p1, dPoint p2, dPoint p3)
    {
      return (p1.r - p3.r) * (p2.s - p3.s) - (p2.r - p3.r) * (p1.s - p3.s);
    }
    
    Boolean pointInTriangle(dPoint pt, dPoint v1, dPoint v2, dPoint v3)
    {
      Boolean b1, b2, b3;

      b1 = sign(pt, v1, v2) < 0.0d;
      b2 = sign(pt, v2, v3) < 0.0d;
      b3 = sign(pt, v3, v1) < 0.0d;

      return ((b1 == b2) && (b2 == b3));
    }

}
