@echo off
echo.

git add -A

git status

git diff --cached --quiet
if %errorlevel%==0 (
    echo.
    echo Nada para commit.
    pause
    exit
)

echo.
set /p msg=Digite a mensagem do commit:

git commit -m "%msg%"
git push

echo.
echo Codigo enviado para o GitHub!
pause