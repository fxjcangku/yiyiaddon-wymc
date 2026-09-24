@echo off
REM 简化编译脚本 - 使用 Java 25 直接编译

echo ========================================
echo  YiYi WYMC Loader 快速编译脚本
echo ========================================
echo.

cd /d "%~dp0"

REM 创建目录
if not exist "build\classes\agent" mkdir build\classes\agent
if not exist "build\classes\launcher" mkdir build\classes\launcher
if not exist "build\libs" mkdir build\libs
if not exist "build\temp" mkdir build\temp

echo [1/5] 编译 Agent...
javac --release 17 -encoding UTF-8 -d build\classes\agent ^
    wymc-agent\src\main\java\com\yiyiaddon\wymc\agent\WymcAgent.java ^
    wymc-agent\src\main\java\com\yiyiaddon\wymc\agent\AgentBootstrap.java ^
    wymc-agent\src\main\java\com\yiyiaddon\wymc\agent\RuntimeContext.java ^
    wymc-agent\src\main\java\com\yiyiaddon\wymc\agent\analysis\ClassLoaderAnalyzer.java ^
    wymc-agent\src\main\java\com\yiyiaddon\wymc\agent\analysis\MinecraftClassProbe.java

if errorlevel 1 goto :error

echo [2/5] 打包 Agent JAR...
cd build\classes\agent
jar cfm ..\..\..\build\libs\wymc-agent.jar META-INF\MANIFEST.MF com
cd ..\..\..

echo [3/5] 下载依赖...
if not exist "build\libs\deps" mkdir build\libs\deps
cd build\libs\deps

if not exist "gson-2.10.1.jar" (
    echo 下载 Gson...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar' -OutFile 'gson-2.10.1.jar'"
)
if not exist "slf4j-api-2.0.9.jar" (
    echo 下载 SLF4J...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar' -OutFile 'slf4j-api-2.0.9.jar'"
)
if not exist "logback-classic-1.4.11.jar" (
    echo 下载 Logback Classic...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/ch/qos/logback/logback-classic/1.4.11/logback-classic-1.4.11.jar' -OutFile 'logback-classic-1.4.11.jar'"
)
if not exist "logback-core-1.4.11.jar" (
    echo 下载 Logback Core...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.4.11/logback-core-1.4.11.jar' -OutFile 'logback-core-1.4.11.jar'"
)
if not exist "jna-5.13.0.jar" (
    echo 下载 JNA...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/net/java/dev/jna/jna/5.13.0/jna-5.13.0.jar' -OutFile 'jna-5.13.0.jar'"
)
if not exist "jna-platform-5.13.0.jar" (
    echo 下载 JNA Platform...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/net/java/dev/jna/jna-platform/5.13.0/jna-platform-5.13.0.jar' -OutFile 'jna-platform-5.13.0.jar'"
)

cd ..\..\..

echo [4/5] 编译 Launcher...
javac --release 17 -encoding UTF-8 -cp "build\libs\deps\*" -d build\classes\launcher ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\WymcLauncher.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\ui\MainWindow.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\process\MinecraftProcessDetector.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\process\ProcessCandidate.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\environment\MinecraftVersionDetector.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\attach\AgentAttacher.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\attach\AttachResult.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\analysis\runtime\RuntimeAnalyzer.java ^
    wymc-launcher\src\main\java\com\yiyiaddon\wymc\launcher\report\ReportGenerator.java

if errorlevel 1 goto :error

echo [5/5] 打包 Launcher...
cd build\classes\launcher

REM 复制资源
xcopy /Y /Q ..\..\..\wymc-launcher\src\main\resources\*.* .

REM 解压依赖
jar xf ..\..\libs\deps\gson-2.10.1.jar
jar xf ..\..\libs\deps\slf4j-api-2.0.9.jar
jar xf ..\..\libs\deps\logback-classic-1.4.11.jar
jar xf ..\..\libs\deps\logback-core-1.4.11.jar
jar xf ..\..\libs\deps\jna-5.13.0.jar
jar xf ..\..\libs\deps\jna-platform-5.13.0.jar

REM 删除签名
if exist "META-INF\*.SF" del /Q META-INF\*.SF
if exist "META-INF\*.DSA" del /Q META-INF\*.DSA
if exist "META-INF\*.RSA" del /Q META-INF\*.RSA

REM 打包
jar cfe ..\..\libs\YiYi-WYMC-Loader.jar com.yiyiaddon.wymc.launcher.WymcLauncher .

cd ..\..\..

echo.
echo ========================================
echo  构建完成！
echo ========================================
echo  Agent: build\libs\wymc-agent.jar
echo  Launcher: build\libs\YiYi-WYMC-Loader.jar
echo.
echo  运行: java -jar build\libs\YiYi-WYMC-Loader.jar
echo ========================================
pause
exit /b 0

:error
echo.
echo ========================================
echo  构建失败！
echo ========================================
pause
exit /b 1
