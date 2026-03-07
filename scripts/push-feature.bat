@echo off
echo.

git add .

git diff --cached --quiet
if %errorlevel%==0 (
    echo Nada para commit.
    pause
    exit
)

set /p msg=Digite a mensagem do commit:
git commit -m "%msg%"
git push origin HEAD

echo.
echo Codigo enviado para o GitHub!
pause