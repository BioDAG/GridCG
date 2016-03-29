#!/bin/bash

# Define helper functions

proteinNum(){
	line=$1
	echo $line | awk -F'[-:]' '{print $4;}'	
}

lineIsValid(){
	local line=$1
	if [[ $line == "#"* ]] || [[ -z  $line  ]]; then
		valid=false
	else	
		valid=true
	fi	
}

AppendLeft(){
	local line="$1"
	local protein=$(echo $line | awk -F':' '{print $1;}')
	local vector=$(echo $line | awk -F [\]\[] '{print $2;}')
	concat_line="$protein:	[ $APPENDER$vector]"
}

AppendRight(){
	local line=$1
	local protein=$(echo $line | awk -F':' '{print $1;}')
	local vector=$(echo $line | awk -F [\]\[] '{print $2;}')
	concat_line="$protein:	[$vector$APPENDER ]"
}

ConcatLines(){
	local line1=$1
	local line2=$2
	local protein=$(echo $line1 | awk -F':' '{print $1;}')
	local vector1=$(echo $line1 | awk -F [\]\[] '{print $2;}')
	local vector2=$(echo $line2 | awk -F [\]\[] '{print $2;}')
	concat_line="$protein:	[$vector1$vector2]"
}

Finish(){
	local mode=$1 # Denotes the unfinished input file
	local index=$2
	if [ $mode -eq 1 ]; then
		while read line; do
			lineIsValid $line
			if [ "$valid" = false ]; then
				continue
			fi
			if [ $(proteinNum $line) -gt $index ]; then 
				AppendRight "$line"
				echo "$concat_line" >> $WRITE_FILE		
			fi
		done < $FILE_1
	elif [ $mode -eq 2 ]; then
		while read line2; do
			lineIsValid $line2
			if [ "$valid" = false ]; then
				continue
			fi
			if [ $(proteinNum $line2) -gt $index ]; then 
				AppendLeft "$line2"
				echo "$concat_line" >> $WRITE_FILE		
			fi	
		done < $FILE_2
	else
		echo "the mode arg should be either 1 or 2"
		exit
	fi
	
}
# MAIN SCRIPT STARTS HERE

if [ "$#" -ne 3 ]; then
    echo "Illegal number of parameters"
    echo "USAGE: concat_expanded_output.sh <file_1> <file_2> <org_identifier>"
    exit 1
fi

# Read arguments
FILE_1=$1
FILE_2=$2
ORGANISM=$3
APPENDER="0 0 0 0 0"

WRITE_FILE="TEST.TXT"
> $WRITE_FILE
while true; do
  lineIsValid $line
  if [ "$valid" = false ]; then
    read line
    continue
  fi
  lineIsValid $line2
  if [ "$valid" = false ]; then
	read -u 3 line2
	continue
  fi

  PROTEIN_1=$(proteinNum $line)
  PROTEIN_2=$(proteinNum $line2) 
  
  if [ $PROTEIN_1 -eq $PROTEIN_2 ]; then
	ConcatLines "$line" "$line2"
	echo "$concat_line" >> $WRITE_FILE
	if ! read line; then Finish "2" "$PROTEIN_2"; break; fi
	if ! read -u 3 line2; then Finish "1" "$PROTEIN_1"; break; fi
	continue	
  elif [ $PROTEIN_1 -gt $PROTEIN_2 ]; then
	AppendLeft "$line2"
	echo "$concat_line" >> $WRITE_FILE
	if ! read -u 3 line2; then Finish "1" "$PROTEIN_1"; break; fi
  else
	AppendRight $line
	echo "$concat_line" >> $WRITE_FILE
	if ! read line; then Finish "2" "$PROTEIN_2"; break; fi
  fi
done < $FILE_1 3< $FILE_2

