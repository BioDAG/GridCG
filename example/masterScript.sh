#!/bin/bash
set -e

# This is the master script launching every job submission
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
if [[ -z $QUERY_DIR ]]; then
	echo "Please specify a query directory in the properties file"
	exit 1
fi
ORGANISMS=`awk '/gene_map/{print $2;}' $properties`
if [[ -z $ORGANISMS ]];	then
        echo "Please specify a gene map file in the properties file"
        exit 1
fi
DATABASE=`awk '/database/{print $2;}' $properties`
if [[ -z $DATABASE ]];	then
        echo "Please specify the database in the properties file"
        exit 1
fi
VO=`awk '/virtual_organization/{print $2;}' $properties`
[[ -z  $VO  ]] && VO="see"
isBBH=`awk '/isBBH/{print $2;}' $properties`
[[ -z $isBBH ]] && isBBH="false"
expand=`awk '/expand/{print $2;}' $properties`
[[ -z  $expand  ]] && expand="false"
timestamp=`awk '/timestamp/{print $2;}' $properties`
[[ -z  $timestamp  ]] && timestamp=timestamp_"$identifier.out"

cp $properties $sf
cp scripts/*.sh $sf
cp scripts/*.py $sf
cp -r $QUERY_DIR $sf
# Make sure there are no conflicts on the S.E when uploading the DB
cp $DATABASE "$sf/"
# Check if user specified a gene map file
if [ ! -f $ORGANISMS ]; then
	./"$sf"/createMapping.sh "$QUERY_DIR" "$sf"/Genome_"$identifier"
else
	cp $ORGANISMS "$sf"/Genome_"$identifier"
fi

if [ "$expand" = false ]; then
	rm "$sf/expand.sh"
fi
# Get basename of database file.
DATABASE=${DATABASE##*/}
cd $sf
mv $DATABASE "$DATABASE"_"$identifier"
rm createMapping.sh
nohup ./submitAll.sh "$DATABASE"_"$identifier" $QUERY_DIR Genome_"$identifier" $VO $isBBH $timestamp &
