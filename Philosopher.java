import BaseThread;

public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			System.out.println("Philosopher " + getTID() + " has started eating.");
			Thread.yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			Thread.yield();
			System.out.println("Philosopher " + getTID() + " has finished eating.");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		try
		{
			System.out.println("Philsopher " + getTID() + " has started thinking.");
			Thread.yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			Thread.yield();
			System.out.println("Philsopher " + getTID() + " has finished thinking.");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		try 
		{
			System.out.println("Philosopher " + getTID() + " has started talking.");
			Thread.yield();
			saySomething();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			Thread.yield();
			System.out.println("Philosopher " + getTID() + " has finished talking.");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.talk():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	//overridden Thread.run()
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			DiningPhilosophers.soMonitor.pickUp(getTID());

			eat();

			DiningPhilosophers.soMonitor.putDown(getTID());

			think();

            DiningPhilosophers.soMonitor.requestTalk();
            talk();
            DiningPhilosophers.soMonitor.endTalk();

			Thread.yield();
		}
	}

	/**
	 * Prints out a phrase from the array of phrases at random.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}
