import lxml.etree
import os
import sys
import json



def validate(xmlTree, xpath , count ):
    found = xmlTree.xpath(xpath)
    if(found.__len__() == count):
        return True
    return False

def runAssesments(AssesmentFilePath, treeObject):
    status = None
    results=[]
    if(not os.path.exists(AssesmentFilePath)):
        raise Exception("Unable to locate json path: "+ AssesmentFilePath)
    assesments = json.load(open(AssesmentFilePath,'r')).get("assesments")
    if(assesments is None):
        raise Exception("no Assesments were found")
    for items in assesments:
        result={}
        status = validate(treeObject , items["find"], items["expectedCount"])
        result["description"]= items.get("assesmentDescription")
        if(not status):
            result['status']="FAIL"
            result["message"]=items["errorMessage"]
        else:
            result['status']="PASS"
            result["message"]="Assesment Success"
        results.append(result)
    return results

    


def getResultXml(ResultFilePath):
    if(not os.path.exists(ResultFilePath)):
        raise Exception("Unable to locate xml path: "+ ResultFilePath)

    try:
        root = lxml.etree.parse(ResultFilePath)
        return root
    except:
        print("error while parsing XML file: "+ str(sys.exc_info()))
        raise Exception("error while parsing XML file: "+ str(sys.exc_info()))


if __name__ == "__main__":
    try:
        sys.argv[1]
        sys.argv[2]
    except:
        print("incorrect calling format. use: python xmlAssesment.py <assesmentInstructionfile.json> <testNG result xml file>")
        raise Exception("incorrect calling format. use: python xmlAssesment.py <assesmentInstructionfile.json> <testNG result xml file>")
        
    jsonFile = sys.argv[1]#"Assesment_Instruction.json"
    xmlFile = sys.argv[2]#"testng-results.xml"
    resultsXml = getResultXml(xmlFile)
    results = runAssesments(jsonFile, resultsXml)

    # Print the results
    print(json.dumps(results, indent=4))

    # Store results in required format
    final_output={}
    for instruction in results:
        if instruction['status'] == "FAIL":
           final_output[instruction['description']]="TEST_STATUS_FAILURE"
        else:
            final_output[instruction['description']]="TEST_STATUS_SUCCESS"

    json.dump(final_output,open("assesment_result.json",'w+'))