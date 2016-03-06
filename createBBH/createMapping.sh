#!/bin/bash
 
for fullfile in `ls Fasta/` ; do
	base=$(basename "$fullfile")
	filename="${base%.*}"
	echo -e "$base \t $filename" >> GenomeNew
done

