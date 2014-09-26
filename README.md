Infection
=========

A graphing program that involves two sets of users, coaches and students, and causes a state of "infection" that may spread to other users.

Setup
----
To immediately begin using Infection, you will need to compile the java files.  Please ensure you have the default JDK installed.  I have already included a makefile for you to use. To compile, do one of the following:
  Using the makefile:
```
make
```
  OR, compiling each java file individually:
```
javac User.java
javac World.java
javac Scene.java
```

Run
----
There is a standard way for running this program. So one does not have to go into the code to see it in action, several parameters are available for the user to use. 
Firstly, the default way to run is:
```
java Scene
```
The default settings used are:
* use totalInfection function
* target user 'Nikhil'
* pull users from 'tests/default.xml'

To customize what users are created, some sample xml files can be found in the 'tests/' folder. Each xml file also comes with comments about what one might test with it.

To customize what actions to take, one should take into consideration the following format:
```
java Scene <filename> <username> <command> <limit_infection_value>
```
* **filename**: the xml file the program should gather its data from
* **username**: the user to target with total infection or limit infection. This value ignores case, so if you put 'KHANACADEMY', it is no different than 'khanacademy'.
* **command**: 'limit' is the only meaningful argument for the third parameter which results in limit infection. Anything else results in a total infection.
* **limit_infection_value**: if 'limit' was chosen as the third argument, then this value is used to set a soft maximum for the number of infections that occur in limit infection. This defaults to 0 if nothing is provided

Here are some examples of how to use this program.
This will infect user 'Khan' using the total infection, using data from the file total_chain.xml.
```
java Scene tests/total_chain.xml khan
```
This will infect user 'Nikhil' using limit infection and only allowing a soft max of 5 infections. The file used is total_separate_components.xml.
```
java Scene tests/total_separate_components.xml nikhil limit 5
```

Additional Features
----
Within the **tests/** folder, there is an xml_creator.py.  This file can be used to generate random xml files with passed in number of users and a random set of edges (student-coach relationships). To use this file, make sure you're current working directory is under the tests/ folder and use the following format:
```
python xml_creator.py <number_of_users>
```
For example, to create 500 users with randomized relationships, one would use the following statement:
```
python xml_creator.py 500
```
