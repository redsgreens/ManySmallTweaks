package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

public class MSTPlayerListener extends PlayerListener {

	private MSTPlugin Plugin;
	public MSTPlayerListener(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

    ArrayList<Material> AllowedButtonMaterials = new ArrayList<Material>(Arrays.asList(Material.TNT, Material.MOB_SPAWNER, Material.PISTON_BASE, Material.PISTON_STICKY_BASE, Material.PISTON_MOVING_PIECE, Material.SOIL, Material.WOODEN_DOOR, Material.WOOD_DOOR, Material.IRON_DOOR_BLOCK, Material.DISPENSER, Material.NOTE_BLOCK, Material.JUKEBOX, Material.FURNACE));

    @Override
    public void onPlayerInteract(PlayerInteractEvent event)
    // catch player click events
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

		BlockFace blockFace = event.getBlockFace();
		ItemStack itemInHand = player.getItemInHand();
		Material itemInHandMaterial = itemInHand.getType();
		Material blockMaterial = block.getType();

		String worldName = event.getPlayer().getLocation().getWorld().getName();

		if(Plugin.Config.isTweakEnabled(worldName, MSTName.ButtonsOnMoreBlocks))
			handleButtonsOnMoreBlocks(block, blockMaterial, blockFace, player, itemInHand, itemInHandMaterial, event);
		
		if(Plugin.Config.isTweakEnabled(worldName, MSTName.FloatingLadders))
			handleFloatingLadders(block, blockMaterial, blockFace, player, itemInHand, itemInHandMaterial);
		
		if(Plugin.Config.isTweakEnabled(worldName, MSTName.FloatingRails))
			handleFloatingRails(block, blockMaterial, blockFace, player, itemInHand, itemInHandMaterial);
		
		if(Plugin.Config.isTweakEnabled(worldName, MSTName.InfiniteCauldrons))
			handleInfiniteCauldrons(block, blockMaterial, blockFace, player, itemInHand, itemInHandMaterial, event);
		
    }
    
    void handleFloatingLadders(Block block, Material blockMaterial, BlockFace blockFace, Player player, ItemStack itemInHand, Material itemInHandMaterial)
    {
    	// return if it's not a ladder
    	if(blockMaterial != Material.LADDER) return;
    	
    	// return if they're not holding a ladder
    	if(itemInHandMaterial != Material.LADDER) return;

    	// return if blockface is neither up nor down
    	if(blockFace != BlockFace.UP && blockFace != BlockFace.DOWN) return;
    	
    	// place another ladder in the adjacent block
		Block block2 = block.getRelative(blockFace);
		if(block2.getType() != Material.AIR) return;
		Byte data = block.getData();
		block2.setType(Material.LADDER);
		block2.setData(data);
		
		Block behindLadder = Plugin.getBlockBehindLadder(block2);
		if(behindLadder != null && behindLadder.getType() == Material.AIR)
		{
			behindLadder.setType(Material.LADDER);
			
			if(data % 2 == 0)
				data++;
			else
				data--;

			behindLadder.setData(data);
		}

		if(player.getGameMode() != GameMode.CREATIVE)
		{
			// take the item from the player
			if(itemInHand.getAmount() == 1)
				player.setItemInHand(null);
			else
			{
				itemInHand.setAmount(itemInHand.getAmount() - 1);
				player.setItemInHand(itemInHand);
			}
		}
    }
    
    void handleFloatingRails(Block block, Material blockMaterial, BlockFace blockFace, Player player, ItemStack itemInHand, Material itemInHandMaterial)
    {
    	// return if it's not a rail
    	if(blockMaterial != Material.RAILS && blockMaterial != Material.DETECTOR_RAIL && blockMaterial != Material.POWERED_RAIL) return;
    	
    	// return if they're not holding a rail
    	if(itemInHandMaterial != Material.RAILS && itemInHandMaterial != Material.POWERED_RAIL && itemInHandMaterial != Material.DETECTOR_RAIL) return;

    	// return if blockface is not a side
    	if(blockFace != BlockFace.EAST && blockFace != BlockFace.WEST && blockFace != BlockFace.NORTH && blockFace != BlockFace.SOUTH) return;

    	// place another rail in the adjacent block
		Block block2 = block.getRelative(blockFace);
		if(block2.getType() != Material.AIR) return;
		block2.setType(itemInHandMaterial);

		if(player.getGameMode() != GameMode.CREATIVE)
		{
			// take the item from the player
			if(itemInHand.getAmount() == 1)
				player.setItemInHand(null);
			else
			{
				itemInHand.setAmount(itemInHand.getAmount() - 1);
				player.setItemInHand(itemInHand);
			}
		}
    }
    
    void handleButtonsOnMoreBlocks(Block block, Material blockMaterial, BlockFace blockFace, Player player, ItemStack itemInHand, Material itemInHandMaterial, PlayerInteractEvent event)
    {

		// return if the block is not allowed
		if(!AllowedButtonMaterials.contains(blockMaterial)) return;

		// return if they don't have a button or lever in hand
		if(itemInHandMaterial != Material.STONE_BUTTON && itemInHandMaterial != Material.LEVER) return;

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
				button.setData((byte)5);
			}
			break;
		default: // top or bottom was clicked, do nothing
			return;			
		}

		if(player.getGameMode() != GameMode.CREATIVE)
		{
			// take the item from the player
			if(itemInHand.getAmount() == 1)
				player.setItemInHand(null);
			else
			{
				itemInHand.setAmount(itemInHand.getAmount() - 1);
				player.setItemInHand(itemInHand);
			}
		}

		// cancel the event so the inventory is not displayed
		if(blockMaterial == Material.DISPENSER)
			event.setCancelled(true);
    }
    
    
    void handleInfiniteCauldrons(final Block block, Material blockMaterial, BlockFace blockFace, Player player, ItemStack itemInHand, Material itemInHandMaterial, PlayerInteractEvent event)    
    {
    	if(blockMaterial != Material.CAULDRON) return;

    	// water level is stored in the data byte
    	if(block.getData() > 0)
    	{
    		// refill the cauldron
			Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
			    public void run() {
			    	block.setData((byte) 3);
			    }
			}, 0);

			if(itemInHandMaterial == Material.GLASS_BOTTLE)
			{
				itemInHand.setType(Material.POTION);
				player.setItemInHand(itemInHand);
				event.setCancelled(true);
			}
			else if(itemInHandMaterial == Material.BUCKET)
			{
				itemInHand.setType(Material.WATER_BUCKET);
				player.setItemInHand(itemInHand);
				event.setCancelled(true);
			}
    	}

    }
}
