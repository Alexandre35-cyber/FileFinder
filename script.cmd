rem lancement de l application
rem java -cp .\finder.jar;.\lib\commons-io-2.8.0.jar;.\lib\jdatepicker-2.0.3.jar org.maison.filefinder.ctrlr.Controller
cd src/main/java
find . -name "*.java" > sources.txt
export JAVA_HOME="D:/Applications/jdk-21.0.4"
javac --source 8 --source-path D:/Projets/JAVA/Covea/FileFinder/src/main/java @sources.txt -d D:/Projets/JAVA/Covea/FileFinder/target/classes -classpath "D:/Projets/JAVA/Covea/FileFinder/src/main/java;D:/Projets/JAVA/Covea/FileFinder/target/classes;D:\Users\amaison\.m2\repository\org\slf4j\slf4j-api\1.7.36\slf4j-api-1.7.36.jar;D:\Users\amaison\.m2\repository\org\codehaus\plexus\plexus-interpolation\1.26\plexus-interpolation-1.26.jar;D:\Users\amaison\.m2\repository\org\apache\maven\shared\maven-filtering\3.3.1\maven-filtering-3.3.1.jar;D:\Users\amaison\.m2\repository\javax\inject\javax.inject\1\javax.inject-1.jar;D:\Users\amaison\.m2\repository\org\sonatype\plexus\plexus-build-api\0.0.7\plexus-build-api-0.0.7.jar;D:\Users\amaison\.m2\repository\org\codehaus\plexus\plexus-io\3.4.1\plexus-io-3.4.1.jar;D:\Users\amaison\.m2\repository\org\codehaus\plexus\plexus-archiver\4.7.1\plexus-archiver-4.7.1.jar;D:\Users\amaison\.m2\repository\org\apache\commons\commons-compress\1.23.0\commons-compress-1.23.0.jar;D:\Users\amaison\.m2\repository\org\iq80\snappy\snappy\0.4\snappy-0.4.jar;D:\Users\amaison\.m2\repository\org\tukaani\xz\1.9\xz-1.9.jar;D:\Users\amaison\.m2\repository\com\github\luben\zstd-jni\1.5.5-2\zstd-jni-1.5.5-2.jar;D:\Users\amaison\.m2\repository\org\apache\maven\maven-archiver\3.6.0\maven-archiver-3.6.0.jar;D:\Users\amaison\.m2\repository\org\codehaus\plexus\plexus-utils\3.5.1\plexus-utils-3.5.1.jar;D:\Users\amaison\.m2\repository\javax\servlet\javax.servlet-api\3.1.0\javax.servlet-api-3.1.0.jar;D:\Users\amaison\.m2\repository\commons-io\commons-io\2.8.0\commons-io-2.8.0.jar;D:\Users\amaison\.m2\repository\junit\junit\4.11\junit-4.11.jar;D:\Users\amaison\.m2\repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar;D:\Users\amaison\.m2\repository\io\github\lzh0379\jdatepicker\2.0.3\jdatepicker-2.0.3.jar"
cp D:\Projets\JAVA\Covea\FileFinder\src\main\resources\config.properties D:/Projets/JAVA/Covea/FileFinder/target/classes
cp D:\Projets\JAVA\Covea\FileFinder\src\main\resources\log4j2.xml D:/Projets/JAVA/Covea/FileFinder/target/classes