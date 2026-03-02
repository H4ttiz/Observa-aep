@echo off
echo.
echo Atualizando branch develop...
git checkout develop
git pull origin develop

echo.
set /p branchName=Digite o nome da feature (ex: cadastro-usuario):

git checkout -b feature/%branchName%

echo.
echo Branch criada com sucesso!
pause