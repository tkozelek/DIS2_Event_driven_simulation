# Event-driven simulation of a furniture workshop

Semester project for Discrete Simulation (FRI UNIZA, 2025), the second of three:
[Monte Carlo](https://github.com/tkozelek/DIS1_Monte_carlo),
this event-driven one and
[agent-based](https://github.com/tkozelek/DIS3_Agent_Simulation).

A workshop makes tables, chairs and cupboards. Orders arrive at random times, and three groups of
workers handle them: group A cuts the material, group B assembles, group C paints and fits.
Workers move between the storage and the workstations. The simulation measures how long an order
takes and how busy the workers are with different numbers of workers and workstations.

## How it works

- An event calendar on a priority queue. One replication is 249 working days of 8 hours.
- Exponential order arrivals, and triangular, uniform and empirical times for moves and operations,
  from my own generators with separate seeds.
- Statistics over many replications: time of an order in the system, time-weighted queue lengths and
  the utilisation of every worker and group, with 95 % confidence intervals.
- A Swing GUI that runs at full speed or slowed down with live tables of workers, orders and
  workstations, can be paused, and draws a chart with JFreeChart.

## Run it

Open the project in IntelliJ IDEA and run `kozelek.Main` (the GUI uses IntelliJ GUI Designer forms).
Java 23, Maven.
