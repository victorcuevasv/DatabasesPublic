
DATA_DIR=BigBench_1/base
DB_NAME=bigbench

echo ''
echo "PROCESSING FILES IN DIRECTORY $DATA_DIR"
echo ''

#Load the data into the tables by psql instructions

    for f in $DATA_DIR/* ; do
    	#The table has the same name as the file, minus the extension (unlike Derby it does not have to be in uppercase).
    	tableName=$(basename "$f")
    	#Remove the extension
		tableName="${tableName%.*}"
    	fFull=$(realpath "$f")
    	stmt="psql -d $DB_NAME -c \"COPY $tableName FROM '$fFull' delimiter '|' csv;\""
		eval $stmt
	done

echo ''
echo 'DONE' 
echo ''



