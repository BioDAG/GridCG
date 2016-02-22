#########################################GOAL####################################################
In this working directory i attempt to wrap Blast and phylogenetic profiling utlities 
into the same grid jobs, thus minimizing communication between the local machine 
and the working nodes to improve performance.

#########################################WORKFLOW#################################################

 1) User calls submitAll.sh providing 3 arguments:
      - the fasta file serving as the database (supposed to be typically bigger than all queries)
      - a directory containing all fasta query files 
			- At the moment of writing is query is expected to contain 1 organism only,
			  this will be further developed to allow any integer number of orgs per query file
	  - a text file containg organism identifiers
	  example usage: ./submitAll.sh database.fasta Fasta/ GenomeAll
	  
	  
2) The Main.java from blastUtils is called by the script to generate JDL files. Default location
   is a directory named jdl_collection, an overloaded function can be used to alter that. Key class
   utilized here is Submitter.java
   
3) the script submits all jdl files. All of them have the same executable, fasta2phylo.py to manage 
   each query fasta file.
   
4) fasta2phylo.sh first downloads and installs latest blast version on the working node. It creates a
   Blast DB out of the database fasta file and runs blastp using the query fasta file against the db.
   It then feeds the blast output to BBH.py which finds all best hits. Finally we output both the blast 
   and the best hit files.
   
5) User calls collect_bh_output.sh (still under development) to combine all best hit files and produce 
   the final BBH phylogenetic profiles for each sequence.
 
