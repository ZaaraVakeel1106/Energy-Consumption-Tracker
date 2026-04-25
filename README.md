# Energy Consumption Tracker

Energy Consumption Tracker is a Java-based console application designed to monitor and analyze electricity and water usage for households and organizations. It provides insights based on city-level data and promotes sustainable resource consumption through practical feedback and recommendations.

## Features

### Household Module
- Tracks electricity and water consumption  
- Calculates monthly bills using city-specific rates  
- Displays usage benchmarks and sustainability suggestions  
- Alerts users if bill payment is pending  
- Exports a detailed bill report as a text file  

### Organization Module
- Stores and displays electricity and water consumption for the past five months  
- Calculates average monthly usage  
- Provides warnings if consumption exceeds recommended limits  
- Suggests sustainable practices for organizations  

### City Analysis
- Compare two cities based on:
  - Water cost per liter  
  - Electricity cost per unit  
  - Average consumption  
  - Pollution index  
  - Population  
- Rank cities based on selected metrics  

### Authentication
- Role-based login system for households and organizations  
- Data is validated using stored text files  

## Tech Stack
- Java  
- Object-Oriented Programming  
- File Handling  
- Collections  

## Project Structure
- EnergyConsumptionTracker.java  
- users.txt  
- households.txt  
- organizations.txt  

## How to Run

Compile:

Run:

## Data File Format

users.txt  
username password role  

households.txt  
username city electricity water billPaid  

Example:  
zaara Delhi 210 4200 true  

organizations.txt  
orgName license e1 w1 e2 w2 e3 w3 e4 w4 e5 w5  

## Author
Zaara Vakeel
