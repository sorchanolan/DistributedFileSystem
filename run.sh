#!/bin/sh
cd DirectoryService
mvn clean package
mvn exec:java -Dexec.mainClass="com.company.sorchanolan.DirectoryServiceMain" -Dexec.args=$1 &
cd ../
cd FileServer
mvn clean package
x=$3
for i in $(seq 1 $2)
do 
	mvn exec:java -Dexec.mainClass="com.company.sorchanolan.FileServerMain" -Dexec.args="$x $4 $1" &
	x=$((x+1))
done
cd ../
cd ClientProxy
mvn clean package
mvn exec:java -Dexec.mainClass="com.company.sorchanolan.ClientMain" -Dexec.args="$4 $1 $5" &