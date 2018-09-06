# Chess Engine

This repository contains the Source code for the project Chess Engine developed in Java. This Engine makes use of Alpha Beta pruning algorithm to make moves. Depth of Alpha Beta is not fixed, it can be set by user.  
Depth can take values from 1 to 4. Lower the depth, easier the game level. At depth 5, program takes around 20 seconds to process single move.
## Getting Started
These instructions will help you to set up and run this engine on your local machine.
### Prerequisites
* Java version 7 or more  
Follow this [link](https://www.java.com/en/download/help/download_options.xml) to download and install Java
* Java Path should be added to the Environment variable  
Here's the [link](https://www.java.com/en/download/help/path.xml) to do this.
## Compiling and running the project
### Running the project:
* To run this project, clone this repository or download all the src folder.
* Open command prompt / terminal in src folder.
* Compile Chess.java by writing ```javac chess/Chess.java``` in the command window.
* Run the executable by writing ```java chess/Chess``` in the command window.
### Using the program:
* Once the program runs, A dialog box opens asking user to select the first player. Click AI if you want Computer to play first and click Human otherwise.
* Next dialog box opens to select depth
  * Depth can take value from 1 - 4.
  * More the depth, harder the game gets.
  * At depth 5, program takes around 20 seconds to process single move. So select 5 if you have a lot of time.
  * After depth selection, you can start playing.
## Screenshots:
![Player selection window](https://github.com/coder-singh/Chess-Engine/blob/master/screenshots/player.PNG)  
![Depth selection window](https://github.com/coder-singh/Chess-Engine/blob/master/screenshots/depth.PNG)  
![Game play](https://github.com/coder-singh/Chess-Engine/blob/master/screenshots/game.PNG)
