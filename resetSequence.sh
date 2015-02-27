#!/bin/bash

psql -d w3act2 -U training -f resetIdSequence.sql
echo PostgreSQL resetIdSequence completed. 
