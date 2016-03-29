#!/bin/bash

# This script tries to submit multiple blast jobs
if [ "$#" -ne 2 ]; then
    echo "Wrong number of arguments"
    echo "USAGE: masterScript.sh <properties_file> <unique_session_identifier>" 
    exit 1
fi


# Read command line arguments.
properties=${1}
identifier=${2}

# Check that no other session file exists with the same identifier.
if [ -d "session_$identifier" ]; then
	echo "a session file with the provided identifier already exists. Please supply a different one."
	exit 1
fi

sf="session_$identifier"
mkdir "$sf"

# Read the provided properties file
QUERY_DIR=`awk '/query_directory/{print $2;}' $properties`
ORGANISMS=`awk '/gene_map/{print $2;}' $properties`
DATABASE=`awk '/database/{print $2;}' $properties`
VO=`awk '/virtual_organization/{print $2;}' $properties`
[[ -z  $VO  ]] && VO="see"
isBBH=`awk '/isBBH/{print $2;}' $properties`
expand=`awk '/expand/{print $2;}' $properties`
[[ -z  $expand  ]] && expand="false"
timestamp=`awk '/timestamp/{print $2;}' $properties`
[[ -z  $timestamp  ]] && timestamp="timestamp.out"

cp scripts/*.sh $sf
cp scripts/*.py $sf
cp -r $QUERY_DIR $sf
cp $DATABASE $sf
if [ "$expand" = false ]; then
	rm "$sf/expand.sh"
fi

cd $sf
./submitAll.sh $DATABASE $QUERY_DIR $ORGANISMS $VO $isBBH
