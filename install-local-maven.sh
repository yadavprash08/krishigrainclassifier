#!/bin/bash
mvn install:install-file -DgroupId=com.prashant.java.krishi -Dversion=1.0 -Dpackaging=jar -DartifactId=Jama -Dfile=./lib/Jama-1.0.2.jar;
mvn install:install-file -DgroupId=com.prashant.java.krishi -Dversion=1.0 -Dpackaging=jar -DartifactId=ajt -Dfile=./lib/ajt-1.20.jar;
mvn install:install-file -DgroupId=com.prashant.java.krishi -Dversion=1.0 -Dpackaging=jar -DartifactId=commons-math -Dfile=./lib/commons-math-1.2.jar;
mvn install:install-file -DgroupId=com.prashant.java.krishi -Dversion=1.0 -Dpackaging=jar -DartifactId=javaml -Dfile=./lib/javaml-0.1.5.jar;
mvn install:install-file -DgroupId=com.prashant.java.krishi -Dversion=1.0 -Dpackaging=jar -DartifactId=libsvm -Dfile=./lib/libsvm.jar;
mvn install:install-file -DgroupId=com.prashant.java.krishi -Dversion=1.0 -Dpackaging=jar -DartifactId=weka -Dfile=./lib/weka.jar;
