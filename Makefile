JC = javac
JCR = java

.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java

CLASSES = \
    User.java \
    World.java
#    Scene.java

default: classes

classes: $(CLASSES:.java=.class)

#run-tests - to be written

clean:
	$(RM) *.class *~