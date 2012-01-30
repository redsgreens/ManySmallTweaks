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
/*
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
    public Boolean EnablePigsReproduceQuick = false;
    public Boolean EnableRedstoneIgnitesPumpkins = false;
*/	
    public MSTConfigWorld Defaults = new MSTConfigWorld();
    
    // specific config data for all worlds in config file 
	private HashMap<String, MSTConfigWorld> Worlds = new HashMap<String, MSTConfigWorld>();
	
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
			
			if(configMap.containsKey("Defaults"))
			{
				HashMap<String, Object> defaults = (HashMap<String, Object>)configMap.get("Defaults");

				// load each field
				if(defaults.containsKey("FloatingLadders"))
					Defaults.FloatingLadders = (Boolean)defaults.get("FloatingLadders");
				if(defaults.containsKey("FloatingRails"))
					Defaults.FloatingRails = (Boolean)defaults.get("FloatingRails");
				if(defaults.containsKey("FloatingHatch"))
					Defaults.FloatingHatch = (Boolean)defaults.get("FloatingHatch");
				if(defaults.containsKey("ButtonsOnMoreBlocks"))
					Defaults.ButtonsOnMoreBlocks = (Boolean)defaults.get("ButtonsOnMoreBlocks");
				if(defaults.containsKey("ProjectileTriggers"))
					Defaults.ProjectileTriggers = (Boolean)defaults.get("ProjectileTriggers");
				if(defaults.containsKey("PercentSaddledPigs"))
					Defaults.PercentSaddledPigs = Double.parseDouble(defaults.get("PercentSaddledPigs").toString());
				if(defaults.containsKey("PercentColorSheep"))
					Defaults.PercentColorSheep = Double.parseDouble(defaults.get("PercentColorSheep").toString());
				if(defaults.containsKey("KeepSaddleOnPigDeath"))
					Defaults.KeepSaddleOnPigDeath = (Boolean)defaults.get("KeepSaddleOnPigDeath");
				if(defaults.containsKey("RedstoneIgnitesNetherrack"))
					Defaults.RedstoneIgnitesNetherrack = (Boolean)defaults.get("RedstoneIgnitesNetherrack");
				if(defaults.containsKey("InfiniteCauldrons"))
					Defaults.InfiniteCauldrons = (Boolean)defaults.get("InfiniteCauldrons");
				if(defaults.containsKey("PigsReproduceQuick"))
					Defaults.PigsReproduceQuick = (Boolean)defaults.get("PigsReproduceQuick");
				if(defaults.containsKey("RedstoneIgnitesPumpkins"))
					Defaults.RedstoneIgnitesPumpkins = (Boolean)defaults.get("RedstoneIgnitesPumpkins");

				if(VerboseStartup)
					PrintSettings("Defaults", Defaults);

			}
			
			// loop through worlds in yaml
			if(configMap.containsKey("Worlds"))
			{
				HashMap<String, Object> worlds = (HashMap<String, Object>)configMap.get("Worlds");
				if(worlds != null)
				{
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
					    Boolean pigsReproduceQuick = false;
					    Boolean redstoneIgnitesPumpkins = false;
						
						HashMap<String, Object> world = (HashMap<String, Object>)worlds.get(worldName);
						
						// load each field
						if(world.containsKey("FloatingLadders"))
							floatingLadders = (Boolean)world.get("FloatingLadders");
						else
							floatingLadders = Defaults.FloatingLadders;
							
						if(world.containsKey("FloatingRails"))
							floatingRails = (Boolean)world.get("FloatingRails");
						else
							floatingRails = Defaults.FloatingRails;
						
						if(world.containsKey("FloatingHatch"))
							floatingHatch = (Boolean)world.get("FloatingHatch");
						else
							floatingHatch = Defaults.FloatingHatch;
						
						if(world.containsKey("ButtonsOnMoreBlocks"))
							buttonsOnMoreBlocks = (Boolean)world.get("ButtonsOnMoreBlocks");
						else
							buttonsOnMoreBlocks = Defaults.ButtonsOnMoreBlocks;
						
						if(world.containsKey("ProjectileTriggers"))
							projectileTriggers = (Boolean)world.get("ProjectileTriggers");
						else
							projectileTriggers = Defaults.ProjectileTriggers;
							
						if(world.containsKey("PercentSaddledPigs"))
							percentSaddledPigs = Double.parseDouble(world.get("PercentSaddledPigs").toString());
						else
							percentSaddledPigs = Defaults.PercentSaddledPigs;
						
						if(world.containsKey("PercentColorSheep"))
							percentColorSheep = Double.parseDouble(world.get("PercentColorSheep").toString());
						else
							percentColorSheep = Defaults.PercentColorSheep;
						
						if(world.containsKey("KeepSaddleOnPigDeath"))
							keepSaddleOnPigDeath = (Boolean)world.get("KeepSaddleOnPigDeath");
						else
							keepSaddleOnPigDeath = Defaults.KeepSaddleOnPigDeath;
						
						if(world.containsKey("RedstoneIgnitesNetherrack"))
							redstoneIgnitesNetherrack = (Boolean)world.get("RedstoneIgnitesNetherrack");
						else
							redstoneIgnitesNetherrack = Defaults.RedstoneIgnitesNetherrack;
						
						if(world.containsKey("InfiniteCauldrons"))
							infiniteCauldrons = (Boolean)world.get("InfiniteCauldrons");
						else
							infiniteCauldrons = Defaults.InfiniteCauldrons;
						
						if(world.containsKey("PigsReproduceQuick"))
							pigsReproduceQuick = (Boolean)world.get("PigsReproduceQuick");
						else
							pigsReproduceQuick = Defaults.PigsReproduceQuick;
						
						if(world.containsKey("RedstoneIgnitesPumpkins"))
							redstoneIgnitesPumpkins = (Boolean)world.get("RedstoneIgnitesPumpkins");
						else
							redstoneIgnitesPumpkins = Defaults.RedstoneIgnitesPumpkins;
						
						// add world to config hashmap
						MSTConfigWorld config = new MSTConfigWorld(floatingLadders, floatingRails, floatingHatch, buttonsOnMoreBlocks, projectileTriggers, percentSaddledPigs, percentColorSheep, keepSaddleOnPigDeath, redstoneIgnitesNetherrack, infiniteCauldrons, pigsReproduceQuick, redstoneIgnitesPumpkins); 
						Worlds.put(worldName, config);
						
						if(VerboseStartup)
							PrintSettings(worldName, config);

					}
				
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
/*
		// temporarily add the defaults to the worlds list
		Worlds.put("DEFAULTS", Defaults);
		
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
			if(config.RedstoneIgnitesPumpkins) EnableRedstoneIgnitesPumpkins = true; 
		}
		
		// remove defaults from worlds list after testing for enabled options
		Worlds.remove("DEFAULTS");
*/		
	}
	
	public Boolean isTweakEnabled(String worldName, MSTName tweak)
	{
		MSTConfigWorld config;
		
		if(Worlds.containsKey(worldName))
			config = Worlds.get(worldName);
		else
			config = Defaults;
		
		switch(tweak)
		{
		case FloatingLadders:
			return config.FloatingLadders;

		case FloatingRails:
			return config.FloatingRails;
			
		case FloatingHatch:
			return config.FloatingHatch;
			
		case ButtonsOnMoreBlocks:
			return config.ButtonsOnMoreBlocks;
			
		case ProjectileTriggers:
			return config.ProjectileTriggers;
			
		case PercentSaddledPigs:
			if(config.PercentSaddledPigs > 0)
				return true;
			else
				return false;
			
		case PercentColorSheep:
			if(config.PercentColorSheep > 0)
				return true;
			else
				return false;
		
		case KeepSaddleOnPigDeath:
			return config.KeepSaddleOnPigDeath;
		
		case RedstoneIgnitesNetherrack:
			return config.RedstoneIgnitesNetherrack;
			
		case InfiniteCauldrons:
			return config.InfiniteCauldrons;
		
		case PigsReproduceQuick:
			return config.PigsReproduceQuick;

		case RedstoneIgnitesPumpkins:
			return config.RedstoneIgnitesPumpkins;
			
		default: 
			return false;
		}

	}
	
	public Double getNumericTweakValue(String worldName, MSTName tweak)
	{
		MSTConfigWorld config;
		
		if(Worlds.containsKey(worldName))
			config = Worlds.get(worldName);
		else
			config = Defaults;
		
		switch(tweak)
		{
		case PercentSaddledPigs:
			return config.PercentSaddledPigs;
			
		case PercentColorSheep:
			return config.PercentColorSheep;

		default:
			return 0D;
		}
	}
	
	void PrintSettings(String name, MSTConfigWorld config)
	{
		// print values
		System.out.println("ManySmallTweaks: " + name + ".FloatingLadders=" + config.FloatingLadders);
		System.out.println("ManySmallTweaks: " + name + ".FloatingRails=" + config.FloatingRails);
		System.out.println("ManySmallTweaks: " + name + ".FloatingHatch=" + config.FloatingHatch);
		System.out.println("ManySmallTweaks: " + name + ".ButtonsOnMoreBlocks=" + config.ButtonsOnMoreBlocks);
		System.out.println("ManySmallTweaks: " + name + ".ProjectileTriggers=" + config.ProjectileTriggers);
		System.out.println("ManySmallTweaks: " + name + ".RedstoneIgnitesNetherrack=" + config.RedstoneIgnitesNetherrack);
		System.out.println("ManySmallTweaks: " + name + ".RedstoneIgnitesPumpkins=" + config.RedstoneIgnitesPumpkins);
		System.out.println("ManySmallTweaks: " + name + ".InfiniteCauldrons=" + config.InfiniteCauldrons);
		System.out.println("ManySmallTweaks: " + name + ".KeepSaddleOnPigDeath=" + config.KeepSaddleOnPigDeath);
		System.out.println("ManySmallTweaks: " + name + ".PigsReproduceQuick=" + config.PigsReproduceQuick);
		System.out.println("ManySmallTweaks: " + name + ".PercentSaddledPigs=" + config.PercentSaddledPigs);
		System.out.println("ManySmallTweaks: " + name + ".PercentColorSheep=" + config.PercentColorSheep);

	}
}
