# Wireless Sensor Networks Project
In this program we simulate a Wireless Sensor Network.<br/>The project is seperated into two sub-projects: 
1. the first contains the sink's implementation (package net.java.netbeansspot.sink)
2. the second contains the sampler's implementation (package net.java.netbeansspot.spot). 

In order to execute the program, we have to use `solarium` simulator, in which we create three spots, a sink and two samplers. The samplers collect information about the temprature and send a broadcast message with it. Sink cosists of two threads: _Receiver thread_ and _Alert thread_. The Receiver thread receives the message and stores it into an array. The Alert thread reads the measurements from the array and applies cusum algorithm in order to determine whether a fire exists or not. Written in `Java Micro Edition` using _Netbeans_ and _Solarium simulator_ in _Windows XP_.
