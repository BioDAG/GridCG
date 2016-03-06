#!/bin/bash

# This script tries to submit multiple blast jobs
if [ "$#" -ne 3 ]; then
    echo "Wrong number of arguments"
    echo "USAGE: submitAll.sh <database.fasta> <query_directory> <organisms.txt>" 
    exit 1
fi

DATABASE=$1
QUERY_DIR=$2
ORGANISMS=$3

javac blastUtils/*.java
java blastUtils/Main $DATABASE $QUERY_DIR $ORGANISMS
# Assuming default jdl directory in Java Main code.
JDL_DIR="jdl_collection"
echo $JDL_DIR


# NOTE: submission will work only when called from inside the JDL_DIR.
# Thats why navigation (cd's) is needed.
cd $JDL_DIR

for file in `ls`; do
  if [[ $file == *.jdl ]]; then
    glite-wms-job-submit -o jobID -a $file
    #glite-wms-job-submit -a -o jobID --endpoint https://wms01.afroditi.hellasgrid.gr:7443/glite_wms_wmproxy_server $file
  fi
done

cd ..
