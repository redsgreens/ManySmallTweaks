package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class MSTPlugin extends JavaPlugin {
    private WorldGuardPlugin WorldGuard = null;

    public String Name;
    public String Version;

    public MSTConfig Config = new MSTConfig(this);
    
    public void onEnable() {

        Name = getDescription().getName();
        Version = getDescription().getVersion();

        Config.loadConfig();
        
        try{
            Plugin test = getServer().getPluginManager().getPlugin("WorldGuard");

            if (test != null) {
             WorldGuard = (WorldGuardPlugin)test;
             if(Config.VerboseStartup)
            	 System.out.println(this.Name + ": " + WorldGuard.getDescription().getName() + " " + WorldGuard.getDescription().getVersion() + " found");
            }
        }
        catch (Exception ex){
        	WorldGuard = null;
        }
        
        // register our events
        PluginManager pm = getServer().getPluginManager();
        
        if(Config.isTweakEnabledAnywhere(MSTName.ButtonsOnMoreBlocks)) pm.registerEvents(new MSTListenerButtonsOnMoreBlocks(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.FloatingHatch)) pm.registerEvents(new MSTListenerFloatingHatch(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.FloatingLadders)) pm.registerEvents(new MSTListenerFloatingLadders(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.FloatingLilyPads)) pm.registerEvents(new MSTListenerFloatingLilyPads(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.FloatingPaintings)) pm.registerEvents(new MSTListenerFloatingPaintings(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.FloatingRails)) pm.registerEvents(new MSTListenerFloatingRails(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.InfiniteCauldrons)) pm.registerEvents(new MSTListenerInfiniteCauldrons(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.KeepSaddleOnPigDeath)) pm.registerEvents(new MSTListenerKeepSaddleOnPigDeath(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.PigsReproduceQuick)) pm.registerEvents(new MSTListenerPigsReproduceQuick(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.ProjectileTriggers)) pm.registerEvents(new MSTListenerProjectileTriggers(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.RedstoneIgnitesNetherrack)) pm.registerEvents(new MSTListenerRedstoneIgnitesNetherrack(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.RedstoneIgnitesPumpkins)) pm.registerEvents(new MSTListenerRedstoneIgnitesPumpkins(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.PercentColorSheep)) pm.registerEvents(new MSTListenerPercentColorSheep(this), this);
        if(Config.isTweakEnabledAnywhere(MSTName.PercentSaddledPigs)) pm.registerEvents(new MSTListenerPercentSaddledPigs(this), this);

        System.out.println(this.Name + " v" + this.Version + " is enabled!" );
    }


    public void takeItemInHand(Player player)
    {
		if(player.getGameMode() != GameMode.CREATIVE)
		{
			ItemStack itemInHand = player.getItemInHand();
			
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
    
	public Block getBlockBehindButton(Block b)
	{
		Integer d = ((Byte)b.getData()).intValue();
		if(d>8) d-=8;

		switch(d)
		{
		case 4:
			return b.getRelative(BlockFace.WEST);
		case 3:
			return b.getRelative(BlockFace.EAST);
		case 2:
			return b.getRelative(BlockFace.SOUTH);
		case 1:
			return b.getRelative(BlockFace.NORTH);
		case 5:
			return b.getRelative(BlockFace.DOWN);
		default:
			return b;				
		}
	}

	public ArrayList<Block> getCloseBlocks(Location loc)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		World world = loc.getWorld();
		ArrayList<Location> locations = new ArrayList<Location>();
		
		locations.add(loc);
		locations.add(loc.clone().add(-0.5, -0.5, -0.5));
		locations.add(loc.clone().add(-0.5, -0.5, 0));
		locations.add(loc.clone().add(-0.5, -0.5, 0.5));
		locations.add(loc.clone().add(-0.5, 0, -0.5));
		locations.add(loc.clone().add(-0.5, 0, 0));
		locations.add(loc.clone().add(-0.5, 0, 0.5));
		locations.add(loc.clone().add(-0.5, 0.5, -0.5));
		locations.add(loc.clone().add(-0.5, 0.5, 0));
		locations.add(loc.clone().add(-0.5, 0.5, 0.5));
		locations.add(loc.clone().add(0, -0.5, -0.5));
		locations.add(loc.clone().add(0, -0.5, 0));
		locations.add(loc.clone().add(0, -0.5, 0.5));
		locations.add(loc.clone().add(0, 0, -0.5));
		locations.add(loc.clone().add(0, 0, 0));
		locations.add(loc.clone().add(0, 0, 0.5));
		locations.add(loc.clone().add(0, 0.5, -0.5));
		locations.add(loc.clone().add(0, 0.5, 0));
		locations.add(loc.clone().add(0, 0.5, 0.5));
		locations.add(loc.clone().add(0.5, -0.5, -0.5));
		locations.add(loc.clone().add(0.5, -0.5, 0));
		locations.add(loc.clone().add(0.5, -0.5, 0.5));
		locations.add(loc.clone().add(0.5, 0, -0.5));
		locations.add(loc.clone().add(0.5, 0, 0));
		locations.add(loc.clone().add(0.5, 0, 0.5));
		locations.add(loc.clone().add(0.5, 0.5, -0.5));
		locations.add(loc.clone().add(0.5, 0.5, 0));
		locations.add(loc.clone().add(0.5, 0.5, 0.5));

		Iterator<Location> itr = locations.iterator();
		while(itr.hasNext())
		{
			Location l = itr.next();
			Block block = world.getBlockAt(l);
			if(!blocks.contains(block))
				blocks.add(block);
		}
		
		return blocks;
	}
	
    public Boolean canBuild(Player p, Block b)
    {
    	if(WorldGuard != null)
    		return WorldGuard.canBuild(p, b);
    	else
    		return true;
    }
    public void onDisable() {
        System.out.println(this.Name + " v" + this.Version + " is disabled." );
    }

}
