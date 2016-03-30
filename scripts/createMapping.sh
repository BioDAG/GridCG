#!/bin/bash
 
if [ "$#" -ne 2 ]; then
    echo "Wrong number of arguments"
    echo "USAGE: createMapping.sh <fasta_directory> <gene_map_file>" 
    exit 1
fi
FASTA=$1
GENE_MAP=$2

for fullfile in `ls $FASTA/` ; do
	base=$(basename "$fullfile")
	filename="${base%.*}"
	echo -e "$base \t $filename" >> $GENE_MAP
done

