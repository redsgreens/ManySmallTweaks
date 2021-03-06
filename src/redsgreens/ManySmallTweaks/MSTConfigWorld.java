package redsgreens.ManySmallTweaks;

public class MSTConfigWorld {

	public Boolean FloatingLadders = false;
	public Boolean FloatingRails = false;
	public Boolean FloatingHatch = false;
	public Boolean ButtonsOnMoreBlocks = false;
	public Boolean ProjectileTriggers = false;
	public Double PercentSaddledPigs = 0D;
    public Double PercentColorSheep = 0D;
//    public Boolean KeepSaddleOnPigDeath = false;
    public Boolean RedstoneIgnitesNetherrack = false;
    public Boolean InfiniteCauldrons = false;
    public Boolean PigsReproduceQuick = false;
    public Boolean RedstoneIgnitesPumpkins = false;
    public Boolean FloatingLilyPads = false;
    public Boolean FloatingPaintings = false;
    public Boolean MineCobwebsWithShears = false;

	public MSTConfigWorld(Boolean floatingLadders, Boolean floatingRails, Boolean floatingHatch, Boolean buttonsOnMoreBlocks, 
			Boolean projectileTriggers, Double percentSaddledPigs, Double percentColorSheep, Boolean redstoneIgnitesNetherrack,
			Boolean infiniteCauldrons, Boolean pigsReproduceQuick, Boolean redstoneIgnitesPumpkins, Boolean floatingLilyPads, Boolean floatingPaintings,
			Boolean mineCobwebsWithShears)
	{
		FloatingLadders = floatingLadders;
		FloatingRails = floatingRails;
		FloatingHatch = floatingHatch;
		ButtonsOnMoreBlocks = buttonsOnMoreBlocks;
		ProjectileTriggers = projectileTriggers;
		PercentSaddledPigs = percentSaddledPigs;
		PercentColorSheep = percentColorSheep;
//		KeepSaddleOnPigDeath = keepSaddleOnPigDeath;
		RedstoneIgnitesNetherrack = redstoneIgnitesNetherrack;
		InfiniteCauldrons = infiniteCauldrons;
		PigsReproduceQuick = pigsReproduceQuick;
		RedstoneIgnitesPumpkins = redstoneIgnitesPumpkins;
		FloatingLilyPads = floatingLilyPads;
		FloatingPaintings = floatingPaintings;
		MineCobwebsWithShears = mineCobwebsWithShears;
	}
	
	public MSTConfigWorld()
	{
		
	}
}
