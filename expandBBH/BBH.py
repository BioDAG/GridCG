#!/usr/bin/env python

# This python script serves the same purpose as phylogenetic.py,
# i.e creating phylogenetic profiles given the blast output for
# part of the query file contained in 1 job. Unlike phylogenetic.py
# this script will return best hit pairs to the master, who will then
# scan all parts to find BBHs.

import sys

# Define helper functions.
def get_organism(protein):
	return '-'.join(protein.split('-')[:2])

def get_BH_output(blast_output):
	return blast_output.split('.')[0] + '.bh'

# CAUTION: THIS ONLY WORKS IN WE HAVE 1 QUERY ORG PER JOB!
def get_this_org(blast_output):
	return blast_output.split('.')[0]
	
def write_best_hit(protein,dbProtein,file_object):
	file_object.write(protein + "    " + dbProtein + "\n")
	
def main():	
	# Check command line arguments.
	if len(sys.argv) < 3:
		print("Please specify the Blast+ output and a file containing the organism names")
		sys.exit(85)

	try:
		blast = sys.argv[1]
		genomeAll = sys.argv[2]
		print("blast file path: " + blast)
		print("GenomeAll path: " + genomeAll)
		r = open(blast,'r')
		orderedOrg = []
		with open(genomeAll,'r') as f:
			for line in f:
				orderedOrg.append(line.split()[1])
	except IOError:
		print("Could not read an input file, please check the specified paths")
		exit(80)

	outputFile = get_BH_output(blast)
	w = open(outputFile,'w')

	print('Reading Blast+ output from file: ' + sys.argv[1] + ' and writing profile output to: ' + outputFile)
	
	# This will not work if query contains more than 1 organisms!!!
	thisOrganism = get_this_org(blast)
	
	# Provide a header.
	dicOrg = {}
	for o in enumerate(orderedOrg):
		dicOrg[o[1]] = o[0]

	for line in r:
		protein = line.split()[0]
		dbProtein = line.split()[1]
		organism = get_organism(dbProtein)
		try:
			if organism == thisOrganism:
				continue;
			if protein == previous:
				if organism not in org_hits:
					write_best_hit(protein,dbProtein,w)
					orgPrevious = organism
					org_hits.add(organism) 
			else:
				write_best_hit(protein,dbProtein,w)
				org_hits = set([organism])
				previous = protein
				orgPrevious = organism
		except:
			previous = protein
			orgPrevious = organism
			org_hits = set()
	r.close()
	w.close()

if __name__ == "__main__":
    main()
