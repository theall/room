call build_game.bat
call build_client.bat
rem call build_server.bat

copy target\Client-0.jar .\client.jar
copy target\Game-0.jar .\demo.jar
rem copy Server-0.jar .\server.jar
