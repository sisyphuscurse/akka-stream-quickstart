
安装sbt
```
   brew install sbt
```


需要创建或编辑 ~/.sbt/repositories
```dtd
[repositories]
local
repox-maven: http://repox.gtan.com:8078/
repox-ivy: http://repox.gtan.com:8078/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]
```
如果你使用命令行，请在sbt命令行参数中添加 ```-Dsbt.override.build.repos=true``` 。例如我的sbt shell脚本的内容是这样的：
```
  #!/bin/sh
  export SBT_OPTS="-Dsbt.override.build.repos=true"
  exec java -Xmx512M ${SBT_OPTS} -jar $(dirname "$0")/sbt-launch.jar "$@"
```
如果使用jetbrains IDEA，修改 Preferences -> SBT -> JVM Options -> VM parameters，保证它包含
```
  -Dsbt.override.build.repos=true 
```