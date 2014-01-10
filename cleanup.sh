#!/bin/bash

rm -rf target/ project/target/ project/project/target/ conf/evolutions/
echo Project generated folders clean up completed. 

psql -d w3act -U postgres -f cleanDB.sql
su - postgres -c "dropdb w3act"
su - postgres -c "createdb --owner=training --username=training w3act"
su - postgres -c "psql -c 'grant all on database w3act to training'"
echo PostgreSQL schema clean up completed. 
