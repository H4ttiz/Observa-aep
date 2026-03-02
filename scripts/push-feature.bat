@echo off
echo.
git add .
set /p msg=Digite a mensagem do commit:
git commit -m "%msg%"
git push origin HEAD

echo.
echo Código enviado para o GitHub!
pause