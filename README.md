Online Grocery Delivery System

The Core Problem We Solved
Imagine running an online grocery store. You face two major logistical problems every day: first, you cannot afford to send out a delivery driver for a single $2 pack of gum. Second, you cannot guarantee a delivery slot to a customer if that exact timeslot was already snatched up by someone else.

This project serves as the automated "brain" solving those problems. We built a core Java engine that enforces strict business rules—ensuring every order meets a $10 minimum and every requested delivery slot is genuinely available. 

More importantly, we wrapped this logic inside a continuous integration pipeline. This means that anytime a developer updates the code, the system automatically checks it against your rules, packages it, and pushes it to a live Kubernetes cluster without any human error.

Project Structure
There are two main halves to this project: the Java core and the deployment pipeline.

The Java side lives in the src directory. 
App.java handles the core behavior. It contains the data models (Order and DeliverySlot) and the validation rules that enforce how they work.
AppTest.java proves that the code works. It uses simple JUnit 5 tests to verify the rules accept valid scenarios and correctly block invalid ones.
pom.xml manages the build so Maven can compile the code and run the tests automatically.

The deployment infrastructure is handled by the configuration files in the root folder.
Jenkinsfile controls our automated pipeline. It clones the repository, runs the tests, packages the app via Docker, and triggers the final Kubernetes release.
Dockerfile packages the compiled java application so it can run securely anywhere.
deployment.yaml handles the Kubernetes orchestration, dictating how many copies of the app should run and handling the health checks.

How to Build and Run Locally
If you want to run the tests locally to prove everything works before committing your code, simply open your terminal here and run:
mvn clean test

To package the code into an executable file:
mvn package

To deploy the latest container to Kubernetes: 
kubectl apply -f deployment.yaml

Adapting to New Scenarios
This application was designed so you only ever have to touch the Java classes when the scenario rules change. You do not need to touch the Dockerfile, Jenkinsfile, or deployment files. 

If the scenario changes, simply open App.java and swap out the objects to match the new problem (like replacing an Order with a DigitalWallet or a LibraryBook). Write three quick tests in AppTest.java to prove your new logic works, and push your code. Jenkins will automate the rest.
