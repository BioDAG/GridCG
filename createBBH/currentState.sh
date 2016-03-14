#!/bin/bash
#set -e

cd jdl_collection/JOBID
running=0
scheduled=0
cleared=0
aborted=0

for i in `ls`; do
	status=$(echo 2 | glite-wms-job-status -i $i | grep "Current")
	if [[ $status == *"Running"* ]]; then
		((running++))
	elif [[ $status = *"Scheduled"* ]]; then
		((scheduled++))
	elif [[ $status = *"Cleared"* ]]; then
		((cleared++))
	elif [[ $status = *"Aborted"* ]]; then
		((aborted++))
	else
		echo "$status"
	fi
done

sum=`expr $running + $scheduled + $cleared + $aborted`
cd ../..

echo "There are $running jobs running!"
echo "There are $scheduled jobs scheduled!"
echo "There are $cleared jobs cleared!"
echo "There are $aborted jobs aborted!"
echo "In summary i checked $sum jobs!"
