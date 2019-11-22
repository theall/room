call build_client.bat
call build_server.bat

cd target
copy Client-0.jar ..\client.jar
copy Server-0.jar ..\server.jar
