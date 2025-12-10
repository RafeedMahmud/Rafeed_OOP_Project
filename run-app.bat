@echo off
echo Compiling GUI...

REM create bin folder if not exists
if not exist bin (
    mkdir bin
)

REM compile all model + ui classes
javac -d bin src\model\*.java src\ui\*.java

if errorlevel 1 (
    echo.
    echo Compile error. Fix the errors above, then run this file again.
    echo.
    pause
    exit /b
)

echo.
echo Running GUI with SQLite...

REM add sqlite-jdbc.jar to classpath
java -cp "bin;lib\sqlite-jdbc.jar" ui.MainWindow

echo.
pause
