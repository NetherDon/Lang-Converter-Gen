@echo off

set dir=Compiler.jar
set dir_dep=Compiler-jar-with-dependencies.jar
set newdir=.\target\%dir_dep%

call mvn clean package
if exist "%dir%" (
    del %dir%
)
move %newdir% .\
ren %dir_dep% %dir%