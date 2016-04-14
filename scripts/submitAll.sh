#!/bin/bash

Submit(){
	# Keep trying to submit at all costs!
	num=$1
	jdl=$2
	# Temporarily use this endpoint for max job count.
	endpoint="https://wms01.afroditi.hellasgrid.gr:7443/glite_wms_wmproxy_server"
	submit_error="not empty"
	while [[ -n $submit_error ]]; do
		submit_error="" # Is this really necessary?
		submit_error=$(glite-wms-job-submit -o "JOBID/jobID_$num" --endpoint $endpoint -a $jdl 2>&1 | grep -A 1 "Error -")	
		if [[ -z $submit_error ]]; then
			echo "job $num submitted for the 1st time!"
			currentTime=$(date +"%Y-%m-%d %T")
			echo "submitted job $num at: $currentTime" >> "$timestamp"
		else
			echo $submit_error
		fi
		sleep 60
	done		
}

# This script tries to submit multiple blast jobs
if [ "$#" -ne 6 ]; then
    echo "Wrong number of arguments"
    echo "USAGE: submitAll.sh <database.fasta> <query_directory> <organisms.txt> <VO> <isBBH> <timestamp>" 
    exit 1
fi

DATABASE=$1
QUERY_DIR=$2
ORGANISMS=$3
VO=$4
isBBH=$5
timestamp=$6

javaDir="/home/steremma/Thesis/javaTools"
javac -d $javaDir/bin $javaDir/src/submit/*.java
MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
JDL_DIR=`java -cp $javaDir/bin submit/Submit $DATABASE $QUERY_DIR $ORGANISMS $VO $isBBH $MY_DIR`
echo $JDL_DIR

# Available greek storage elements for the biomed VO.
storage_elements=(
	se01.afroditi.hellasgrid.gr
	se01.ariagni.hellasgrid.gr
	se01.athena.hellasgrid.gr
	se01.grid.auth.gr
	se01.isabella.grnet.gr
	se01.marie.hellasgrid.gr
)

# Upload database to a SE.

lcg-del -a lfn:/grid/see/steremma/${DATABASE##*/} 2>/dev/null || true # Ignore error on this particular command
for j in ${storage_elements[@]}; do
  	lcg-cr -v -d $j -l lfn:/grid/see/steremma/${DATABASE##*/} file:$DATABASE >> log  2>&1
  	if [ $? == 0 ]; then
     	break;
  	fi
done

uploaded=$(grep guid -c log)
if [[ -z $uploaded ]]
then
	echo "file didn't upload"
	exit 3
fi

rm log

# Sort files from larger to smallest to optimize submission
cd "$QUERY_DIR"
SORTED_ARRAY=($(ls -l | awk '{print $5 , "\t" ,  $8}' | sort -n -r | awk '{print $2}'))
cd ..

# NOTE: submission will work only when called from inside the JDL_DIR.
# Thats why navigation (cd's) is needed.
cd $JDL_DIR
cp ../handleJob.sh ./

mkdir -p JOBID

declare -i jobNumber=$(ls JOBID/ | wc -l)
currentTime=$(date +"%Y-%m-%d %T")
echo "Beginning the submission at time: $currentTime" >> "$timestamp"
numJobs=$(ls | grep .jdl | wc -l)

for fasta in "${SORTED_ARRAY[@]}"; do
  file="$fasta.jdl"
  if [[ $file == *.jdl ]]; then
    let "jobNumber++"
    Submit $jobNumber $file	
    nohup ./handleJob.sh $file $jobNumber $numJobs $timestamp &
    sleep 3600 # Set me to 1 hour
  fi
done

cd ..
