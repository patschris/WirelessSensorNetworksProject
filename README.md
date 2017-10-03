# Wireless Sensor Networks Project
In this program we simulate a wireless sensor network. The project is seperated into two sub-projects: the first contains the implementation for sink (package net.java.netbeansspot.sink) and the second contains the implementation for the samplers (package net.java.netbeansspot.spot). In order to execute the program, we have to use solarium simulator, in which we create three spots, a sink and two samplers. The samplers collect information about the temprature and send a broadcast message with it. Sink cosists of two threads: Receiver thread and Alert thread. The Receiver thread receives the message and stores it into an array. The Alert thread reads the measurements from the array and applies cusum algorithm in order to determine whether a fire exists or not. Written in java micro edition using Netbeans and Solarium simulator in Windows XP.
