compile:
	javac -classpath "./WEB-INF/sources/:./WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar:./WEB-INF/lib/servlet-api.jar" WEB-INF/sources/Types/*.java
	cp ./WEB-INF/sources/Types/*.class ./WEB-INF/classes/Types/
	javac -classpath "./WEB-INF/sources/:./WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar:./WEB-INF/lib/servlet-api.jar:./WEB-INF/lib/bonecp-0.8.0.RELEASE.jar:./WEB-INF/lib/slf4j-api-1.7.5.jar:./WEB-INF/lib/guava-16.0.jar" WEB-INF/sources/Helpers/*.java
	cp ./WEB-INF/sources/Helpers/*.class ./WEB-INF/classes/Helpers/
	javac -classpath "./WEB-INF/sources/:./WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar:./WEB-INF/lib/servlet-api.jar" WEB-INF/sources/*.java
	cp ./WEB-INF/sources/*.class ./WEB-INF/classes/

war: compile
	jar -cvf ./_deploy/Project2.war ./META-INF/*.* ./WEB-INF/* ./*.* ./js/* ./css/* ./_template/* ./fonts/* ./images/*
	cp ./_deploy/Project2.war $$CATALINA_HOME/webapps/Project2.war

clean:
	rm -rf ./WEB-INF/sources/*.class ./WEB-INF/classes/*.class
	rm -rf ./WEB-INF/sources/Helpers/*.class ./WEB-INF/classes/Helpers/*.class
	rm -rf ./WEB-INF/sources/Types/*.class ./WEB-INF/classes/Types/*.class

rundriver:
	java -cp "./WEB-INF/sources/:./WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar:./WEB-INF/lib/bonecp-0.8.0.RELEASE.jar:./WEB-INF/lib/slf4j-api-1.7.5.jar:./WEB-INF/lib/guava-16.0.jar" Driver