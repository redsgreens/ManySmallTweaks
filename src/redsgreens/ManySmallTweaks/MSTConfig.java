package redsgreens.ManySmallTweaks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import org.yaml.snakeyaml.Yaml;

public class MSTConfig {

	// reference to main plugin class
	private MSTPlugin Plugin;

	// determines if config values should be printed at startup
	public Boolean VerboseStartup = false; 

	// these will be set to true if any world has the feature enabled
	public Boolean EnableFloatingLadders = false;
	public Boolean EnableFloatingRails = false;
	public Boolean EnableFloatingHatch = false;
	public Boolean EnableButtonsOnMoreBlocks = false;
	public Boolean EnableProjectileTriggers = false;
	public Boolean EnablePercentSaddledPigs = false;
    public Boolean EnablePercentColorSheep = false;
    public Boolean EnableKeepSaddleOnPigDeath = false;
    public Boolean EnableRedstoneIgnitesNetherrack = false;
    public Boolean EnableInfiniteCauldrons = false;
	
    // specific config data for all worlds in config file 
	public HashMap<String, MSTConfigWorld> Worlds = new HashMap<String, MSTConfigWorld>();
	
	public MSTConfig(MSTPlugin plugin)
	{
		Plugin = plugin;
	}
	
	@SuppressWarnings("unchecked")
	public void loadConfig()
	{
		try
		{
			// create the data folder if it doesn't exist
			File folder = Plugin.getDataFolder();
	    	if(!folder.exists())
	    		folder.mkdirs();
			
			// create the file from the one in the jar if it doesn't exist on disk
	    	File configFile = new File(Plugin.getDataFolder(), "config.yml");
			if (!configFile.exists()){
				configFile.createNewFile();
				InputStream res = MSTPlugin.class.getResourceAsStream("/config.yml");
				FileWriter tx = new FileWriter(configFile);
				for (int i = 0; (i = res.read()) > 0;) tx.write(i);
				tx.flush();
				tx.close();
				res.close();
			}

			// create an empty config
			HashMap<String, Object> configMap = new HashMap<String, Object>();
			BufferedReader rx = new BufferedReader(new FileReader(configFile));
			Yaml yaml = new Yaml();

			try{
				// load the yaml
				configMap = (HashMap<String,Object>)yaml.load(rx);
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
			finally
			{
				rx.close();
			}

			// check for verbose startup
			if(configMap.containsKey("VerboseStartup"))
				VerboseStartup = (Boolean)configMap.get("VerboseStartup");
			
			// loop through worlds in yaml
			if(configMap.containsKey("Worlds"))
			{
				HashMap<String, Object> worlds = (HashMap<String, Object>)configMap.get("Worlds");
				Iterator<String> itr = worlds.keySet().iterator();
				while(itr.hasNext())
				{
					// initialize values for this world
					String worldName = itr.next();
					Boolean floatingLadders = false;
					Boolean floatingRails = false;
					Boolean floatingHatch = false;
					Boolean buttonsOnMoreBlocks = false;
					Boolean projectileTriggers = false;
					Double percentSaddledPigs = 0D;
				    Double percentColorSheep = 0D;
				    Boolean keepSaddleOnPigDeath = false;
				    Boolean redstoneIgnitesNetherrack = false;
				    Boolean infiniteCauldrons = false;
					
					HashMap<String, Object> world = (HashMap<String, Object>)worlds.get(worldName);
					
					// load each field
					if(world.containsKey("FloatingLadders"))
						floatingLadders = (Boolean)world.get("FloatingLadders");
					if(world.containsKey("FloatingRails"))
						floatingRails = (Boolean)world.get("FloatingRails");
					if(world.containsKey("FloatingHatch"))
						floatingHatch = (Boolean)world.get("FloatingHatch");
					if(world.containsKey("ButtonsOnMoreBlocks"))
						buttonsOnMoreBlocks = (Boolean)world.get("ButtonsOnMoreBlocks");
					if(world.containsKey("ProjectileTriggers"))
						projectileTriggers = (Boolean)world.get("ProjectileTriggers");
					if(world.containsKey("PercentSaddledPigs"))
						percentSaddledPigs = Double.parseDouble(world.get("PercentSaddledPigs").toString());
					if(world.containsKey("PercentColorSheep"))
						percentColorSheep = Double.parseDouble(world.get("PercentColorSheep").toString());
					if(world.containsKey("KeepSaddleOnPigDeath"))
						keepSaddleOnPigDeath = (Boolean)world.get("KeepSaddleOnPigDeath");
					if(world.containsKey("RedstoneIgnitesNetherrack"))
						redstoneIgnitesNetherrack = (Boolean)world.get("RedstoneIgnitesNetherrack");
					if(world.containsKey("InfiniteCauldrons"))
						infiniteCauldrons = (Boolean)world.get("InfiniteCauldrons");
					
					// add world to config hashmap
					Worlds.put(worldName, new MSTConfigWorld(floatingLadders, floatingRails, floatingHatch, buttonsOnMoreBlocks, projectileTriggers, percentSaddledPigs, percentColorSheep, keepSaddleOnPigDeath, redstoneIgnitesNetherrack, infiniteCauldrons));
					
					if(VerboseStartup)
					{
						// print values
						System.out.println("ManySmallTweaks: " + worldName + ".FloatingLadders=" + floatingLadders);
						System.out.println("ManySmallTweaks: " + worldName + ".FloatingRails=" + floatingRails);
						System.out.println("ManySmallTweaks: " + worldName + ".FloatingHatch=" + floatingHatch);
						System.out.println("ManySmallTweaks: " + worldName + ".ButtonsOnMoreBlocks=" + buttonsOnMoreBlocks);
						System.out.println("ManySmallTweaks: " + worldName + ".ProjectileTriggers=" + projectileTriggers);
						System.out.println("ManySmallTweaks: " + worldName + ".RedstoneIgnitesNetherrack=" + redstoneIgnitesNetherrack);
						System.out.println("ManySmallTweaks: " + worldName + ".InfiniteCauldrons=" + infiniteCauldrons);
						System.out.println("ManySmallTweaks: " + worldName + ".KeepSaddleOnPigDeath=" + keepSaddleOnPigDeath);
						System.out.println("ManySmallTweaks: " + worldName + ".PercentSaddledPigs=" + percentSaddledPigs);
						System.out.println("ManySmallTweaks: " + worldName + ".PercentColorSheep=" + percentColorSheep);
					}
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		// loop through loaded worlds and set "enable" flags
		Iterator<String> itr = Worlds.keySet().iterator();
		while(itr.hasNext())
		{
			String worldName = itr.next();
			MSTConfigWorld config = Worlds.get(worldName);
			if(config.FloatingLadders) EnableFloatingLadders = true;
			if(config.FloatingRails) EnableFloatingRails = true;
			if(config.FloatingHatch) EnableFloatingHatch = true;
			if(config.ButtonsOnMoreBlocks) EnableButtonsOnMoreBlocks = true;
			if(config.ProjectileTriggers) EnableProjectileTriggers = true;
			if(config.PercentSaddledPigs > 0) EnablePercentSaddledPigs = true;
			if(config.PercentColorSheep > 0) EnablePercentColorSheep = true;
			if(config.KeepSaddleOnPigDeath) EnableKeepSaddleOnPigDeath = true;
			if(config.RedstoneIgnitesNetherrack) EnableRedstoneIgnitesNetherrack = true;
			if(config.InfiniteCauldrons) EnableInfiniteCauldrons = true;
		}
		
		
	}
	
}
