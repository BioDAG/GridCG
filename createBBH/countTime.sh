#!/bin/bash

Time(){
# get the time difference of the two timestamps
        timestamp1=${1}
        timestamp2=${2}
        t=$(date -d "$timestamp1" +"%d-%h %H-%M-%S")
        t1=$(date -d "$timestamp2" +"%d-%h %H-%M-%S")
        diff=$(expr $t1 - $t)

}

if [[ "$#" -ne 1 && "$#" -ne 2 ]]; then 
	echo "Wrong number of arguments"
    echo "USAGE: countTime.sh <jobNumber> optional:<filename>" 
    exit 1
fi

jobNumber=$1
if [ "$#" -eq 2 ]; then
	file=$2
else
	# Default file to look at is timestamp.out
	file="testTimestamp"
fi

date1=$(date)
echo "hahahehe $date1 ahhaha"
