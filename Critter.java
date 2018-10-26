package assignment4;
/* CRITTERS Critter.java
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



import java.util.Iterator;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	/*
	 * version is essentially a fix to avoid having to iterate through boolean hasMoved flags
	 * Is set to global_version when the Critter has moved, so if version != global_version, a
	 *     Critter may move.
	 */
	private long version;
	private static long global_version = 0;
	
	/*
	 * This boolean flag represents whether or not worldTimeStep() is in the encounter phase
	 * Since the Critters can only move during the movement phase and encounter phase, this being false
	 *     implies the movement phase, so we need not check the critter versions 
	 */
	private static boolean determining_encounters;
	
	private static boolean doingWorldTimeStep = false; // Basically, only check for multiple movements if worldTimeStep is being called - prevents errors in grading scripts
	
	/**
	 * Attempt to take 1 step in a given direction. Checks if caller has moved this turn already. Deducts Params.walk_energy_cost regardless
	 *  If a Critter is calling to attempt to flee combat, checks to see if movement will be blocked by another Critter.
	 * @param direction The direction to walk
	 */
	protected final void walk(int direction) {
		this.energy -= Params.walk_energy_cost;
		if (this.version != global_version || !doingWorldTimeStep) {
			if(determining_encounters && look(direction, 1) != null) { // if fleeing combat
				return; // attempt to move blocked by another critter
			}
			this.x_coord = newX(this.x_coord, direction, 1);
			this.y_coord = newY(this.y_coord, direction, 1);
			this.version = global_version;
		}
	}
	
	/**
	 * Attempt to take 2 steps in a given direction. Checks if caller has moved this turn already. Deducts Params.run_energy_cost regardless.
	 *  If a Critter is calling to attempt to flee combat, checks to see if movement will be blocked by another Critter.
	 * @param direction The direction to run
	 */
	protected final void run(int direction) {
		this.energy -= Params.run_energy_cost;
		if (this.version != global_version || !doingWorldTimeStep) {
			if(determining_encounters && look(direction, 2) != null) { // if fleeing combat
				return; // attempt to move blocked by another critter
			}
			this.x_coord = newX(this.x_coord, direction,2);
			this.y_coord = newY(this.y_coord, direction,2);
			this.version = global_version;
		}
	}
	
	/**
	 * Determine what x coordinate will be after a specified move
	 * @param current_x The current x coordinate
	 * @param direction The direction to move (0-7)
	 * @param steps How many steps to take in said direction
	 * @return The resulting x coordinate after the move.
	 */
	private final int newX(int current_x, int direction, int steps) {
		if(direction == 7 || direction < 2) {
			current_x += steps;
		}
		else if(direction > 2 && direction < 6) {
			current_x -= steps;
		}
		if(current_x < 0) {
			current_x = Params.world_width - 1;
		}
		current_x = current_x % Params.world_width;
		return current_x;
	}
	
	/**
	 * Determine what y coordinate will be after a specified move
	 * @param current_y The current y coordinate
	 * @param direction The direction to move (0-7)
	 * @param steps How many steps to take in said direction
	 * @return The resulting y coordinate after the move.
	 */
	private final int newY(int current_y,int direction,int steps) {
		if(direction > 0 && direction < 4) {
			current_y -= steps;
		}
		else if(direction > 4) {
			current_y += steps;
		}
		if(current_y < 0) {
			current_y = Params.world_height - 1;
		}
		current_y = current_y % Params.world_height;
		return current_y;
	}
	
	/**
	 * Look at a potential walk/run coordinate and find the first alive critter that is there that isn't the caller.
	 * @param direction The direction to look (0-7)
	 * @param num_steps How many steps in said direction to go to look
	 * @return The first Critter found to inhabit the space. Null if none.
	 */
	private final Critter look(int direction, int num_steps) {
		int lookx = newX(this.x_coord, direction, num_steps);
		int looky = newY(this.y_coord, direction, num_steps);
		for (Critter crit : population) {
			if (crit.x_coord == lookx && crit.y_coord == looky && crit.energy > 0 && crit != this) {
				return crit;
			}
		}
		return null;
	}
	
	/**
	 * Iterate through the list of critters and return the first other one that shares the same space and is alive
	 * @return The first non-calling alive critter at caller's location. Null if none exists
	 */
	private final Critter check_encounter() {
		return look(0, 0);
	}
	
	/**
	 * The method a Critter will call when it wishes to reproduce. Checks minimum energy,
	 *  and adds the offspring to the babies list at the appropriate location. Initializes inherited baby fields.
	 * @param offspring The baby to add
	 * @param direction Where relative to the parent to put the baby
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if(this.energy < Params.min_reproduce_energy) {
			return;
		}
		offspring.x_coord = newX(this.x_coord,direction, 1);
		offspring.y_coord = newY(this.y_coord,direction, 1);
		offspring.energy = this.energy/2;
		offspring.version = global_version;
		
		if(this.energy %2 ==1) {
			this.energy++;
		}
		this.energy /= 2;
		babies.add(offspring);
	
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			Class critter_class = Class.forName(myPackage + "." + critter_class_name);
			Critter created_critter = (Critter) critter_class.newInstance();
			created_critter.energy = Params.start_energy;
			created_critter.x_coord = Critter.getRandomInt(Params.world_width);
			created_critter.y_coord = Critter.getRandomInt(Params.world_height);
			created_critter.version = global_version;
			population.add(created_critter);
		}
		catch(Exception e) {
			throw new InvalidCritterException(critter_class_name);
		}
		// TODO: verify
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		Class critter_class;
		try {
			critter_class = Class.forName(myPackage + "." + critter_class_name);
		} catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		for(Critter critter : population) {
			if(critter_class.isInstance(critter)) {
				result.add(critter);
			}
		}
		// TODO: verify/debug
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}
	// End test critter!

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population = new java.util.ArrayList<Critter>();
		babies = new java.util.ArrayList<Critter>();
	}
	
	/**
	 * Advance the Critter World by one time step, first by calling doTimeStep() for every critter,
	 *  then by running through encounters. These are then followed by application of resting costs, 
	 *  a culling of the dead, and then the addition of babies and Algae to the main population.
	 */
	public static void worldTimeStep() {
		doingWorldTimeStep = true;
		determining_encounters = false;
		global_version++;
		// All Critters doTimeStep()
	    for(Critter crit : population) {
	    	crit.doTimeStep(); // theoretically enough?
	    }
	    // Check encounters
	    determining_encounters = true;
		for (Critter crit:population) {
			if(crit.energy < 1) {
				continue;
			}
			Critter temp = crit.check_encounter();
			while(temp != null) {
				boolean a_fight = crit.fight(temp.toString());
				boolean b_fight = temp.fight(crit.toString());
				if(temp.energy < 1 || crit.energy<1 || temp.x_coord != crit.x_coord|| temp.y_coord != crit.y_coord) {
					break;
				}
				int a_roll = 0;
				int b_roll = 0;
				if (a_fight) {
					a_roll = Critter.getRandomInt(crit.energy);
				}
				if (b_fight) {
					b_roll = Critter.getRandomInt(temp.energy);
				}

				if (a_roll < b_roll) {//crit < temp
					temp.energy += crit.energy / 2;
					crit.energy = 0;	
				}
				else {
					crit.energy += temp.energy / 2;
					temp.energy = 0;	
				}
				if(crit.energy > 0) {
					temp = crit.check_encounter();
				}
			}
		}
		determining_encounters = false;
		// Cull the dead (after applying rest energy)
		Iterator it = population.iterator();
		while(it.hasNext()) {
			Critter crit = (Critter) it.next();
			crit.energy -= Params.rest_energy_cost;
			if (crit.energy < 1) {
				it.remove();
			}
		}
		// Add babies and Algae
		population.addAll(babies);
		babies = new java.util.ArrayList<Critter>();
		try {
			for(int i = 0; i < Params.refresh_algae_count; i++) {
				Critter.makeCritter("Algae");
			}
		}
		catch (Exception e) {
			// Will only fail if algae class is not provided - in which case we don't need to do anything anyways!
		}
		doingWorldTimeStep = false;
		// TODO: verify
		
	}
	
	/**
	 * Print to the console the current state of the Critter world, 
	 * with all critters being represented by their toString()
	 */
	public static void displayWorld() {
		String top_and_bottom = "+";
		for(int i = 0; i < Params.world_width; i++) {
			top_and_bottom += "-";
		}
		top_and_bottom += "+";
		String[] rows = new String[Params.world_height];
		for(int i = 0; i < Params.world_height; i++) {
			for(int j = 0; j < Params.world_width; j++) {
				if(j == 0) {
					rows[i] = "|";
				}
				rows[i] += " ";
				if(j == Params.world_width - 1) {
					rows[i] += "|";
				}
			}
		}
		for(Critter crit : population) {
			rows[crit.y_coord] = rows[crit.y_coord].substring(0, crit.x_coord + 1) + crit.toString() + rows[crit.y_coord].substring(crit.x_coord + 2, rows[crit.y_coord].length());
		}
		System.out.println(top_and_bottom);
		for(String row : rows) {
			System.out.println(row);
		}
		System.out.println(top_and_bottom);
		// TODO: verify
	}
	

}
