mkdir build\classes
c:\prg\jdk8\bin\javac -d build\classes\ -classpath "c:\prg\libs\*" src\EventoUtilizzoGUI.java src\LogServerEventiUtilizzoGUI.java
start cmd /k "c:\prg\jdk8\bin\java -classpath "c:\prg\libs\*;build\classes" LogServerEventiUtilizzoGUI"