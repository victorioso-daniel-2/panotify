@echo off
echo Compiling PaNotify Application

REM Set JavaFX path to the user's installation
set JAVAFX_PATH=D:\School\JAVA\javafx-sdk-24.0.1\lib
echo Using JavaFX from: %JAVAFX_PATH%

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Compile the application with specific source files
javac -d bin -cp ".;%JAVAFX_PATH%\*;lib\*" ^
  src\com\panotify\Main.java ^
  src\com\panotify\model\*.java ^
  src\com\panotify\service\*.java ^
  src\com\panotify\util\*.java ^
  src\com\panotify\ui\*.java ^
  src\com\panotify\ui\student\*.java ^
  src\com\panotify\ui\instructor\*.java

if %ERRORLEVEL% NEQ 0 (
  echo Compilation failed
) else (
  echo Compilation succeeded
)

pause 