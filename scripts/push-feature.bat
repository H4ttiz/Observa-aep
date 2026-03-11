@echo off
echo.

git add -A

echo.
git status

git diff --cached --quiet
if %errorlevel%==0 (
    echo.
    echo Nada para commit.
    pause
    exit /b
)

echo.
set /p msg=Digite a mensagem do commit:

echo.
git commit -m "%msg%"

echo.
git push origin HEAD

echo.
echo Codigo enviado para o GitHub!
pause