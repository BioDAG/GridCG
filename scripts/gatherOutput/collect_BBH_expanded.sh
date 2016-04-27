#!/bin/bash
set -e
# This script collects EXPANDED output from all grid jobs
# I assume the existence of multiple directories 
# named: steremma_<identifier>, each of them containing
# 1 blast (.blast extension) and 1 phylogenetic (.bh extension) output file.
# ATTENTION: I am also assuming the existence of the BH_output and Blast_output
#            directories, filled with the already processed organisms.


# Check that BH_output and Blast_output do exist.
if [ ! -d "BH_output" ]; then
	echo "directory BH_output does not exist!"
	exit 1
fi
if [ ! -d "Blast_output" ]; then
	echo "directory Blast_output does not exist!"
	exit 1
fi

mkdir temp_dir
cp `find jdl_collection/steremma_* -name "*.blast"` temp_dir/
cp `find jdl_collection/steremma_* -name "*.bh"` temp_dir/

cd temp_dir
for file in `ls`; do
	if [[ $file == *.bh ]]; then
		stripped_file="${file%.*}"
		awk -v var="$stripped_file" '{F=substr($0,23,8);print >> var"_"F".bh";close(F)}' $file
		rm $file
	fi
	if [[ $file == *.blast ]]; then
		if [ -f ../Blast_output/$file ]; then
		   cat $file >> ../Blast_output/$file
		   # resort blast file.
		   sort -k1,1 -k3nr,3nr -o ../Blast_output/$file ../Blast_output/$file
		else
		   mv $file ../Blast_output
		fi	
	fi
done

# Combine files with the same name inside BH_output.
cd ..
for fA in temp_dir/*; do
    fB=BH_output/${fA##*/}
    if [ -f $fB ]; then
		cat "$fA" >> "$fB"
		sort -k1,1 -o $fB $fB
	else
		mv $fA $fB
	fi	
done

# Clean up
rm -r temp_dir





