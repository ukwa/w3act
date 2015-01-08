#!/bin/bash

# postgres setup:
# su - postgres -c "createuser --superuser --password training"
#su - postgres -c "createdb --owner=training --username=training w3act"
#su - postgres -c "psql -c 'grant all on database w3act to training'"
psql -d w3act -U training -f cleanupDB.sql
echo PostgreSQL schema clean up completed. 
