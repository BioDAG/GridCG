#!/bin/bash
/bin/hostname -f

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
    
    address="ftp://ftp.ncbi.nlm.nih.gov/blast/executables/LATEST/ncbi-blast-$version+-x64-linux.tar.gz"	
    wget --prefer-family=IPv4 -T 1000 $address 2>$1 >> /dev/null 2>&1
    #wget --prefer-family=IPv4 -T 1000 ftp://ftp.ncbi.nlm.nih.gov/blast/executables/LATEST/ncbi-blast-$version+-x64-linux.tar.gz
    #wget --prefer-family=IPv4 -T 1000 ftp://ftp.ncbi.nlm.nih.gov/blast/executables/$version/ncbi-blast-$version+-x64-linux.tar.gz

    gunzip -d ncbi-blast-$version+-x64-linux.tar.gz
    tar xvpf ncbi-blast-$version+-x64-linux.tar

    # export PATH for faster calling blast function
    export PATH="ncbi-blast-$version+/bin:$PATH"

}

# Main starts here. This is the executable fed to the grid worker nodes.
# It will execute a blast search, then pipe its output to phylogenetic profiling algorithm.

if [ "$#" -ne 4 ]; then
    echo "Illegal number of parameters"
    echo "USAGE: gridExecutable.sh <database.fasta> <query.fasta> <organisms.txt> <isBBH>"
    exit 1
fi

export LCG_CATALOG_TYPE=lfc   
export LFC_HOST=lfc.isabella.grnet.gr   
export LCG_GFAL_INFOSYS=bdii.isabella.grnet.gr:2170   
export LCG_GFAL_VO=see  

DATABASE=${1##*/}
# Download database from the SE.
lcg-cp lfn:/grid/see/steremma/$DATABASE file:$DATABASE
QUERY=${2##*/}
ORGANISMS=${3##*/}
isBBH=$4
output="${QUERY%.*}.blast"

# Comment out the following line if running on local machine (blast already installed).
blast_version="2.3.0"
#blast_version="2.2.31"
getBlast $blast_version

start=`date +%s`

################################# PROBLEM WITH GLIBC VERSION HERE!##################################
makeblastdb -in $DATABASE -dbtype prot -parse_seqids -out script_db/data -hash_index
blastp -db script_db/data -out $output -query $QUERY -evalue 0.000001 -outfmt 6
rm -r script_db
####################################################################################################
end=`date +%s`

runtime=$((end-start))
echo -e "blast runtime is: $runtime seconds "

start=`date +%s`

if [[ $isBBH == "true" || $isBBH == "1" ]]; then
    echo "creating BBH profiles"
elif [[ $isBBH == "false" || $isBBH == "0" ]]; then
    echo "creating plain phylogenetic profiles"
else
    echo "argument isBBH should be boolean"
    exit 1
fi

chmod +x blast2phylo.py
./blast2phylo.py $output $ORGANISMS $isBBH

end=`date +%s`

runtime=$((end-start))
echo -e "phylogenetic runtime is: $runtime seconds "

