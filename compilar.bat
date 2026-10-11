@echo off
REM Compila o mod. Requer JDK 8 (JAVA_HOME apontando para ele) e internet na 1a vez.
if exist gradlew.bat (
  call gradlew.bat build
) else (
  echo gradlew.bat nao encontrado. Copie gradlew, gradlew.bat e a pasta gradle\wrapper do MDK do Forge 1.16.5-36.2.39,
  echo ou instale o Gradle 7.4.2 e rode: gradle build
  gradle build
)
echo.
echo O JAR final fica em build\libs\estatuapersonalizada-1.0.0.jar
pause
