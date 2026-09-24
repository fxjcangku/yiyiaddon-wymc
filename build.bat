@echo off
echo Building YiYi WYMC Loader...
echo.

cd /d "%~dp0"

REM 创建构建目录
if not exist "build\classes\agent" mkdir build\classes\agent
if not exist "build\classes\launcher" mkdir build\classes\launcher
if not exist "build\libs" mkdir build\libs

echo [1/6] 编译 Agent...
javac -encoding UTF-8 -d build\classes\agent ^
    wymc-agent\src\main\java\com\yiyiaddon\wymc\agent\*.java ^
    wymc-agent\src\main\java\com\yiyiaddon\wymc\agent\analysis\*.java

if errorlevel 1 (
    echo Agent 编译失败
    pause
    exit /b 1
)

echo [2/6] 打包 Agent JAR...
cd build\classes\agent
jar cfm ..\..\..\build\libs\wymc-agent.jar META-INF\MANIFEST.MF com\yiyiaddon\wymc\agent\*.class com\yiyiaddon\wymc\agent\analysis\*.class
cd ..\..\..

echo [3/6] 下载依赖...
if not exist "build\libs\gson-2.10.1.jar" (
    echo 下载 Gson...
    curl -L -o build\libs\gson-2.10.1.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar
)
if not exist "build\libs\slf4j-api-2.0.9.jar" (
    echo 下载 SLF4J API...
    curl -L -o build\libs\slf4j-api-2.0.9.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar
)
if not exist "build\libs\logback-classic-1.4.11.jar" (
    echo 下载 Logback Classic...
    curl -L -o build\libs\logback-classic-1.4.11.jar https://repo1.maven.org/maven2/ch/qos/logback/logback-classic/1.4.11/logback-classic-1.4.11.jar
)
if not exist "build\libs\logback-core-1.4.11.jar" (
    echo 下载 Logback Core...
    curl -L -o build\libs\logback-core-1.4.11.jar https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.4.11/logback-core-1.4.11.jar
)
if not exist "build\libs\jna-5.13.0.jar" (
    echo 下载 JNA...
    curl -L -o build\libs\jna-5.13.0.jar https://repo1.maven.org/maven2/net/java/dev/jna/jna/5.13.0/jna-5.13.0.jar
)
if not exist "build\libs\jna-platform-5.13.0.jar" (
    echo 下载 JNA Platform...
    curl -L -o build\libs\jna-platform-5.13.0.jar https://repo1.maven.org/maven2/net/java/dev/jna/jna-platform/5.13.0/jna-platform-5.13.0.jar
)

echo [4/6] 编译 Launcher...
javac -encoding UTF-8 -cp "build\libs\*" -d build\classes\launcher ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\*.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\ui\*.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\process\*.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\environment\*.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\attach\*.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\analysis\runtime\*.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\report\*.java

if errorlevel 1 (
    echo Launcher 编译失败
    pause
    exit /b 1
)

echo [5/6] 复制资源文件...
xcopy /Y /Q wymc-launcher\src\main\resources\*.* build\classes\launcher\

echo [6/6] 打包 Launcher JAR...
cd build\classes\launcher
jar xf ..\..\libs\gson-2.10.1.jar
jar xf ..\..\libs\slf4j-api-2.0.9.jar
jar xf ..\..\libs\logback-classic-1.4.11.jar
jar xf ..\..\libs\logback-core-1.4.11.jar
jar xf ..\..\libs\jna-5.13.0.jar
jar xf ..\..\libs\jna-platform-5.13.0.jar
del /Q META-INF\*.SF META-INF\*.DSA META-INF\*.RSA 2>nul
jar cfe ..\..\libs\YiYi-WYMC-Loader.jar com.yiyiaddon.wymc.launcher.WymcLauncher .
cd ..\..\..

echo.
echo 构建完成！
echo Agent JAR: build\libs\wymc-agent.jar
echo Launcher JAR: build\libs\YiYi-WYMC-Loader.jar
echo.
echo 运行: java -jar build\libs\YiYi-WYMC-Loader.jar
pause
