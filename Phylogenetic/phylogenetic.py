#!/usr/bin/env python

import sys

# Define helper functions
def get_phylo_output(blast_output):
	return blast_output.split('.')[0] + '.phylo'

def get_organism(protein):
	return '-'.join(protein.split('-')[:2])	

def get_this_org(blast_output):
	return blast_output.split('.')[0]
		
# Check command line arguments.

if len(sys.argv) < 3:
	print("Please specify the Blast+ output and a file containing the organism names")
	sys.exit(1)

blast = sys.argv[1]
try:
	validOrganisms = []
	r = open(blast,'r')
	for line in open(sys.argv[2]):
		validOrganisms.append(line.split()[1].strip())
	print validOrganisms
	
except IOError:
	print("Could not read an input file, please check the specified paths")
	exit(2)

outputFile = get_phylo_output(blast)
w = open(outputFile,'w')

print('Reading Blast+ output from file: ' + blast + ' and writing profile output to: ' + outputFile)

# Provide a header.
dicOrg = {}
for o in list(enumerate(validOrganisms)):
	dicOrg[o[1]] = o[0]

thisOrganism = get_this_org(blast)

w.write("# This is the file's format: \n")
w.write("# protein_name:  [" + '   '.join([o for o in validOrganisms]) + '] \n \n \n' )
for line in r:
	protein = line.split()[0]
	dbProtein = line.split()[1]
	organism = get_organism(dbProtein)
	try:
		
		if protein == previous:
			if dbProtein == dbPrevious:
				continue;
			dbPrevious = dbProtein
			tempDic[organism] += 1
		else:
			tableVal = ''
			for o in validOrganisms:
				tableVal += ' ' +str(tempDic[o]) + ' '
			w.write(previous + ':      ['+tableVal+']\n')
			for key in tempDic:
				tempDic[key] = 0
			tempDic[organism] += 1
			previous = protein
			dbPrevious = dbProtein    
            
	except:
		tempDic = dicOrg
		for key in tempDic:
			tempDic[key] = 0
		tempDic[organism] += 1
		previous = protein
		dbPrevious = dbProtein
		
r.close()
w.close()


