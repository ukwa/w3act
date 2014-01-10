#!/bin/bash

rm -rf target/ project/target/ project/project/target/ 
echo Project generated target folders clean up completed. 

# replace psql path, database name (-d), host (-h), port (-p) and user (-U) if needed
#psql -d w3act -h localhost -p 5432 -U training -f cleanupDB.sql
#echo PostgreSQL schema clean up completed. 
