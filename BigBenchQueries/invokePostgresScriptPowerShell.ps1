$env:Path += ";C:\Program Files\PostgreSQL\9.4\bin"
$DB_NAME="bigbench"
$SCRIPT_FILE="createTablesPostgresBB.sql"
$stmt = "psql -U postgres -d " + $DB_NAME + " -a -f " + $SCRIPT_FILE
Invoke-Expression $stmt
