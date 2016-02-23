#!/usr/bin/env python

import sys

# Define helper functions
def appears_before(prot1,prot2):
	"""This function will return TRUE if the order in which the user 
	specified the organisms, is the same as the arguments order"""
	return validOrganisms.index(get_organism(prot1)) < validOrganisms.index(get_organism(prot2))

def get_organism(protein):
	return '-'.join(protein.split('-')[:2])

def e_value(line):
	return float(line.split()[10])
	
# Check command line arguments.
if len(sys.argv) < 3:
	print("Please specify the Blast+ output and a file containing the organism names")
	sys.exit(1)

try:
	validOrganisms = []
	r = open(sys.argv[1],'r')
	for line in open(sys.argv[2]):
		validOrganisms.append(line.split()[1].strip())
	print validOrganisms
except IOError:
	print("Could not read an input file, please check the specified paths")
	exit(2)

outputFile = "phylo.out"
w = open(outputFile,'w')

print('Reading Blast+ output from file: ' + sys.argv[1] + ' and writing profile output to: ' + outputFile)


dicOrg = {}
for o in list(enumerate(validOrganisms)):
	dicOrg[o[1]] = o[0]

# Provide a header.
w.write("# This is the file's format: \n")
w.write("# protein_name:  [" + '   '.join([o for o in validOrganisms]) + '] \n \n \n' )

# Loop through each line.
for line in r:
	protein = line.split()[0]
	dbProtein = line.split()[1]
	organism = get_organism(line.split()[1])
	try:
		if protein == previous:
			if dbProtein == dbPrevious:
				continue;
			#This executes if the dbProtein is the best match for protein(it appears 1st)	
			dbPrevious = dbProtein
			#Only increment if this is the 1st (best) hit on this organism.
			if tempDic[organism]==0:
				org_scores[dbProtein] = e_value(line)
			tempDic[organism] += 1
				
		else:			
			tableVal = ''
			for o in validOrganisms:
				tableVal += ' ' +str(tempDic[o])
			w.write(previous + '\t['+tableVal+']\t' + str(org_scores) + '\n')
			#Reinitialize temp variables
			for key in tempDic:
				tempDic[key] = 0 
				
			tempDic[organism] += 1
			previous = protein
			dbPrevious = dbProtein
			org_scores = {}
            
	except:
		org_scores = {}
		tempDic = dicOrg
		for key in tempDic:
			tempDic[key] = 0
		tempDic[organism] += 1
		previous = protein
		dbPrevious = dbProtein
		
r.close()
w.close()


