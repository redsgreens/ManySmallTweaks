package redsgreens.ManySmallTweaks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;

@SuppressWarnings("deprecation")
public class MSTListenerFloatingPaintings implements Listener {

	private MSTPlugin Plugin;

	public MSTListenerFloatingPaintings(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPaintingBreak(PaintingBreakEvent event)
	{
		if(event.getCause() != RemoveCause.PHYSICS || event.isCancelled()) return;
		if(Plugin.Config.isTweakEnabled(event.getPainting().getWorld().getName(), MSTName.FloatingPaintings))
			event.setCancelled(true);
	}

}
