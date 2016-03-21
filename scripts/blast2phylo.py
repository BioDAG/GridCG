#!/usr/bin/env python

# This python script creates phylogenetic profiles given the blast output for
# part of the query file contained in 1 job. 

import sys

# Define helper functions
def get_output(blast_output,isBBH):
	if(isBBH):
		extension = '.bh'
	else:
		extension = '.phylo'

	return blast_output.split('.')[0] + extension
	
def write_best_hit(protein,dbProtein,file_object):
	file_object.write(protein + "    " + dbProtein + "\n")
	
def get_organism(protein):
	return '-'.join(protein.split('-')[:2])	

def get_this_org(blast_output):
	return blast_output.split('.')[0]
	
def write_best_hit(protein,dbProtein,file_object):
	file_object.write(protein + "    " + dbProtein + "\n")
	
# Define main functionality functions
def plainPhylo(r,w,orderedOrgs):
	w.write("# This is the file's format: \n")
	w.write("# protein_name:  [" + '   '.join([o for o in orderedOrgs]) + '] \n \n \n' )		

	dicOrg = {}
	for o in list(enumerate(orderedOrgs)):
		dicOrg[o[1]] = o[0]
		
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
				for o in orderedOrgs:
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

def BBH(r,w,thisOrganism):
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

def main():	
	# Check command line arguments.
	if len(sys.argv) < 3:
		print("Please specify the Blast+ output and a file containing the organism names."
			+ " Lastly supply 'true' for BBH or 'false' for plain Phylo profiles")
		sys.exit(85)
	if(sys.argv[3] == "True" or sys.argv[3] == "true"):
		isBBH = True;
	
    
	elif(sys.argv[3] == "False" or sys.argv[3] == "false"):
		isBBH = False;
	else:
		print "The isBBH argument should be either true or false"
	
	try:
		blast = sys.argv[1]
		orderedOrgs = []
		r = open(blast,'r')
		for line in open(sys.argv[2]):
			orderedOrgs.append(line.split()[1].strip())
		print orderedOrgs
		
	except IOError:
		print("Could not read an input file, please check the specified paths")
		exit(2)

	outputFile = get_output(blast,isBBH)
	w = open(outputFile,'w')

	print('Reading Blast+ output from file: ' + blast + ' and writing profile output to: ' + outputFile)
	
	# Main functionality depending on isBBH
	if(isBBH):
		thisOrganism = get_this_org(blast)
		BBH(r,w,thisOrganism)
	
	else:
		plainPhylo(r,w,orderedOrgs)	
			
	r.close()
	w.close()

if __name__ == "__main__":
    main()
