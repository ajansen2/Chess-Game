# Chess
You will be working in a team environment on a large open-ended software project representing the development of a complex multifaceted GUI application.  In order to be successful in this software endeavour, your group must remain on track and stay focused throughout the semester.  This is no small task—do not let the project slip!

## Compile
To compile the project run:m

```
ant compile
```

## Testing (JUnit)
To test the model and generate a testing report run:

```
ant test
```

Then navigate to `report/html/index.html` with a web browser. 

```
open report/html/index.html
```

## Build a Distributable (JAR file)
To build an executable jar file, run:

```
ant jar
```


## Running

### Without Ant, Command line
To run the application via the portable jar file (this is by default created at `dist/Chess.jar` after building) run:

```
java -jar <jar file>
```

Example:

```
java -jar dist/Chess.jar # This is the default jar location after build
```
### With Ant
To run the application through `ant` (also performs application build if necessary), navigate
to the root of this repository and run:

```
ant
```

## JavaDoc
To create the backend (API-level) documentation run:

```
ant javadoc
```

Then navigate to `doc/index.html` with a web browser. 

```
open doc/index.html
```

## Cleaning
To clean created files from building, navigate to the root of this repository and run:

```
ant clean
```

## Software versions
- Adam is using JDK 17. Unless the labs are different, we should run JDK 17 so that we all have same language features.
- Dalton is using Junit 4.13 for testing.

# Problems!!!!

Due to the group focusing on images and stuff, I had to add a command line override that allows for me to override the AI strength drop down.

**DO NOT REMOVE THIS OVERRIDE** Or I will be very displeased.

To use with Java (after running ant):
```shell
java -jar dist/Chess.jar white <number of plys> black <number of plys>
```

You need to pass the arguments `white` and/or `black` followed by a number of levels to search. I have added a run script (`override.sh`) that will launch with the levels from the command line.

You can also run from within ant using:
```shell
ant run -Dards="white 3 black 4"
```