import json
import csv
from sets import Set

def convert_hat_faelle():

    with open ("covid19.json", "r") as f:
        data = json.load(f)
        records = data["records"]

    with open ("convert.csv", "w") as f:
        csv_file = csv.writer(f)
        csv_file.writerow(["Datum","Faelle","Tode","Name"])
        for record in records:
            if int(record["cases"]) < 0: #Zahl negativ? Mache die Positiv!
                    temp = int(record["cases"]) *-1
                    csv_file.writerow([record["dateRep"], temp, record["deaths"], record["countriesAndTerritories"].encode('utf-8') ])
            elif int(record["deaths"]) < 0: #Zahl negativ? Mache die Positiv!
                    temp = int(record["deaths"]) *-1
                    csv_file.writerow([record["dateRep"], record["cases"], tmp, record["countriesAndTerritories"].encode('utf-8') ])
            else: 
                    csv_file.writerow([record["dateRep"], record["cases"], record["deaths"], record["countriesAndTerritories"].encode('utf-8')])

    #Loescht alle Zeilen die doppelt sind
    infile = open('convert.csv','r')
    outfile = open('output.csv', 'w')
    seen = Set()
    for line in infile:
        if line not in seen:
            outfile.write(line)
            seen.add(line)
    infile.close()
    outfile.close()

    #Loescht alle Zeilen mit gleichem Datum und Landnamen
    inputf = open('output.csv', 'r')
    outputf = open('hat_faelle.csv', 'w')
    seen2 = Set()
    for line in inputf:
        tmp = line.split(",")
        tmp2 = tmp[0] + tmp[3]
        if tmp2 not in seen2:
            outputf.write(line)
            seen2.add(tmp2)
    inputf.close()
    outputf.close()
    print("File:hat_faelle.csv was created successfull")


def convert_name():
    with open ("covid19.json", "r") as f:
        data = json.load(f)
        records = data["records"]

    with open ("convert.csv", "w") as f:

        csv_file = csv.writer(f)
        csv_file.writerow(["Name", "Bevoelkerung", "Kontinent"])
        for record in records:
            if record["popData2018"] < 0: #Zahl negativ? Mache die Positiv!
                    temp = long(record["popData2018"]) *-1
                    csv_file.writerow([record["countriesAndTerritories"].encode('utf-8'), temp, record["continentExp"]])
            else: 
                    csv_file.writerow([record["countriesAndTerritories"].encode('utf-8'), record["popData2018"], record["continentExp"]])

    #Loescht alle Zeilen die doppelt sind
    infile = open('convert.csv','r')
    outfile = open('land.csv', 'w')
    seen = Set()
    for line in infile:
        if line not in seen:
            outfile.write(line)
            seen.add(line)
    infile.close()
    outfile.close()
    print("File:land.csv was created successfull")


def convert_tag():
    with open ("covid19.json", "r") as f:
        data = json.load(f)
        records = data["records"]

    with open ("convert.csv", "w") as f:
        csv_file = csv.writer(f)
        csv_file.writerow(["Datum"])
        for record in records:
            csv_file.writerow([record["dateRep"]])

    #Loescht alle Zeilen die doppelt sind
    infile = open('convert.csv','r')
    outfile = open('tag.csv', 'w')
    seen = Set()
    for line in infile:
        if line not in seen:
            outfile.write(line)
            seen.add(line)
    infile.close()
    outfile.close()
    print("File:tag.csv was created successfull")



convert_hat_faelle()
convert_name()
convert_tag()