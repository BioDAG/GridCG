#!/bin/bash
#set -e

cd jdl_collection/JOBID
running=0
scheduled=0
cleared=0
aborted=0
unknown=0
sum=0
for i in `ls`; do
	((sum++))
	status=$(echo 2 | glite-wms-job-status -i $i | grep "Current")
	if [[ $status == *"Running"* ]]; then
		((running++))
	elif [[ $status = *"Scheduled"* ]]; then
		((scheduled++))
	elif [[ $status = *"Cleared"* ]]; then
		((cleared++))
	elif [[ $status = *"Aborted"* ]]; then
		((aborted++))
		echo "job $i was aborted"
	else
		status=$(echo 2 | glite-wms-job-status -i $i | grep "purged")
		if [[ ! -z $status ]]; then
			((cleared++))
		else 
			((other++))
		fi
	fi
done

cd ../..

echo "There are $running jobs running!"
echo "There are $scheduled jobs scheduled!"
echo "There are $cleared jobs cleared!"
echo "There are $aborted jobs aborted!"
echo "There are $other jobs with unknown status!"
echo "There are $sum jdl files in the JOBID/ directory!"
