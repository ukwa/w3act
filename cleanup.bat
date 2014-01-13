REM This script removes all generated targets in W3ACT in order to clean up the database
rmdir /S /Q target project\target project\project\target
echo Project generated target folders clean up completed. 

REM replace psql path, database name (-d), host (-h), port (-p) and user (-U) if needed
D:\install\PostgreSQL\9.3\bin\psql -d w3act -h localhost -p 5432 -U training -f cleanupDB.sql
echo PostgreSQL schema clean up completed. 

