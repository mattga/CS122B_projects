# Project 1

## Team \#03
- Matthew Gardner (mdgardne@uci.edu)
- Bladymir Tellez (tellezb@uci.edu)


##Directories
- `src` contains the source code for the Java program.
- `bin` contains the compiled Java program.


## Files
- `createtable.sql` contains the queries to create the database, and tables required.
- `data.sql` contains the test data for the database
- `makefile` contains building commands for easily executing common tasks.


## Makefile Commands
The make file make use of 2 variables, `USER` and `DBNAME`, update the variables inside the file before making use of the `.*db`-commands.

- `compile` - compiles the Java program and drops it into the `bin` directory.
- `run` - runs the program inside the `bin` directory.
- `clean` - deletes the compiles program inside `bin`.
- `createdb` - feeds the `createtable.sql` file into MySQL to be executed.
- `filldb` - feeds the `data.sql` file into MySQL to be executed.


## Compiling the Program
### Option 1
Import the Project into Eclipse and Run.

### Option 2
**Open** a Unix compatible terminal window.

**Compile the program** by entering `make compile` into the terminal.


## Running the Program
**Open** `makefile` inside a text editor, and update the `USER` and `DBNAME` variables, then save and close the file.

**Open** a Unix compatible terminal window.

*At this point the program should be compiled, else see compilation notes above.*

**Create and Populate the database** by entering `make createdb` followed by `make filldb`, each time entering your password to the database as requested into the terminal.

**Run the program** by entering `make run` into the terminal.

## Raw Commands to Compile and Run Program, and Create and Populate the Database. 
	# Create the database Tables
	mysql -u USER -p moviedb < createtable.sql
	
	# Populate the datbase tables.
	mysql -u USER -p moviedb < data.sql
	
	# Compile the Java Program
	javac -cp ./src:./mysql-connector-java-5.1.23-bin.jar -d ./bin src/Main.java

	# Run the program after its been compiled.
	java -cp ./bin/:./mysql-connector-java-5.1.23-bin.jar Main

	
