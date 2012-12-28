package redsgreens.ManySmallTweaks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;

public class MSTListenerFloatingPaintings implements Listener {

	private MSTPlugin Plugin;

	public MSTListenerFloatingPaintings(MSTPlugin plugin)
	{
		Plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onHangingBreak(HangingBreakEvent event)
	{
		if(event.getCause() != RemoveCause.PHYSICS || event.isCancelled()) return;
		if(Plugin.Config.isTweakEnabled(event.getEntity().getWorld().getName(), MSTName.FloatingPaintings))
			event.setCancelled(true);
	}

}
