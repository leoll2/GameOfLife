mkdir build\classes
c:\prg\jdk8\bin\javac -d build\classes\ -classpath "c:\prg\libs\*" src\*.java
copy src\stile.css build\classes\
c:\prg\jdk8\bin\java -classpath "c:\prg\libs\*;build\classes" GiocoDellaVita