#!/bin/bash

# Script responsible for resubmitting an aborted job.
# Assuming that jdl_directory is the current dir.

if [ "$#" -ne 2 ]; then
    echo "Wrong number of arguments"
    echo "USAGE: ./resubmitAborted <jdl_file> <job_number>" 
    exit 1
fi

jdl=$1
jobNumber=$2

status=$(glite-wms-job-status -i "JOBID/$jdl" | grep "Current")
if [[ ! $status == *"Aborted"* ]]; then
	echo "status is not aborted, i cannot move forward"
	exit 2
fi


