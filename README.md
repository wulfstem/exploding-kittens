# ExplodingKittens61

Hello from Team 61, Ervinas and Ugnius! We hope that you will have a smooth setup of Exploding Kittens and find this guide useful.
*<span style="color:red">This project was created using IntelliJ IDEA, and works best when run on this platform. For VSCode you will need to add .json files for configuration.</span>

---

## What can be found where?

Our project is divided into 4 main directories, with the fifth one consisting of our few tests:

- `client`
- `exploding_kittens`
- `local`
- `server`
- `tests`

**CLIENT:**

Includes all classes related to playing a game of Exploding Kittens through a network from the client's side. It has:

- `ExplodingKittensClient` - Direct connection to the server and is responsible for managing it.
- `ClientController` - The main brains of a client, initializes through its main method to start a client.
- Extra subclasses of `ClientController` for concurrent play with multiple players (clients).
- `ClientTUI` - Responsible for textual user interface and receiving input from the user directly.

**EXPLODING_KITTENS:**

Includes the basic game logic and all the resources used by different Controllers to run an Exploding Kittens game. It has:

- Interfaces for `PlayerTUI` and `Controller`.
- Classes for game functionality, including custom Exception classes, `Card` class and its subclasses, `Player` class and its subclass `Computer`, `Game` class, `Deck` class, and `Hand` class.

**LOCAL:**

Includes `LocalController` class, which is the control panel for running a game of Exploding Kittens locally (on the same terminal). It is a subclass of `Controller` and is adapted to work with the basic game logic from the `exploding_kittens` directory. This directory also includes `LocalPlayerTUI`, responsible for communication with the players.

**SERVER:**

Includes:

- `ServerTUI` - Responsible for any output shown on the server console.
- `ExplodingKittensServer` - Initializes and accepts server connections, creating new threads to handle separate clients (players) through `ClientHandler`.
- `ServerController` - The brains of hosting the server, starting the game, following the basic game logic, and controlling communication with different clients and taking their requests.

**Chat functionality:** Our game includes the ability to chat among clients during gameplay, with a separate server called `ChatServer` and `ChatClientHandler` in the server's subdirectory `chat`. The client directory contains a subdirectory `chat`, where `ChatClient` class and its subclasses are located and should be run through their main methods to start the chat.

---

## How to start a game

### 1. Local gameplay

To play a game locally:

1. Right-click on the `LocalController` class.
2. Go to "More Run/Debug" and then to "Modify run configuration...".
3. In the "Program arguments" field, type the arguments following this template: `<number of players> <'1' if one of the players is a computer player and '0' if not> <namePlayer1> <namePlayer2> <namePlayer3>`.

	- Example: `3 1 Ervinas Matas` (game of 2 players with names Ervinas and Matas, and the third player is a computer player).

	- Be aware of the following rules for program arguments:
		- `<number of players>`: The total number of players, should be between 2 and 5.
		- `<'1' if one of the players is a computer player and '0' if not>`: 1 if one of the players is a computer player, 0 if not.
		- `<namePlayer1> <namePlayer2> <namePlayer3>`: Names of the players, separated by spaces.

4. Press "Apply" and "Okay".
5. Right-click the `LocalController` class and press "Run LocalController.main()".

### 2. Network gameplay

To play a game through a network:

1. Start a server:
	- Right-click on the `ServerController` class.
	- Go to "More Run/Debug" and then to "Modify run configuration...".
	- In the "Program arguments" field, type the arguments following this template: `<number of players> <'1' if one of the players is a computer player and '0' if not> <port number>`.

		- Example: `3 1 6744` (game of two players who will be connected through network on port 6744, and one local computer player).

		- Be aware of the following rules for program arguments:
			- `<number of players>`: The total number of players, should be between 2 and 5.
			- `<'1' if one of the players is a computer player and '0' if not>`: 1 if one of the players is a computer player, 0 if not.
			- `<port number>`: A valid port number for the server.

2. Press "Apply" and "Okay".
3. Right-click the `ServerController` class and press "Run ServerController.main()".

3. Modify the program arguments for the `ClientController` class:
	- In the "Program arguments" field, type the arguments following this template: `<serverAddress> <portNumber>`.

		- Example: `localhost 6744` (client is connecting to server with the address "localhost" on port 6744).

	- Be aware of the following rules for program arguments:
		- `<serverAddress>`: The address of the server.
		- `<portNumber>`: The port number to connect to.

4. Run the `ClientController` class.
5. Type in your nickname.
6. Repeat the process for each client/player you want to connect `Client2Controller`, `Client3Controller`, etc...

### Chat functionality

To use the chat functionality:

1. Run `ChatServer` class with program arguments being only the valid port number.
	- Examples: `6283`, `5422`.

2. After running `ChatServer`, each player(client) can initialize their `ChatClient` classes with program arguments `<serverAddress> <portNumber>` (similar to the program arguments in `ClientController` class). Then run `ChatClient.main()` for each client `Chat2Client.main()`, `Chat3Client.main()` etc...
3. Type in your nickname and start chatting with other clients connected to the chat server.

---

## References

Authors:

- Ervinas Vilkaitis | e.vilkaitis@student.utwente.nl
- Ugnius Tulaba | u.tulaba@student.utwente.nl

---
