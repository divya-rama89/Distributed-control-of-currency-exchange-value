Homework 4 of Distributed Computing EEL6935, Spring 2014
Title: Distributed control of currency exchange value
Student Name: Divya Ramachandran

----------------------------------------------------------------------------------------------------------------------------------------
This code is designed to illustrate Lamport's algorithm to maintain consistency across replicas of data - sell rate, buy rate, maintained by different processes.
----------------------------------------------------------------------------------------------------------------------------------------
Instructions to run the code:
1. Untar folder DivyaRmachandran_hw4.tar using command: tar -xvf DivyaRamachandran_hw4.tar 
2. Go into the folder DivyaRamachandran_hw4 generated. Compile the code using command: make.
3. Run the code using command: java Currency <Process ID> <Number of operations> <Number of ticks>
4. Run the command of step 3 for each process.

(The code takes some time to run and produce results. In case it does not terminate Ctrl+C may be used.)

Example:java Currency 0 3 4

----------------------------------------------------------------------------------------------------------------------------------------
Code Description
	
Classes:
Client.java : Although the name is Client, it represents the process class. This contains all the methods and actions related to the processes.
Clock.java : This contains tick generation and logical clock adjustment codes.
Currency.java : This is the main code that takes user inputs and takes necessary actions and passes the correct parameters to create a client class.
KKMultiServerThread.java : Used for implementing a server that can connect to multiple clients.	
myCompare.java : This contains the code to sort the process queues as per the local clocks of the processes.
randomThread.java : This contains the code for random number generation to generate random sleep times for threads, random difference values for buy rate and sell rate.
randomQ.java : This contains the structure for maintaining the list of delta_x and delta_y values generated randomly.
queue.java : This contains the structure to hold the values to be updated held by each process.


The currency structure initialises a process by constructing a Client object. In the main body of the Client class, I call the different threads that must run concurrently. Different threads are as follows:
	Thread t3, t4, t5, t6: connection threads that create required sockets
	Thread chkAdd: checks whether random values are generated and accordingly updates queues
	Thread tsend: Checks for sending buffer empty flags and outputs the message stream when the socket does not already have a message
	Thread treadQ: Reads the queue in which incoming messages are saved and accordingly processes each message and takes actions.
        Thread endThread: Triggered a little later in the execution. This checks whether all buffers/queues are empty, all operations are complete. If all is done, it triggers the sending of a FINNISH message.

Threads chkAdd, tsend, treadQ are trigerred after communication is established amongst all processes.

Protocol for Communication:
Each string is separated by '.' operator except for the timestamp-dot operator is a part of it.
Start Message: begin (sent within connection codes)
	  UPDATE message: 1.PID.delta_x.delta_y.oprnID.clkCntr.timestamp
	  ACK message: 2.PID.originalSender.oprnID.timestamp
	  FIN message: 3.PID.FIN.timestamp 
Here, timestamp is the logical clock.
----------------------------------------------------------------------------------------------------------------------------------------

Disclaimer
For information about syntax and multithreading in JAVA, I have referred to online sources of information and examples illustrated on the Java online tutorials.
----------------------------------------------------------------------------------------------------------------------------------------
