GENERAL USAGE NOTES
-------------------

This program is a client server quiz game.

The SERVER program is invoked by the program QuizServerLauncher and the SERVER must be 
the first program to be invoked i.e. the quiz clients will fail with the message 
"java.rmi.ConnectException: Connection refused to host: 127.0.0.1" if there is no server running.

There are two CLIENT programs, QuizServerSetupClient and QuizServerPlayerClient. 

QuizServerSetupClient can be used to ADD a new Quiz or to Close (and remove) and existing quiz.
If a Quiz is closed, the server returns the name and highest score together with the associated winning player details. 
Once closed, the Game is removed and is no longer available for playing. It should be noted that 
a Quiz cannot be closed if someone is currently playing.

QuizServerPlayerClient is used to play an existing game. On invocation, this program requests 
a list of available quizzes from the server and presents these available quizzes to the user 
for them to select one. If there are no quizzes available then the message "No quizzes found" is returned.
If quizzes do exist, then the player selects the quiz and plays it. At the end of the game, the player
is given their score and this is forwarded to the server and, if the player has the highest score, the player score
and details are saved on the server.

The server persists player and quiz ids, quizzes and also highest scores and these details are reloaded when the 
server is invoked (or these details are created from scratch on initial server invocation).

HOW TO RUN THE PROGRAMS
-----------------------

1. launch Server : java -Djava.security.policy=securityPolicy.txt QuizServerLauncher
2. launch either Setup or Player Client as follows:
	SETUP 	- java -Djava.security.policy=securityPolicy.txt QuizServerSetupClient
	PLAYER 	- java -Djava.security.policy=securityPolicy.txt QuizServerPlayerClient
	
The security policy file needs to be located in same directory as the executables and is called securityPolicy.txt

The game data is persisted within quizzes.txt, which is also located within the same directory - If you want to start a new quiz session 
from scratch i.e. initialise all id's a delete all existing quizzes then delete quizzes.txt

The above programs do not take input parameters and the only property file is the security policy file