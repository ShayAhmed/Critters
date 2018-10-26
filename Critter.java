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
	private int version;
	private static int global_version = 0;
	
	private static boolean determining_encounters;
	
	protected final void walk(int direction) {
		
		if (this.version != global_version) {
		this.x_coord = newX(this.x_coord,direction,1);
		this.y_coord = newX(this.y_coord,direction,1);
		this.version = global_version;
		}
		
		this.energy -= Params.walk_energy_cost;
	}
	
//	private final void updatecoord() {
//		
//	}
	
	
	protected final void run(int direction) {
		if(this.version != global_version) {
		this.x_coord = newX(this.x_coord,direction,2);
		this.y_coord = newX(this.y_coord,direction,2);
		this.version = global_version;
		}
		this.energy -= Params.run_energy_cost;
		// TODO: verify/debug
	}
	
	private final int newX(int current_x,int direction,int numstepstodo) {
		if(direction == 7 || direction < 2) {
			current_x+=numstepstodo;
		}
		else if(direction > 2 && direction < 6) {
			current_x-=numstepstodo;
		}
		
		current_x = current_x % Params.world_width;
		if(current_x < 0) {
			current_x = Params.world_width - 1;
		}
		return current_x;
	}
	private final int newY(int current_y,int direction,int numstepstodo) {
		if(direction == 7 || direction < 2) {
			current_y+=numstepstodo;
		}
		else if(direction > 2 && direction < 6) {
			current_y-=numstepstodo;
		}
		current_y = current_y % Params.world_height;
		if(current_y < 0) {
			current_y = Params.world_height - 1;
		}
		return current_y;
	}
	
	private final Critter look(int direction, int num_steps) {
		for (Critter crit:population) {
			if (crit.x_coord == newX(this.x_coord,direction,num_steps) &&crit.y_coord == newX(this.y_coord,direction,num_steps)) {
				return crit;
			}
		}
		return null;
	}
	
	private final Critter check_encounter() {
		for (Critter crit:population) {
			if (crit.x_coord == this.x_coord &&crit.y_coord == this.y_coord &&crit.energy>0 && crit != this) {
				return crit;
			}

		}
		return null;
	}
	
	protected final void reproduce(Critter offspring, int direction) {
		if(this.energy < Params.min_reproduce_energy) {
			return;
		}
		offspring.x_coord = newX(offspring.x_coord,direction, 1);
		offspring.y_coord = newX(offspring.y_coord,direction, 1);
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
			created_critter.x_coord = Critter.getRandomInt(Params.world_width - 1);
			created_critter.y_coord = Critter.getRandomInt(Params.world_height - 1);
			created_critter.version = global_version;
			population.add(created_critter);  // Will this even work? It may not be specific enough for our needs
		}
		catch(Exception e) {
			throw new InvalidCritterException(critter_class_name); // This may be too general; do we blame the class name for all problems?
		}
		// TODO: verify/debug
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
		// TODO: is this final? Do we need to implement anything more for this method?
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

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population = new java.util.ArrayList<Critter>();
		babies = new java.util.ArrayList<Critter>();
	}
	
	public static void worldTimeStep() {
		global_version++;
	    for(Critter crit : population) {
	    	crit.doTimeStep(); // theoretically enough?
	    }
	    
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
					temp.energy += crit.energy/2;
					crit.energy = 0;	
				}
				else {
					crit.energy += temp.energy/2;
					temp.energy = 0;	
				}
				if(crit.energy>0) {
					temp = crit.check_encounter();
				}
				
			}
			crit.energy -= Params.rest_energy_cost;
		}
		
		Iterator it = population.iterator();
		while(it.hasNext()) {
			Critter crit = (Critter) it.next();
			if (crit.energy<1) {
				it.remove();
			}
		}
		population.addAll(babies);
		babies = new java.util.ArrayList<Critter>();
		for(int i = 0; i < Params.refresh_algae_count; i++) {
			try {
				Critter.makeCritter("Algae");
			}
			catch (Exception e) {
				// we are assuming that algae is to be included, so we need not do anything here
			}
		}
		
	}
	
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
		
		// Complete this method.
		// TODO: debug!
	}
	

}
