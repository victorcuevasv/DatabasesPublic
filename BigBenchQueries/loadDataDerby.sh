#!/bin/bash

url="'jdbc:derby:testbbdb;create=true';"
work_fileCreate=workCreate.sql
work_fileLoad=workLoad.sql

#First create a .sql file that establishes the connection
echo ''
rm $work_fileCreate
echo "CONNECT $url" >> $work_fileCreate
echo ''

#Add to the file with the CONNECT statement all of the CREATE TABLE statements read from the
#predefined script

createTablesScript=createTablesBB.sql

cat $createTablesScript >> $work_fileCreate

#Execute the script to connect to the database and create the tables

#ij $work_fileCreate

#Create a new script with the load data statements that also first establishes the connection.
#Add one statement per data file found in the folder

rm $work_fileLoad

echo "CONNECT $url" >> $work_fileLoad

DATA_DIR=LoadBB

echo ''
echo "PROCESSING FILES IN DIRECTORY $DATA_DIR"
echo ''

#Load the data into the tables by a generated script
load_file=load_tables.sql
echo "connect $url" > $load_file 
    for f in $DATA_DIR/* ; do
    	#The table has the same name as the file, minus the extension and it must be in uppercase
    	tableName=$(basename "$f")
    	#Remove the extension
		tableName="${tableName%.*}"
		#Convert table name to uppercase
		tableName=${tableName^^}
		echo $tableName
    	echo "$f"
    	echo ''
    	fFull=$(realpath "$f")
    	stmt="CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE (NULL,'$tableName','$fFull','|','\"',NULL,0);"
		echo $stmt
		echo $stmt >> $load_file
		echo ''
	done

#Execute the .sql file with the connection and the load data statements

#ij $load_file

echo 'DONE' 



