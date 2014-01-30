Project II - Read Me
================================

Authors: Matt Gardner, & Bladymir Tellez

## Compiling the Code:

On a \*nix compatible computers being there is a makefile
available that contains all the commands to compile and deploy 
the program.

`make compile` compiles the program and copies the appropriate class files to
their corrct locations.

`make war` compiles, and then creates a war file that is automatically
copied to the $CATALINA\_HOME\webapps location to be deployed by tomcat.

`make clean` is the command that will delete all `.class` files in teh WEB-INF directory and sub directories.


