Server, Cli and Gui are separated into 3 different .jar files, so that the server and cli .jar can be lightweight.

SERVER

Usage:
>> java -jar SantoriniServer.jar
to use a random port
>> java -jar SantoriniServer.jar --port=PORT
to use PORT as the listening port

If not specified, a random PORT is used, which is printed to the command line.
The server socket is always bound to all interfaces.

Notes:
game.ser is saved in the working directory when a match is interrupted.
card-config.xml and game.ser must be in the working directory to be read

CLI/GUI

Usage:
(cli)
>> java -jar SantoriniCli.jar --ip=IP --port=PORT
(gui)
>> java -jar SantoriniGui.jar --ip=IP --port=PORT
or
(cli)
>> java -jar SantoriniCli.jar --addr=IP --port=PORT
(gui)
>> java -jar SantoriniGui.jar --addr=IP --port=PORT
to connect to the socket at IP:PORT

If not specified, defaults are IP=127.0.0.1 and PORT=38612
