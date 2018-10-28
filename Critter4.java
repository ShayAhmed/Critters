package assignment4;
/**
 * This critter class cannot walk, but will fight everything including itself it sees and also reproduces 
 * 
 *  
 */
public class Critter4 extends Critter{
	private static int move_direction = Critter.getRandomInt(8); // doesn't include 8
	
	/**
	* <h1>doTimeStep</h1>
	* does time step of critter
	*<p>only fights and reproduces, does not walk
	*
	* @author  Shehryar Ahmed, 
	* @version 1.0
	* @since   2018-10-28 
	*/
	@Override
	public void doTimeStep() {
		if(getEnergy() > Params.min_reproduce_energy) {
			Critter offspring = new Critter1();
			reproduce(offspring, move_direction);
		}
		
	}
	/**
	* <h1>fight</h1>
	* will always fight
	*<p>
	*
	* @author  Shehryar Ahmed, 
	* @version 1.0
	* @since   2018-10-28 
	*/
	@Override
	public boolean fight(String oponent) {
		return true;
	}
	/**
	* <h1>toString</h1>
	* returns string of critter
	*<p>
	*
	* @author  Shehryar Ahmed, 
	* @version 1.0
	* @since   2018-10-28 
	*/
	@Override
	public String toString() {
		return "4";
	}
	/**
	* <h1>runStats</h1>
	* Prints critter stats
	*<p>prints population of critter
	*
	* @author  Shehryar Ahmed, 
	* @version 1.0
	* @since   2018-10-28 
	*/
	public static void runStats(java.util.List<Critter> population) {
		System.out.println("Population size: "+population.size());
		
	}
}
