package redsgreens.ManySmallTweaks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private final MSTBlockListener blockListener = new MSTBlockListener(this);
    private final MSTPlayerListener playerListener = new MSTPlayerListener(this);
    private final MSTEntityListener entityListener = new MSTEntityListener(this);

    private WorldGuardPlugin WorldGuard = null;

    public String Name;
    public String Version;

    public MSTConfig Config = new MSTConfig(this);
    
    public Set<Material> AllowedRailMaterials = new HashSet<Material>(Arrays.asList(Material.AIR, Material.GLASS, Material.GLOWSTONE, Material.THIN_GLASS, Material.IRON_FENCE, Material.PISTON_BASE, Material.PISTON_STICKY_BASE, Material.PISTON_EXTENSION, Material.PISTON_MOVING_PIECE, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON));
    
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
        pm.registerEvents(blockListener, this);
        pm.registerEvents(playerListener, this);
        pm.registerEvents(entityListener, this);

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
