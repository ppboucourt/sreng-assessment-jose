#Solution
#Steps
1- New orders to be automatically pulled in from your marketplace
2- Process each order checking if are fulfillable 
3- Then decide whether or not the order is fulfillable

#Classes Design
In the package **\domain\ I put the classes that give solution to the problem.
A part of store the orders also I choose to store the Inventory and ProductStock,
to keep track of the processed orders. I was trying to not modify the entities that were
provided and processing the information from the first end-point (getOrder).
In the \assessment\AssessmentController.java I keep the logic of our Api. I choose keep it
in the same file for easy access to the methods I had to create.

The process of Allocate Inventory is called by each Order that has no have been Fulfilled.
Based on the description, I choose to keep a way of process again the orders that were not
fullfilled. Based on the field **fullfilled** of the class **Inventory** I keep calling to
those orders that need to be fullfilled.
In my opinion, using in production a service as AWS SQS, could be better to be sure of
process the orders when is needed and not every 10, 5 or any second of the time we choose.

Other approach that we could consider is develop a Microservice Architecture, if is know
the system is going to process big data of orders. Split the Api into microservice could 
better to handle the process of the 3PL API, the slow one. One microservice can handle
part and not overload the entire Api.

Create container, CI/CD tools and IaaS configration, are the few of the best tools to automate
the build process of this Api.
 
 
 