#!/bin/bash

# Helper function to install blast on remote machine, if it's not already installed.
function getBlast {
	version=$1
    echo "Installing blast, version "$version


    # Check that system architecture is 64 bit, if not latest blast is not supported.
    SYS_ARCH=`getconf LONG_BIT`
    if [ $SYS_ARCH = "64" ]
    then
        echo "I'm 64-bit"
    else
		# I could just download the 32 bit version..IF IT EXISTED!! :)
        echo "I'm 32-bit, SOMETHING WILL GO WRONG NOW"
    fi
	
    wget --prefer-family=IPv4 -T 1000 ftp://ftp.ncbi.nlm.nih.gov/blast/executables/LATEST/ncbi-blast-$version+-x64-linux.tar.gz
    #wget --prefer-family=IPv4 -T 1000 ftp://ftp.ncbi.nlm.nih.gov/blast/executables/$version/ncbi-blast-$version+-x64-linux.tar.gz

    gunzip -d ncbi-blast-$version+-x64-linux.tar.gz
    tar xvpf ncbi-blast-$version+-x64-linux.tar

    # export PATH for faster calling blast functions
    export PATH="ncbi-blast-$version+/bin:$PATH"

}

# Main starts here. This is the executable fed to the grid worker nodes.
# It will execute a blast search, then pipe its output to phylogenetic profiling algorithm.
# REMEMBER!! ALL OUTPUT WILL BE SAVED IN THE QUERY DIRECTORY!!!!

if [ "$#" -ne 3 ]; then
    echo "Illegal number of parameters"
    echo "USAGE: fasta2phylo.sh <database.fasta> <query.fasta> <organisms.txt>"
    exit 1
fi

DATABASE=$1
QUERY=$2
ORGANISMS=$3
output="${QUERY%.*}.blast"

# Comment out the following lines if running on local machine (blast already installed).
blast_version="2.3.0" 
#blast_version="2.2.31" # If i choose this version, make sure ftp path does not search in the LATEST dir!
getBlast $blast_version

# Timestamping Blast
start=`date +%s`

makeblastdb -in $DATABASE -dbtype prot -parse_seqids -out script_db/data -hash_index
blastp -db script_db/data -out $output -query $QUERY -evalue 0.000001 -outfmt 6
rm -r script_db

# Blast finished
end=`date +%s`
runtime=$((end-start))
echo -e "\e[31m blast runtime is: $runtime seconds \e[0m"

echo "feeding blast to BBH.py"
# Timestamping BBH profiling.
start=`date +%s`

# This is the only thing different between the regular and BBH version of this executable.
chmod +x BBH.py
./BBH.py $output $ORGANISMS

# BBH finished.
end=`date +%s`
runtime=$((end-start))
echo -e "\e[34m phylo runtime is: $runtime seconds \e[0m"

