package redsgreens.ManySmallTweaks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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

	private MSTPlugin Plugin;

	Set<Material> AllowedButtonMaterials = new HashSet<Material>(Arrays.asList(Material.TNT, Material.MOB_SPAWNER, Material.PISTON_BASE, Material.PISTON_STICKY_BASE, Material.PISTON_MOVING_PIECE, Material.SOIL, Material.WOODEN_DOOR, Material.WOOD_DOOR, Material.IRON_DOOR_BLOCK, Material.DISPENSER, Material.NOTE_BLOCK, Material.JUKEBOX, Material.FURNACE));

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
			
			if(material == Material.STONE_BUTTON || material == Material.LEVER)
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
    	if(event.isCancelled()) return;

		// return if the event is not a right-click-block action
		Action action = event.getAction();
		if(action != Action.RIGHT_CLICK_BLOCK) return;

		Block block = event.getClickedBlock();
		
		if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.ButtonsOnMoreBlocks))
		{
			Player player = event.getPlayer();
			
			// return if they can't build here
			if(!Plugin.canBuild(player, block)) return;

			Material blockMaterial = block.getType();

			// return if the block is not allowed
			if(!AllowedButtonMaterials.contains(blockMaterial)) return;

			BlockFace blockFace = event.getBlockFace();
			ItemStack itemInHand = player.getItemInHand();
			Material itemInHandMaterial = itemInHand.getType();

			// return if they don't have a button or lever in hand
			if(itemInHandMaterial != Material.STONE_BUTTON && itemInHandMaterial != Material.LEVER && itemInHandMaterial != Material.WOOD_PLATE && itemInHandMaterial != Material.STONE_PLATE) return;

			// determine which face was clicked and attach the corresponding button
			Block button;
			switch(blockFace)
			{
			case EAST: // facing east
				button = block.getRelative(BlockFace.EAST); 
				if(button.getType() == Material.AIR)
				{
					button.setType(itemInHandMaterial);
					button.setData((byte)4);
				}
				break;
			case WEST: // facing west
				button = block.getRelative(BlockFace.WEST); 
				if(button.getType() == Material.AIR)
				{
					button.setType(itemInHandMaterial);
					button.setData((byte)3);
				}
				break;
			case NORTH: // facing north
				button = block.getRelative(BlockFace.NORTH); 
				if(button.getType() == Material.AIR)
				{
					button.setType(itemInHandMaterial);
					button.setData((byte)2);
				}
				break;
			case SOUTH: // facing south
				button = block.getRelative(BlockFace.SOUTH); 
				if(button.getType() == Material.AIR)
				{
					button.setType(itemInHandMaterial);
					button.setData((byte)1);
				}
				break;
			case UP:
				if(itemInHandMaterial == Material.STONE_BUTTON) return; // buttons can't go on the top
				button = block.getRelative(BlockFace.UP); 
				if(button.getType() == Material.AIR)
				{
					button.setType(itemInHandMaterial);
					if(itemInHandMaterial == Material.LEVER)
						button.setData((byte)5);
				}
				break;
			default: // bottom was clicked, do nothing
				return;			
			}

			Plugin.takeItemInHand(player);

			event.setCancelled(true);
			
		}
		
    }
}
