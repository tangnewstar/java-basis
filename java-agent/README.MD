
## Java Agent
### Premain模式（启动时加载）
java -jar 启动时增加JVM参数可加载agent
```shell
# 简单的agent，在程序入口处调用agent的premain方法，支持传参
-javaagent:D:\code\java-basis\java-agent\premain-agent\target\premain-agent.jar=hahaha
# 异常的agent，如可出异常程序main方法不会执行
-javaagent:D:\code\java-basis\java-agent\premain-agent-error\target\premain-agent-error.jar
```
- 必须通过-javaagent:agent.jar作为JVM参数指定
- 要求MANIFEST.MF中声明Premain-Class属性
- 必须实现premain(String args, Instrumentation inst)方法 
> 若尝试通过VirtualMachine.loadAgent()加载这种Agent，会抛出**"Agent-Class not found"**错误

### Agentmain模式（运行时Attach）
指定JVM进程和加载的agent，即可实现运行时附加agent
```shell
java -jar runtime-test.jar
id=25672
agent=D:\code\java-basis\java-agent\agentmain-agent\target\agentmain-agent.jar
```
- 通过VirtualMachine.attach()+loadAgent()动态加载
- 要求MANIFEST.MF中声明Agent-Class属性
- 必须实现agentmain(String args, Instrumentation inst)方法
> 若尝试通过-javaagent参数加载这种Agent，会抛出**"Premain-Class not found"**错误

