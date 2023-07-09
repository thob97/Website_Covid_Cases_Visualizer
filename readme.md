# Roguelike in Rust
<p float="left">
    <img src="./_github_example_pictures/1.png"  width="50%" height="50%">
    <img src="./_github_example_pictures/4.png"  width="50%" height="50%">
</p>
This repository presents a script to clean a given dataset and visualize it in graphics on a HTML website.

## Table Of Content

- [Course content](#course-content)
- [Grading](#grading)
- [Getting Started](#getting-started)
  - [Dependencies](#dependencies)
  - [Installation](#installation)
  - [Compile and run](#compile-and-run)
- [Example pictures](#example-pictures)
- [Contribution](#contribution)


## Course content

The purpose of this project is to further explore and solidify the concepts learned in the Database Systems course. Project for the Database Systems module. The goal is to represent a given dataset using graphs. To achieve this, the data needs to be cleaned first, then transferred to a database, and finally connected to a self-developed webpage that visually represents the data.


## Grading

| Assignment  | Grade |
| ------------- | ------------- |
| Project  | 94% -> 1.3  |


## Getting Started 

### Dependencies
 * python2
 * java 11
 * javac 11
 * postgresql
 * postgres jdbc driver
 * optional: pgadmin4
 * optional: eclipse


## installation
 1. Run the dataset cleaner with the dataset beeing in the same directory `python2 convert.py`
 2. Create the ER-model with postgres. E.g by running the following sql code:
 '''
 CREATE TABLE Land (
	Name varchar(100) PRIMARY KEY,
	Bevoelkerung int,
	Kontinent varchar(100)
);

CREATE TABLE Tag (
	Datum date PRIMARY KEY
)

CREATE TABLE hat_faelle (
	Datum date,
	Faelle int,
	Tode int,
	Name varchar(100),
	primary key (Datum, Name),
	foreign key (Datum) references Tag(Datum),
	foreign key (Name) references Land(Name)
);
 '''
 3. Import the three csv files which were created in step 1 to the postgresql database
 4. Change the username, password and databasename in the 'HttpServer.java' file corresponding to your postgresql setup
5. add the jdbc driver to your project (e.g. by adding it to the libaries in eclipse)

## Compile and run

1. run the javaproject 'http_website'. (eg with eclipse) 
2. visit the website by  127.0.0.1:8080

## TODO Example pictures
<p float="left">
    <img src="./_github_example_pictures/1.png"  width="50%" height="50%">
    <img src="./_github_example_pictures/2.png"  width="50%" height="50%">
    <img src="./_github_example_pictures/3.png"  width="50%" height="50%">
    <img src="./_github_example_pictures/4.png"  width="50%" height="50%">
</p>
## Contribution

* Thore Brehmer - projekt task: dev. website with graphs + connection to sql database to retrieve data
* Jonny Lam - projekt task: data cleaning (removing small dataset errors)
* David Lee - projekt task: import data to sql database