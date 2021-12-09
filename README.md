# Brick_Destroy
This is a simple arcace video game.
Player's goal is to destroy a wall with a small ball.
The game has  very simple commmand:
SPACE start/pause the game
A or left arrow to move the player left 
D or right arrow to move the player rigth
ESC enter/exit pause menu
ALT+SHITF+F1 open console
the game automatically pause if the frame loses focus

Enjoy ;-)

#Updates in version 0.2:
- Added the ability to move using left and right arrow keys in GameBoard.java to cater to players who tend to use arrow keys instead of A and D
- Added an audio player class to play background audio and sound effects
- Added a permanent Leaderboard in accordance to project description
- Added an Information Screen that displays Tutorial & Leaderboard in accordance to project description
- Added a GameOver Screen for a nicer transition to the Leaderboard
- Added 2 new levels using the existing level generator in accordance to project description
- Added ability to return to home from gameboard in the gameboard.java pause menu to give player mpre freedom in interacting with the game
- Connected win scenario in GameBoard to GameOver
- Removed unused imports such as java.awt.geom.Point2D & java.awt.geom.Rectangle2D in player.java
- Replaced all colors used in game to enhance visual appeal
- Replaced plain color backgrounds with background images to enhance visual appeal
- Fixed typo in method name such as movRight to moveRight in player.java
- Fixed a bug in makeSingleTypeLevel method from wall.java where it will constanty add 1 clay brick whatever brcik type inputted
- Changed inacurate variable names such as MENU_TEXT to EXIT_TEXT in HomeMenu.java
- Changed GameBoard owner to GameFrame instead of JFrame to pass information from gameframe to gameboard
- Changed resetLevels in wall.java to restart game from first level instead of staying on the current level so restarting after win scenario won't break nextlevel in the debug console
