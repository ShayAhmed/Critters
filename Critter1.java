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

	@Override
	public boolean fight(String oponent) {
		if(oponent.equals("1")) { // never fight its own
			return getEnergy() > ((Params.min_reproduce_energy * 75) / 100); // fight if it has 75% of reproduction energy or higher
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "1";
	}
	
	public static void runStats(java.util.List<Critter> population) {
		System.out.println(population.size() + " total Critter1 moving in direction " + move_direction);
		System.out.println("Critter1 has " + reproductions + " reproductions since last direction change");
	}

}
