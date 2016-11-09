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

# Delete all temporary files (including your load file) that your parser generated
rm *.dat
