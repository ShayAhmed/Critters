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

	@Override
	public boolean fight(String oponent) {
		if(!has_moved) { // attempt to flee combat first
			walk(Critter.getRandomInt(8));
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "2";
	}
	
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
