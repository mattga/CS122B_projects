compile:
	javac -cp ./WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar:./WEB-INF/lib/servlet-api.jar:WEB-INF/sources/ 
	cp ./WEB-INF/sources/*.class ./classes/

war: compile
	jar -cvf ./_deploy/Project2.war ./classes/*.* ./META-INF/*.* ./WEB-INF/*.* index.jsp

clean:
	rm -rf ./WEB-INF/sources/*.class ./WEB-INF/classes/*.class