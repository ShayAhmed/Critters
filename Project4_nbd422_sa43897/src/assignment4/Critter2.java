package assignment4;
/* CRITTERS Critter2.java
 * EE422C Project 4 submission by
 * Shehryar Ahmed
 * SA43897
 * 16345
 * Nicholas Duggar
 * NBD422
 * 16345
 * Slip days used: <0>
 * Fall 2018
 */
/**
 * This critter can "see" how plentiful food is in the world by looking at the ratio of Algae to 
 *  world grid spaces. It will only move and breed if it believes food to be reasonably plentiful.
 *  It goes further with breeding, however, in that it it looks at its own population
 *  and tries to determine if there are too many instances for the world (to prevent over-population).
 *  runStats includes basic population and whether the species is attempting to reproduce
 */
public class Critter2 extends Critter {

	private boolean has_moved;
	private static boolean reproducing = false;
	
	/**
	 * The function that determines what this critter does each world time step. This species of critter will calculate the current
	 *  Algae-to-world-area ratio and the Critter2-count-to-world-area ratios to determine whether to move and/or breed.
	 *  
	 */
	@Override
	public void doTimeStep() {
		has_moved = false;
		try {
			java.util.List<Critter> population_holder = Critter.getInstances("Algae");
			double food_ratio = (double) population_holder.size() / ((double) Params.world_width * Params.world_height);
			if(food_ratio >= .1 ) {
				// So if there is at least a 10% chance of finding Algae in any square
				walk(Critter.getRandomInt(8));
				has_moved = true;
			}
			population_holder = Critter.getInstances("Critter2");
			double population_ratio = (double) population_holder.size() / ((double) Params.world_width * Params.world_height);
			if(population_ratio < food_ratio / 10) {
				reproducing = true;
				reproduce(new Critter2(), Critter.getRandomInt(8));
			}
			else {
				reproducing = false;
			}
		}
		catch(Exception e) {
			// don't move!
		}
	}

	/**
	 * Whether this critter will fight another critter when they share a space. If this critter didn't move during its doTimeStep(), it will
	 *  attempt to flee. If it fails to flee, then it will fight.
	 * 
	 * @param oponent The String representation of the other critter this critter may fight. (unused)
	 * @return Whether this critter will fight the other. (always true)
	 */
	@Override
	public boolean fight(String oponent) {
		if(!has_moved) { // attempt to flee combat first
			walk(Critter.getRandomInt(8));
		}
		return true;
	}
	
	/**
	 * How this critter appears in the world
	 * 
	 * @return "2"
	 */
	@Override
	public String toString() {
		return "2";
	}
	
	/**
	 * This override of the runStats method indicates the current count of Critter2's and whether the species is reproducing.
	 *  
	 * @param population The List of all Critter2 instances
	 */
	public static void runStats(java.util.List<Critter> population) {
		System.out.print(population.size() + " total Critter2.");
		if(reproducing) {
			System.out.println(" Critter2 is reproducing as a species.");
		}
		else {
			System.out.println(" Critter2 is waiting for more food to reproduce.");
		}
	}

}
