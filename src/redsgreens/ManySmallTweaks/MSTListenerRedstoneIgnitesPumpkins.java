package redsgreens.ManySmallTweaks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class MSTListenerRedstoneIgnitesPumpkins implements Listener {

	private MSTPlugin Plugin;

	public MSTListenerRedstoneIgnitesPumpkins(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockRedstoneChange(BlockRedstoneEvent event)
	{
		Block block = event.getBlock();
		Material material = block.getType();
		String worldName = block.getWorld().getName();

		if((material == Material.PUMPKIN || material == Material.JACK_O_LANTERN) && Plugin.Config.isTweakEnabled(worldName, MSTName.RedstoneIgnitesPumpkins))
		{
			Byte data = block.getData();
			if(event.getNewCurrent() > 0)
			{
				block.setType(Material.JACK_O_LANTERN);
				block.setData(data);
			}
			else
			{
				block.setType(Material.PUMPKIN);
				block.setData(data);
			}

		}
		
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled()) return;
		
		Block block = event.getBlock();
		Material material = block.getType();
		String worldName = block.getWorld().getName();

		if(material == Material.PUMPKIN && Plugin.Config.isTweakEnabled(worldName, MSTName.RedstoneIgnitesPumpkins))
		{
			// replace with jackolantern
			if(block.getBlockPower() > 0)
			{
				Byte data = block.getData();
				block.setType(Material.JACK_O_LANTERN);
				block.setData(data);				
			}
			
		}
		
	}

}
