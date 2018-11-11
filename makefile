SDIR = ./src/
DDIR = ./bin/
RDIR = ./data/
JC = javac

.SUFFIXES: .java .class
.java.class:
	$(JC) -sourcepath $(SDIR) -d $(DDIR) $*.java

CLASSES = \
	$(SDIR)basic/agent/Agent.java \
	$(SDIR)basic/agent/BasicMinimaxAgent.java \
	$(SDIR)basic/agent/PrunedMinimaxAgent.java \
	$(SDIR)basic/constant/Board.java \
	$(SDIR)basic/domain/State.java \
	$(SDIR)basic/Main.java \
	$(SDIR)advanced/agent/Agent.java \
	$(SDIR)advanced/agent/HeuristicPrunedMinimaxAgent.java \
	$(SDIR)advanced/domain/AdvanceState.java \
	$(SDIR)advanced/domain/State.java \
	$(SDIR)advanced/Main.java \
#	$(SDIR)ultimate/agent/Agent.java \
#	$(SDIR)ultimate/agent/HeuristicPrunedMinimaxAgent.java \
#	$(SDIR)ultimate/domain/UltimateState.java \
#	$(SDIR)ultimate/Main.java \

default: install

init:
	mkdir -p $(DDIR)

install: init classes
	@echo 'java basic.Main' > $(DDIR)basic-ttt
	@echo 'java advanced.Main' > $(DDIR)advanced-ttt
	@echo 'java ultimate.Main' > $(DDIR)ultimate-ttt
	chmod +x $(DDIR)basic-ttt $(DDIR)advanced-ttt $(DDIR)ultimate-ttt
	@echo 'build complete!'

classes: $(CLASSES:.java=.class)

clean:
	$(RM) -r $(DDIR)
	@echo 'done!'