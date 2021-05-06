import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor
{
	enum STATE {THINKING, HUNGRY, EATING};
	private STATE state[];
	private Condition[] self;
	private Lock key;
	private int numOfChopsticks;

	private boolean someoneTalking;

	//Constructor
	public Monitor(int piNumberOfPhilosophers)
	{
		// Init
		numOfChopsticks = piNumberOfPhilosophers; // Should be equal # of philosophers and chopsticks
		state = new STATE[numOfChopsticks];
		self = new Condition[numOfChopsticks];
		someoneTalking = false;

		// We init our philosophers' conditions and self
		initializeLocks();
		initializeStates();
	}

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		// We adjust it into index starting 0
		int i = piTID - 1;

		key.lock();
		//System.out.println("pickUp: Philosopher " + (i+1) + " locked.\n");
							// Critical section
		try {
			// Trigger hungry state and attempt to eat
			state[i] = STATE.HUNGRY;
			//System.out.println("Philosopher " + (i+1) + " is hungry.\n");
			test(i);
			
			// If could not eat with two chopsticks, then wait
			if (state[i] != STATE.EATING) {
				//System.out.println("Philosopher " + (i+1) + " waiting.\n");
				self[i].await(); //Enters a FIFO queue provided by Java Runtime Environment
			}
		}
							// End critical section
		catch (InterruptedException e)  {
			System.err.println("Monitor.pickUp():");
			DiningPhilosophers.reportException(e);
		}
		finally {
			key.unlock();
			//System.out.println("pickUp: Philosopher " + (i+1) + " unlocked.\n");
			putDown(piTID);
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		// We adjust it into index starting 0
		int i = piTID - 1;

		key.lock();
		//System.out.println("putDown: Philosopher " + (i+1) + " locked.\n");
							// Critical section
		
		// Put down chopsticks and return to thinking
		state[i] = STATE.THINKING;
		//System.out.println("Philosopher " + (i+1) + " is thinking.\n");
		self[i].signalAll(); //Signals all waiting threads. Threads are picked by FIFO queue by Java Runtime Environment
		
		// We let hungry neighbors know this philosopher's chopsticks are down
		test((i + (numOfChopsticks-1)) % numOfChopsticks);
		test((i + 1) % numOfChopsticks);

							// End critical section
		key.unlock();
		//System.out.println("putDown: Philosopher " + (i+1) + " unlocked.\n");
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		try {
			// wait until someone stops talking
			if (someoneTalking) {
				wait();
			}

			// they can now speak
			someoneTalking = true;
		}
		catch (InterruptedException e)  {
			System.err.println("Monitor.requestTalk():");
			DiningPhilosophers.reportException(e);
		}
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		someoneTalking = false;
		notify();
	}

	// Method that initializes ReentrantLock and condition variables (representing
	// the conditions the threads are waiting for) to signal the thread whose
	// turn is next.
	private void initializeLocks()
	{
		key = new ReentrantLock();
		for(int i = 0; i < numOfChopsticks; i++)
		{
			self[i] = key.newCondition();
		}
	}

	// We initialize our philosopher's state to thinking as the starting point 
	// before getting hungry and eating.
	private void initializeStates()
	{
		for(int i = 0; i < numOfChopsticks; i++)
		{
			state[i] = STATE.THINKING;
		}
	}

	// Check if conditions are met for this philosopher to eat accordingly. If 
	// so, philosopher will start eating and then releases lock.
	private void test(int i) 
	{
		if (
			state[i] == STATE.HUNGRY 												// if this philosopher is hungry
			&& state[(i + (numOfChopsticks-1)) % numOfChopsticks] != STATE.EATING 	// if left neighbor doesn't has chopstick
			&& state[(i + 1) % numOfChopsticks] != STATE.EATING					  	// if right neighbor doesn't has chopstick
		) {
			// If hungry and have both chopsticks, then eat
			state[i] = STATE.EATING;
			//System.out.println("Philosopher " + (i+1) + " is eating.\n");
		}
	}
}

