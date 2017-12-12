#!/bin/sh
cd DirectoryService
mvn clean package
mvn exec:java -Dexec.mainClass="com.company.sorchanolan.DirectoryService" -Dexec.args=$1 &
cd ../
cd FileServer
mvn clean package
x=1234
for i in $(seq 1 $2)
do 
	x=$((x+1))
	mvn exec:java -Dexec.mainClass="com.company.sorchanolan.FileServer" -Dexec.args="$x $4 $1" &
done