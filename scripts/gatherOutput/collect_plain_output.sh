#!/bin/bash
set -e
# This script collects output from all grid jobs
# I assume the existence of multiple
# directories named: steremma_<identifier>, each of them containing
# 1 blast (.blast extension) and 1 phylogenetic (.bh extension) output file.

if [ "$#" -ne 1 ]; then
    echo "USAGE: collect_output.sh <output_base_direcory>"
    E_ARGS=85
    exit $E_ARGS
fi

jdl_collection=$1

mkdir -p Blast_output
mkdir -p Phylo_output

#mv `find $jdl_collection/steremma_* -name "*.blast"` Blast_output/
cp `find $jdl_collection/steremma_* -name "*.blast"` Blast_output/
#mv `find $jdl_collection/steremma_* -name "*.phylo"` Phylo_output/
cp `find $jdl_collection/steremma_* -name "*.phylo"` Phylo_output/
