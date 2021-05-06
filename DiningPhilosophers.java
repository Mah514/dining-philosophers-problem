import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers
{
	//This default may be overridden from the command line
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;


	//Dining "iterations" per philosopher thread
	//while they are socializing there
	public static final int DINING_STEPS = 10;

	//shared monitor for the philosphers to consult
	public static Monitor soMonitor = null;

	public static void main(String[] argv)
	{
		try
		{
			int iPhilosophers;
			//We check if any arguments are passed from the command line. If so, 
			//we also check to see if they are valid and display an error message other wise.
			if (argv.length != 0)
			{
				iPhilosophers = Integer.parseInt(argv[0]);
				if (iPhilosophers < 0)
				{
					System.out.println(iPhilosophers + " is not a positive number decimal integer\nUsage: java DiningPhilosophers [NUMBER_OF_PHILOSOPHERS]");
					System.exit(0);
				}
			}
			else
			{
				iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;
			}
			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers);

			// Space for all the philosophers
			Philosopher aoPhilosophers[] = new Philosopher[iPhilosophers];

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				aoPhilosophers[j] = new Philosopher();
				aoPhilosophers[j].start();
			}

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);

			// Main waits for all its children to die...
			for(int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	}

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}
