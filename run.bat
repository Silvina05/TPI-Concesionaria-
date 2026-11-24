@echo off
setlocal
cd /d "%~dp0"
echo Ejecutando Sistema de Concesionaria Multimarcas UTN FRRe...
java -cp bin ar.edu.utn.frre.concesionaria.Main
endlocal
