#!/bin/bash 

#-- HandleSingleJob.sh -------------------------------------------------------------------- 
# 
# This script handles a single job or a single job collection
# 
# Arguments:
# 1. The jobid
# 2. The job jdl file or the collection job folder
# 3. "s" if it is one simple job or "c" if it is a collection
#
#------------------------------------------------------------------------------------------


jdl=${1}
jobNumber=${2}
jobType="s"


################################################################################
#                                                                              #
#                               functions                                      #
#                                                                              #
################################################################################

Unzip(){
# takes the name of the zipped file or folder as an input and decompresses it into the outFolder.
        folder=${1}
        outFolder=${2}
        mkdir tmpdir
        mv $folder tmpdir
        cd tmpdir
        tar -zxvf $folder
        rm $folder
        dataFolder=$(ls)
        mv $dataFolder ../$outFolder/
        cd ..
        rm -rf tmpdir
}

Time(){
# get the time difference of the two timestamps
        timestamp1=${1}
        timestamp2=${2}
        t=$(date -d "$timestamp1" +%s)
        t1=$(date -d "$timestamp2" +%s)
        diff=$(expr $t1 - $t)

}

CancelAndResubmit(){
	echo "i will cancel job $jobNumber"
	echo "y" | glite-wms-job-cancel -i JOBID/jobID_$jobNumber >> /dev/null 2>&1
	rm JOBID/jobID_$jobNumber
	# Find out the job number in jobID.
	echo "submitting the job $jobNumber again"
	if [[ $jobType == "s" ]]; then
		error=$(glite-wms-job-submit -o "JOBID/jobID_$jobNumber" -a $jdl 2>&1 | grep "Error -")
	fi
     
	if [[ -z $error ]]; then
        	echo "New job submitted"
		return 0
	else
		echo "error when resubmitting: $error"
		return 1
	fi
}

################################################################################
#                                                                              #
#                            Control the job flow                              #
#                                                                              #
################################################################################

sf="SessionFolder"
run="true"
fallAsleep=0
sameJobRuns=0
getOutput="false"
echo "......."
sleep 60
startTime=$(date +"%Y%m%d %T")

# run until job is done
while $run; do

	# get the jobs status
	if [[ $jobType == "s" ]]; then
		currentStatus=$(glite-wms-job-status -i JOBID/jobID_$jobNumber 2>&1 | grep "Current Status" | cut -d":" -f 2 | tr -d ' ')
	elif [[ $jobType == "c" ]]; then
		currentStatus=$(glite-wms-job-status -i $jobID 2>&1 | grep "Current Status" -m 1 | cut -d":" -f 2 | tr -d ' ')
	fi

	timeNow=$(date +"%Y%m%d %T")
	# get time difference
	Time "$startTime" "$timeNow"
	if [[ $diff -gt 7200 ]]; then # 2 hours 
		# check if scheduled and if yes, cancel and resubmit
		if [[ $currentStatus == "Scheduled" ]]; then
			CancelAndResubmit
			if [[ $? != 0 ]]; then
				# if submit failed, try one more time
				CancelAndResubmit
			fi
		fi
	fi

	# check the status
	if [[ $currentStatus == "Running" ]]; then
		echo "Job $jobNumber is Running, wait for it to finish"
		sleep 600
	elif [[ $currentStatus == "Done(Success)" ]]; then
		echo "job $jobNumber is Done, get the output"
		getOutput="true"
		run="false"
	elif [[ $currentStatus == "Aborted" ]]; then
		#check again
		if [[ $jobType == "s" ]]; then                                                             
			currentStatus=$(glite-wms-job-status -i JOBID/jobID_$jobNumber 2>/dev/null | grep "Current Status" | cut -d":" -f 2 | tr -d ' ')
        fi
		if [[ $currentStatus == "Aborted" ]]; then 
			echo "Job Aborted"
			run="false"
			exit 5
		fi
	elif [[ $currentStatus == "Cancelled" ||  $currentStatus == "Done(Exit Code !=0)"  || $currentStatus == "Cleared" ]]; then
		echo "Job $jobNumber failed and will be resubmitted"
		if [[ $sameJobRuns -lt 20 ]]; then                                      
            CancelAndResubmit
			if [[ $? != 0 ]]; then
			# if submit failed, try one more time
			CancelAndResubmit
            fi
			let sameJobRuns++                                            
            continue                                                       
        else
			echo "Something is wrong with the grid because status is $currentStatus"                        
			run="false"
			exit 4 
		fi    	
	elif [[ $currentStatus == "Ready" ||  $currentStatus == "Submitted" || $currentStatus == "Scheduled" || $currentStatus == "Waiting" ]]; then
		echo "Waiting for job $jobNumber to start running..."
		sleep 120
	else
		# Code sometimes randomly reaches here, even though status is running or scheduled causing the script to exit.
		error=$(glite-wms-job-status -i JOBID/jobID_$jobNumber 2>&1 | grep "Unable to find" | grep "$jobID")
		if [[ ! -z $error ]]; then
		    echo "status was unknown, retrying, error: $error"
			# Try again to read the status.
			sleep 10
			currentStatus=$(glite-wms-job-status -i JOBID/jobID_$jobNumber 2>&1 | grep "Current Status" | cut -d":" -f 2 | tr -d ' ')
			continue
			#echo "Something is wrong with the grid because status is $currentStatus and error is $error"
            #exit 4

		fi
		sleep 60
	fi
done

################################################################################
#                                                                              #
#                          Get the output                                      #
#                                                                              #
################################################################################
###### DELETED THIS PART FROM ORIGINAL FORK - REPLACE WITH MY OWN VERSION ######
if [[ $getOutput == "true" ]]; then
	glite-wms-job-output -i JOBID/jobID_$jobNumber --dir ./ 2>/dev/null
fi
	


