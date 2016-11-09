#!/bin/bash

#Drop any existing relevant tables in CS144 database IF EXISTS.
mysql CS144 < drop.sql

# Create all the relevant tables in CS144 database
mysql CS144 < create.sql

# Build and run your parser to generate fresh load files
ant
ant run-all

# Load the data into MySQL
mysql CS144 < load.sql

#mysql CS144 < dropSQLIndex.sql
#mysql CS144 < buildSQLIndex.sql

# Delete all temporary files (including your load file) that your parser generated
#rm *.dat
