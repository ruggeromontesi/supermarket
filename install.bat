call git clone  https://github.com/ruggeromontesi/supermarket.git
cd  supermarket
call mvn clean install -f pom.xml
call mvn compile exec:java -Dexec.mainClass="org.reiz.App"  
