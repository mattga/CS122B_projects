# Edit Username and Password for Database here.
USER 	 = testuser
DBNAME   = moviedb

# Compile the Java Program
compile:
	javac -cp ./src:./mysql-connector-java-5.1.23-bin.jar -d ./bin src/Main.java

# Run the Java Program
run:
	java -cp ./bin/:./mysql-connector-java-5.1.23-bin.jar Main

# Delete the Compiled Program.
clean:
	rm ./bin/*

# Edit Variables at the TOP with actual values.
createdb:
	mysql -u $(USER) -p $(DBNAME) < createtable.sql

filldb:
	mysql -u $(USER) -p $(DBNAME) < data.sql
