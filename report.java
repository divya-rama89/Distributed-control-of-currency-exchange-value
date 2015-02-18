1. Some of the data such as the local queues of the processes had update operations from different parts of the code. However, they were to be accessed only by a single thread or method at a time. Running them without considering this would creates inconsistencies which is also reported by JAVA as an exception. These blocks of code were to be wrapped using the 'synchronized' keyword due to which the data access remains atomic.
2. Similarly, certain methods that were called multiple times had to be tagged with the synchronized keyword to ensure that they were accessed by 1 caller at a time.
3. Synchronisation became all the more important since all the threads had to communicate using only common blocks of memory unlike where we can pass parameters by value.
4. Data was being continuosly sent or received and hence collecting variables were overidden. Then I designed queues to handle all the incoming, outgoing data as well as rapidly generated data.
5.  No message was going from client to server. Had to trace the message path from the client to server until the correct path was traced. Used Eclipse debug to trace it and to ensure that the correct value was passed.
6.  Certain operations had to be carried out concurrently. They had to be distibuted in threads. To decide what operation to be put in threads and to distribute them such that they will be able to run concurrently and it will be beneficial took considerable thought and many versions of code.
7.  Certain threads had to begin only after some other threads were completed. Trying to set a flag and poll the flag was one technique. However, I lerant that threads have a joint() method that helps in making a thread wait until another thread terminates. To make threads run serially where necessary, I made use of join.
8.  Due to testing on the same system, the port numbers corresponding to each process in info.txt had to be mapped to different ports. Due to overlooking this earlier, the connection was not possible.
9. In order to create the date and time in the log file in a specific format, some online help was required.
10. To maintain data records, different class structures needed to be created. Initial trials with inherently available structures such as a 2D integer array proved to be cumbersome.
11. Since multiple threads are running, debugging conventionallt using breakpoints failed leaving the option of using debug messages.
12. Certain threads did not seemed to access the wrong variables. In order to ensure the right access, thread sleep routines were added and the current object instance was passed.
13. Sometimes data would take very long to be visible which was later mapped to too much waiting due to synchronisation. Code was thorughly checked to add synchronisation only where absolutely required.
14. Some of the difficulties were due to the complication of the algorithm. Certain parts of the algorithm were simplified- Instead of updating its values after sending an acknowledge, the process would update after seeing the ack message from the third process. 
15. A major difficulty faced was that sometimes just removing the debug line would stop the execution of the message. This meant that the threads needed to sleep i.e. the time taken to print had to be replaced with sleep. Judging the amount of time they should sleep was a trial and error method.
16. For large values, the performance is not very good for my implementation. It works well for small values.



