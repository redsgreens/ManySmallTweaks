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

    // specific config data for all worlds in config file 
	private HashMap<MSTName, HashMap<String, Boolean>> booleanWorlds = new HashMap<MSTName, HashMap<String, Boolean>>();  
	private HashMap<MSTName, HashMap<String, Double>> doubleWorlds = new HashMap<MSTName, HashMap<String, Double>>();  
	
	public MSTConfig(MSTPlugin plugin)
	{
		Plugin = plugin;
		
		// set up the worlds hashmap
		booleanWorlds.put(MSTName.ButtonsOnMoreBlocks, new HashMap<String, Boolean>());
		booleanWorlds.put(MSTName.FloatingHatch, new HashMap<String, Boolean>());
		booleanWorlds.put(MSTName.FloatingLadders, new HashMap<String, Boolean>());
		booleanWorlds.put(MSTName.FloatingLilyPads, new HashMap<String, Boolean>());
		booleanWorlds.put(MSTName.FloatingPaintings, new HashMap<String, Boolean>());
		booleanWorlds.put(MSTName.FloatingRails, new HashMap<String, Boolean>());
		booleanWorlds.put(MSTName.InfiniteCauldrons, new HashMap<String, Boolean>());
		booleanWorlds.put(MSTName.KeepSaddleOnPigDeath, new HashMap<String, Boolean>());
		booleanWorlds.put(MSTName.PigsReproduceQuick, new HashMap<String, Boolean>());
		booleanWorlds.put(MSTName.ProjectileTriggers, new HashMap<String, Boolean>());
		booleanWorlds.put(MSTName.RedstoneIgnitesNetherrack, new HashMap<String, Boolean>());
		booleanWorlds.put(MSTName.RedstoneIgnitesPumpkins, new HashMap<String, Boolean>());
		doubleWorlds.put(MSTName.PercentColorSheep, new HashMap<String, Double>());
		doubleWorlds.put(MSTName.PercentSaddledPigs, new HashMap<String, Double>());

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

		    // default config values
			MSTConfigWorld Defaults = new MSTConfigWorld();

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
				if(defaults.containsKey("FloatingLilyPads"))
					Defaults.FloatingLilyPads = (Boolean)defaults.get("FloatingLilyPads");
				if(defaults.containsKey("FloatingPaintings"))
					Defaults.FloatingPaintings = (Boolean)defaults.get("FloatingPaintings");

			}

			addWorld("Defaults", Defaults);
			
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
					    Boolean floatingLilyPads = false;
					    Boolean floatingPaintings = false;

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

						if(world.containsKey("FloatingLilyPads"))
							floatingLilyPads = (Boolean)world.get("FloatingLilyPads");
						else
							floatingLilyPads = Defaults.FloatingLilyPads;

						if(world.containsKey("FloatingPaintings"))
							floatingPaintings = (Boolean)world.get("FloatingPaintings");
						else
							floatingPaintings = Defaults.FloatingPaintings;
						
						// add world to config hashmap
						MSTConfigWorld config = new MSTConfigWorld(floatingLadders, floatingRails, floatingHatch, buttonsOnMoreBlocks, projectileTriggers, percentSaddledPigs, percentColorSheep, keepSaddleOnPigDeath, redstoneIgnitesNetherrack, infiniteCauldrons, pigsReproduceQuick, redstoneIgnitesPumpkins, floatingLilyPads, floatingPaintings); 
						addWorld(worldName, config);

					}
				
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public Boolean isTweakEnabled(String worldName, MSTName tweak)
	{
		if(booleanWorlds.get(tweak).containsKey(worldName))
			return booleanWorlds.get(tweak).get(worldName);
		else
			return booleanWorlds.get(tweak).get("Defaults"); 
		
	}
	
	public Double getNumericTweakValue(String worldName, MSTName tweak)
	{
		if(doubleWorlds.get(tweak).containsKey(worldName))
			return doubleWorlds.get(tweak).get(worldName);
		else
			return doubleWorlds.get(tweak).get("Defaults");
	}
	
	void printSettings(String name, MSTConfigWorld config)
	{
		// print values
		System.out.println("ManySmallTweaks: " + name + ".ButtonsOnMoreBlocks=" + config.ButtonsOnMoreBlocks);
		System.out.println("ManySmallTweaks: " + name + ".FloatingHatch=" + config.FloatingHatch);
		System.out.println("ManySmallTweaks: " + name + ".FloatingLadders=" + config.FloatingLadders);
//		System.out.println("ManySmallTweaks: " + name + ".FloatingLilyPads=" + config.FloatingLilyPads);
		System.out.println("ManySmallTweaks: " + name + ".FloatingPaintings=" + config.FloatingPaintings);
		System.out.println("ManySmallTweaks: " + name + ".FloatingRails=" + config.FloatingRails);
		System.out.println("ManySmallTweaks: " + name + ".InfiniteCauldrons=" + config.InfiniteCauldrons);
		System.out.println("ManySmallTweaks: " + name + ".KeepSaddleOnPigDeath=" + config.KeepSaddleOnPigDeath);
		System.out.println("ManySmallTweaks: " + name + ".PercentColorSheep=" + config.PercentColorSheep);
		System.out.println("ManySmallTweaks: " + name + ".PercentSaddledPigs=" + config.PercentSaddledPigs);
		System.out.println("ManySmallTweaks: " + name + ".PigsReproduceQuick=" + config.PigsReproduceQuick);
		System.out.println("ManySmallTweaks: " + name + ".ProjectileTriggers=" + config.ProjectileTriggers);
		System.out.println("ManySmallTweaks: " + name + ".RedstoneIgnitesNetherrack=" + config.RedstoneIgnitesNetherrack);
		System.out.println("ManySmallTweaks: " + name + ".RedstoneIgnitesPumpkins=" + config.RedstoneIgnitesPumpkins);

	}

	// adds world to config hashmap
	void addWorld(String worldName, MSTConfigWorld world)
	{
		booleanWorlds.get(MSTName.ButtonsOnMoreBlocks).put(worldName, world.ButtonsOnMoreBlocks);
		booleanWorlds.get(MSTName.FloatingHatch).put(worldName, world.FloatingHatch);
		booleanWorlds.get(MSTName.FloatingLadders).put(worldName, world.FloatingLadders);
		booleanWorlds.get(MSTName.FloatingLilyPads).put(worldName, world.FloatingLilyPads);
		booleanWorlds.get(MSTName.FloatingPaintings).put(worldName, world.FloatingPaintings);
		booleanWorlds.get(MSTName.FloatingRails).put(worldName, world.FloatingRails);
		booleanWorlds.get(MSTName.InfiniteCauldrons).put(worldName, world.InfiniteCauldrons);
		booleanWorlds.get(MSTName.KeepSaddleOnPigDeath).put(worldName, world.KeepSaddleOnPigDeath);
		booleanWorlds.get(MSTName.PigsReproduceQuick).put(worldName, world.PigsReproduceQuick);
		booleanWorlds.get(MSTName.ProjectileTriggers).put(worldName, world.ProjectileTriggers);
		booleanWorlds.get(MSTName.RedstoneIgnitesNetherrack).put(worldName, world.RedstoneIgnitesNetherrack);
		booleanWorlds.get(MSTName.RedstoneIgnitesPumpkins).put(worldName, world.RedstoneIgnitesPumpkins);
		doubleWorlds.get(MSTName.PercentColorSheep).put(worldName, world.PercentColorSheep);
		doubleWorlds.get(MSTName.PercentSaddledPigs).put(worldName, world.PercentSaddledPigs);
		
		if(VerboseStartup)
			printSettings(worldName, world);

	}

	public Boolean isTweakEnabledAnywhere(MSTName tweak)
	{
		if(tweak == MSTName.PercentColorSheep || tweak == MSTName.PercentSaddledPigs)
		{
			Iterator<Double> itr = doubleWorlds.get(tweak).values().iterator();
			while(itr.hasNext())
			{
				Double d = itr.next();
				if(d > 0) return true;
			}

			return false;
		}
		else
			return booleanWorlds.get(tweak).values().contains(true);
	}
	
}
