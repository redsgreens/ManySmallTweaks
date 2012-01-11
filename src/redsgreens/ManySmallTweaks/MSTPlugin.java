package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class MSTPlugin extends JavaPlugin {
    private final MSTBlockListener blockListener = new MSTBlockListener(this);
    private final MSTPlayerListener playerListener = new MSTPlayerListener(this);
    private final MSTEntityListener entityListener = new MSTEntityListener(this);

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
        
        if(Config.EnableFloatingLadders || Config.EnableFloatingRails || Config.EnableFloatingHatch || Config.EnableButtonsOnMoreBlocks)
        	getServer().getPluginManager().registerEvent(Type.BLOCK_PHYSICS, blockListener, Priority.Low, this);

        if(Config.EnableFloatingLadders || Config.EnableFloatingRails || Config.EnableButtonsOnMoreBlocks || Config.EnableInfiniteCauldrons)
        	getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Highest, this);

        if(Config.EnableFloatingLadders)
        	getServer().getPluginManager().registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Normal, this);

        if(Config.EnableProjectileTriggers)
        	getServer().getPluginManager().registerEvent(Type.PROJECTILE_HIT, entityListener, Priority.Normal, this);
        
        if(Config.EnablePercentColorSheep || Config.EnablePercentSaddledPigs|| Config.EnablePigsReproduceQuick)
        	getServer().getPluginManager().registerEvent(Type.CREATURE_SPAWN, entityListener, Priority.Normal, this);
        
        if(Config.EnableKeepSaddleOnPigDeath)
        	getServer().getPluginManager().registerEvent(Type.ENTITY_DEATH, entityListener, Priority.Normal, this);
        
        if(Config.EnableRedstoneIgnitesNetherrack)
        {
            getServer().getPluginManager().registerEvent(Type.REDSTONE_CHANGE, blockListener, Priority.Normal, this);
            getServer().getPluginManager().registerEvent(Type.BLOCK_PISTON_EXTEND, blockListener, Priority.Normal, this);
            getServer().getPluginManager().registerEvent(Type.BLOCK_PISTON_RETRACT, blockListener, Priority.Normal, this);
        	getServer().getPluginManager().registerEvent(Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        }
        
        System.out.println(this.Name + " v" + this.Version + " is enabled!" );
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


	public Block getBlockBehindLadder(Block ladder)
	{
		Integer data = ((Byte)ladder.getData()).intValue();

		switch(data)
		{
		case 2: // facing east
			return ladder.getRelative(BlockFace.WEST);
			
		case 3: // facing west
			return ladder.getRelative(BlockFace.EAST);
			
		case 4: // facing north
			return ladder.getRelative(BlockFace.SOUTH);

		case 5: // facing south
			return ladder.getRelative(BlockFace.NORTH);
		}

		return null;
	}
	
	public ArrayList<Block> getCloseBlocks(Location loc)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		ArrayList<Location> locations = new ArrayList<Location>();
		World world = loc.getWorld();
		
		locations.add(loc);
		locations.add(loc.add(0.25, 0, 0));
		locations.add(loc.add(-0.25, 0, 0));
		locations.add(loc.add(0, 0.25, 0));
		locations.add(loc.add(0, -0.25, 0));
		locations.add(loc.add(0, 0, 0.25));
		locations.add(loc.add(0, 0, -0.25));

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
