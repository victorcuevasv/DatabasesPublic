
$env:Path += ";C:\Program Files\PostgreSQL\9.4\bin"

$DATA_DIR="C:\Users\bscuser\workspace\DatabasesPublic\BigBenchQueries\BigBench_1\base"
$DB_NAME="bigbench"

echo ''
echo "PROCESSING FILES IN DIRECTORY $DATA_DIR"
echo ''

#Load the data into the tables by psql instructions
$files = Get-ChildItem $DATA_DIR
    for ($i=0; $i -lt $files.Count; $i++) {
    	$datafile = $files[$i].FullName
    	#The table has the same name as the file, minus the extension 
    	#(unlike Derby it does not have to be in uppercase). In PowerShell the
    	#extension is removed from the BaseName by default
    	$tableName = $files[$i].BaseName
    	$stmt = "psql -U postgres -d " + $DB_NAME + " -c `"COPY " + $tableName + " FROM `'" + $datafile + "`' delimiter `'|`' csv;`""     
    	#echo $stmt
		Invoke-Expression $stmt
	}

echo ''
echo 'DONE' 
echo ''



