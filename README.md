# dining-philosophers-problem
This is a solution to an extended version of the classical synchronization problem - The Dining Philosophers. The extension refers to the fact that
sometimes philosophers would like to talk, but only one (any) philosopher can be talking at a time while they are not eating.

The built monitor is a deadlock and starvation-free implementation that uses Java’s synchronization primitives, such as wait() and notifyAll().

A philosopher requires to pickup two chopsticks to eat and can only do so if they are both available and the philosopher's state is hungry. In addition, If a given philosopher has decided to make a statement, they can only do so if no one is speaking at the given moment. The philosopher wishing to make the statement has to wait otherwise.
