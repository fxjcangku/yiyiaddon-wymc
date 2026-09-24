@echo off
cd /d "%~dp0"

set JAVAFX_PATH=build\libs\deps
set JAVAFX_MODULES=javafx.controls,javafx.fxml

java --module-path %JAVAFX_PATH% --add-modules %JAVAFX_MODULES% -jar build\libs\YiYi-WYMC-Loader.jar

pause
