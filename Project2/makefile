compile:
	javac -classpath "./WEB-INF/sources/:./WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar:./WEB-INF/lib/servlet-api.jar" WEB-INF/sources/*.java
	cp ./WEB-INF/sources/*.class ./WEB-INF/classes/
	javac -classpath "./WEB-INF/sources/:./WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar:./WEB-INF/lib/servlet-api.jar" WEB-INF/sources/Types/*.java
	cp ./WEB-INF/sources/Types/*.class ./WEB-INF/classes/Types/

war: compile
	jar -cvf ./_deploy/Project2.war ./META-INF/*.* ./WEB-INF/* ./*.*
	cp ./_deploy/Project2.war $$CATALINA_HOME/webapps/Project2.war

clean:
	rm -rf ./WEB-INF/sources/*.class ./WEB-INF/classes/*.class
