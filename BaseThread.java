import java.util.Random;

public class BaseThread extends Thread
{
	public static int siNextTID = 1; //Preserves value across all instances

	//Our Thread ID
	protected int iTID;

	//Constructor
	public BaseThread()
	{
		setTID();
	}

	/**
	 * Assigns name to the thread and places it to the specified group
	 *
	 * @param poGroup ThreadGroup to add this thread to
	 * @param pstrName A string indicating human-readable thread's name
	 */
	public BaseThread(ThreadGroup poGroup, String pstrName)
	{
		super(poGroup, pstrName);
		setTID();
	}

	/**
	 * Sets user-specified TID
	 */
	public BaseThread(final int piTID)
	{
		this.iTID = piTID;
	}

	/**
	 * Retrieves our TID
	 * @return TID, integer
	 */
	public final int getTID()
	{
		return this.iTID;
	}

	/**
	 * Sets internal TID and updates next TID on contruction time, so it's private.
	 */
	private final void setTID()
	{
		this.iTID = siNextTID++;
	}
	
}


