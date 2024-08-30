cp /tmp/external_build/reports/tests/test/testng-results.xml test-results.xml
pip3 install -r assesment/xmlAssessment/requirements.txt
python3 assesment/xmlAssessment/xmlAssessment.py assesment/xmlAssessment/Assessment_Instruction.json test-results.xml
