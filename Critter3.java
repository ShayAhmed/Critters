package assignment4;
/**
 * This critter class only eats itself, this includes its children. Outside of that, it shares properties with other critter. 
 * For example, it can reproduce and walk in all directions  
 *  
 */
public class Critter3 extends Critter {
	
	private static int move_direction = Critter.getRandomInt(8); // doesn't include 8
	/**
	* <h1>doTimeStep</h1>
	* does time step of critter
	*<p>does reproduction and walking for critter
	*
	* @author  Shehryar Ahmed, 
	* @version 1.0
	* @since   2018-10-28 
	*/
	@Override
	public void doTimeStep() {
		if(getEnergy() > Params.walk_energy_cost + Params.rest_energy_cost) {
			walk(move_direction);
		}
		if(getEnergy() > Params.min_reproduce_energy) {
			Critter offspring = new Critter1();
			reproduce(offspring, move_direction);
		}
		
		
	}
	/**
	* <h1>fight</h1>
	* will always fight itself, not others
	*<p>
	*
	* @author  Shehryar Ahmed, 
	* @version 1.0
	* @since   2018-10-28 
	*/
	@Override
	public boolean fight(String oponent) {
		if(oponent.equals("3")) { //fight its own
			return true;
			}
		return false;
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
		return "3";
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
