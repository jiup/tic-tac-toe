## Tic-Tac-Toe

For more detailed documentation, please see [readme.pdf](README.pdf)

#### Collaborators
Gekun Feng, Yonghao Duan

#### Requirements
JDK 1.8+

#### Structure of directory

```
tic-tac-toe
├── README.md *
├── out
│   └── production
├── src
│   ├── basic
│   │   ├── Main.java
│   │   ├── agent
│   │   │   ├── Agent.java
│   │   │   ├── BasicMinimaxAgent.java
│   │   │   └── PrunedMinimaxAgent.java
│   │   ├── constant
│   │   │   └── Board.java
│   │   └── domain
│   │       └── State.java
│   ├── advanced
│   │   ├── Main.java
│   │   ├── agent
│   │   │   ├── Agent.java
│   │   │   └── HeuristicPrunedMinimaxAgent.java
│   │   └── domain
│   │       ├── AdvanceState.java
│   │       └── State.java
│   └── utimate
│       ├── Main.java
│       ├── agent
│       │   ├── Agent.java
│       │   └── HeuristicPrunedMinimaxAgent.java
│       └── domain
│           └── UltimateState.java
└── ttt.iml
```

#### How to use

1. Prepare: `cd /path/to/ttt/out/production/`
2. Build
		1. Basic ttt: `javac -sourcepath ./src/ -d ./bin/ $*.java`
		1. Advanced ttt: `javac -sourcepath ./src/ -d ./bin/ $*.java`
		1. Ultimate ttt: `javac -sourcepath ./src/ -d ./bin/ $*.java`
	or
	`/path/to/ttt/make`
3. Run the program
    1. Basic ttt: `/path/to/ttt/bin/basic-ttt`
    1. Advanced ttt: `/path/to/ttt/bin/advanced-ttt`
    1. Ultimate ttt: `/path/to/ttt/bin/ultimate-ttt`
 