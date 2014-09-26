import xml.etree.cElementTree as ET
import sys
import string
import random

numberOfUsers = int(sys.argv[1])

## Generate a random set of names of length 4
namesList = []
for x in range(0, numberOfUsers):
  str = (''.join(random.choice(string.ascii_lowercase) for i in range(5)))
  namesList.append(str)
  
print(namesList)

root = ET.Element("world")

for x in range(0, numberOfUsers):
  user = ET.SubElement(root, "user")

  name = ET.SubElement(user, "name")
  name.text = namesList[x]

  version = ET.SubElement(user, "version")
  version.text = "0"
  
  # Set up a random students list
  students = ET.SubElement(user, "students")
  for y in range(random.randint(0, numberOfUsers-1)):
    tempString = namesList[random.randint(0, numberOfUsers-1)]
    if tempString != namesList[x]:
      student = ET.SubElement(students, "student")
      student.text = tempString

tree = ET.ElementTree(root)
tree.write("python_created_test.xml")