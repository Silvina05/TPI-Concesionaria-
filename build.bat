@echo off
setlocal
cd /d "%~dp0"
if not exist "bin" mkdir "bin"
dir /s /b src\*.java > sources.txt
echo Compilando codigo Java...
javac -encoding UTF-8 -d bin @sources.txt
if %ERRORLEVEL% EQU 0 (
    echo [EXITO] Compilacion completada sin errores.
) else (
    echo [ERROR] Fallo en la compilacion.
)
del sources.txt
endlocal
