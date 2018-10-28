package assignment4;
/* CRITTERS Critter1.java
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
 * This critter class is modeled after a "hive-mind" mentality, using static variables
 *  as a form of communication amongst all of the critters. In the interest of the species,
 *  and knowing that they can all only move once a turn and that that always move, if they 
 *  encounter each other, if one is weak (<75% reproduction energy) it will
 *  submit to being eaten, so energy is preserved and the species will (hopefully) survive.
 *  If the population reproduces many times, it will change movement direction by 90 degrees counterclockwise
 *  so the babies aren't constantly being spawned in atop other members of the species
 *  runStats gives population count and the direction the population is moving.
 *  
 */
public class Critter1 extends Critter {
	
	private static int move_direction = Critter.getRandomInt(8); // doesn't include 8
	private static int reproductions = 0;
	
	/**
	 * The function that determines what this critter does each world time step. This species of critter will walk in move_direction if doing so
	 *  will not lead to its death. The critter will also reproduce if it has sufficient energy - the offspring will be placed in move_direction.
	 *  When this critter reproduces, it increments the field reproductions, and when reproductions reaches more than half of either the width or height
	 *  of the world, the species will turn 90 degrees (incrementing move_direction by 2)
	 */
	@Override
	public void doTimeStep() {
		if(getEnergy() > Params.walk_energy_cost + Params.rest_energy_cost) {
			walk(move_direction);
		}
		if(getEnergy() > Params.min_reproduce_energy) {
			Critter offspring = new Critter1();
			reproduce(offspring, move_direction);
			reproductions++;
		}
		// It produces all offspring ahead of it, so if done too many times makes a massive blob! So it changes movement angle by 90 degrees
		if(reproductions > Params.world_height / 2 || reproductions > Params.world_width / 2) {
			reproductions = 0;
			move_direction = (move_direction + 2) % 8;
		}
	}

	/**
	 * Whether this critter will fight another critter when they share a space. If the other critter is of the same class (oponent.equals"1")
	 *  this critter will only fight the other if its energy is at least 75% of the minimum reproduction energy (in Params.class)
	 * 
	 * @param oponent The String representation of the other critter this critter may fight.
	 * @return Whether this critter will fight the other. 
	 */
	@Override
	public boolean fight(String oponent) {
		if(oponent.equals("1")) { // never fight its own
			return getEnergy() > ((Params.min_reproduce_energy * 75) / 100); // fight if it has 75% of reproduction energy or higher
		}
		return true;
	}
	
	/**
	 * How this critter appears in the world
	 * 
	 * @return "1"
	 */
	@Override
	public String toString() {
		return "1";
	}
	
	/**
	 * This override of the runStats method indicates the current direction the species is using and the
	 *  reproduction count since last direction change in addition to the population count.
	 *  
	 * @param population The List of all Critter1 instances
	 */
	public static void runStats(java.util.List<Critter> population) {
		System.out.println(population.size() + " total Critter1 moving in direction " + move_direction);
		System.out.println("Critter1 has " + reproductions + " reproductions since last direction change");
	}

}
