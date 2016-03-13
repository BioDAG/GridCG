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
		# submit_error=$(glite-wms-job-submit -o "JOBID/jobID_$num" -a $jdl 2>&1 | grep "Error -")
		submit_error=$(glite-wms-job-submit -o "JOBID/jobID_$num" --endpoint $endpoint -a $jdl 2>&1 | grep "Error -")		
		if [[ -z $submit_error ]]; then
			echo "job $num submitted for the 1st time!"
			currentTime=$(date +"%Y-%m-%d %T")
			echo "submitted job $num at: $currentTime" >> ../timestamp.out
		fi
		sleep 60
	done		
}

# This script tries to submit multiple blast jobs
if [ "$#" -ne 4 ]; then
    echo "Wrong number of arguments"
    echo "USAGE: submitAll.sh <database.fasta> <query_directory> <organisms.txt> <VO>" 
    exit 1
fi

DATABASE=$1
QUERY_DIR=$2
ORGANISMS=$3
VO=$4

javac javaTools/submit/*.java
java javaTools/submit/Submit $DATABASE $QUERY_DIR $ORGANISMS $VO

# Assuming default jdl directory in Java code.
JDL_DIR="jdl_collection"
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
lcg-del -a lfn:/grid/see/steremma/$DATABASE 
for j in ${storage_elements[@]}; do
  	lcg-cr -v -d $j -l lfn:/grid/see/steremma/$DATABASE file:$DATABASE > log  2>&1
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

# NOTE: submission will work only when called from inside the JDL_DIR.
# Thats why navigation (cd's) is needed.
cp handleJob.sh $JDL_DIR
cd $JDL_DIR
mkdir JOBID
declare -i jobNumber=1
for file in `ls`; do
  if [[ $file == *.jdl ]]; then
    Submit $jobNumber $file	
    nohup ./handleJob.sh $file $jobNumber &
    let "jobNumber++"
    sleep 1000
  fi
done

cd ..
