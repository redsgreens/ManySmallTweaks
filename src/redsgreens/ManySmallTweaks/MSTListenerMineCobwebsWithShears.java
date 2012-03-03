package redsgreens.ManySmallTweaks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MSTListenerMineCobwebsWithShears implements Listener {

	private MSTPlugin Plugin;

	Set<Block> BrokenWebs = new HashSet<Block>();
	
	public MSTListenerMineCobwebsWithShears(MSTPlugin plugin)
	{
		Plugin = plugin;
	}
	
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
    	// return if the event is cancelled
    	if(event.isCancelled()) return;

		// return if the event is not a right-click-block action
		Action action = event.getAction();
		if(action != Action.RIGHT_CLICK_BLOCK) return;

		final Block block = event.getClickedBlock();

		if(Plugin.Config.isTweakEnabled(block.getWorld().getName(), MSTName.MineCobwebsWithShears))
		{
			Player player = event.getPlayer();
			
			// return if they can't build here
			if(!Plugin.canBuild(player, block)) return;

			Material blockMaterial = block.getType();
			ItemStack itemInHand = player.getItemInHand();
			Material itemInHandMaterial = itemInHand.getType();

			if(blockMaterial == Material.WEB && itemInHandMaterial == Material.SHEARS)
			{
				event.setCancelled(true);
				if(player.getGameMode() != GameMode.CREATIVE)
					itemInHand.setDurability(((Integer)(itemInHand.getDurability()+1)).shortValue());
				BrokenWebs.add(block);
				block.breakNaturally();
				
				Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
    			    public void run() {
    			    	BrokenWebs.remove(block);
    			    }
    			}, 5);
			}
		}

    }
    
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemSpawn(ItemSpawnEvent event)
	{
    	// return if the event is cancelled
    	if(event.isCancelled()) return;

    	if(event.getEntity() instanceof CraftItem)
    	{
    		ItemStack item = ((CraftItem) event.getEntity()).getItemStack();
    		if(item.getType() == Material.STRING && isDropFromBrokenWeb(event.getLocation()))
    			item.setType(Material.WEB);
    	}
	}
	
	Boolean isDropFromBrokenWeb(Location loc1)
	{
		Iterator<Block> itr = BrokenWebs.iterator();
		while(itr.hasNext())
		{
			Location loc2 = itr.next().getLocation();
			
			if(loc1.getWorld() == loc2.getWorld())
			{
				Double x1 = loc1.getX(); Double y1 = loc1.getY(); Double z1 = loc1.getZ();
				Double x2 = loc2.getX(); Double y2 = loc2.getY(); Double z2 = loc2.getZ();
				
				Double distSquared = ((x1-x2) * (x1-x2)) + ((y1-y2) * (y1-y2)) + ((z1-z2) * (z1-z2));
				
				if(distSquared < 4)
					return true;
			}
		}
		
		return false;
	}
	
	
}
