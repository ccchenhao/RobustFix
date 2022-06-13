官网https://github.com/Meituan-Dianping/Robust

# 背景

因为Robust停止更新了，所以这里自己对源码做部分修改：

1、为了支持gradle高版本和Android11，这边做的简单处理就是移除计算ApkHash，demo中gradle版本为7.1.3

2、fix add方法修复失败

3、add新类时打patch包失败，所以d8替换dx打patch包，但碰到lambda无法打patch包，不知道是d8命令出错还是javassist的问题，有大佬能搞定吗，临时方案是通过modify所在方法+add新类代替lambda

目前测试了android12，混淆和非混淆下，modify修复方法，add新增方法和类正常。

# 使用方法

1. 在App的build.gradle，加入如下依赖

   ```java
   apply plugin: 'com.android.application'
   ....
   //制作补丁时将这个打开，这两插件放其他插件后面
   //apply plugin: 'auto-patch-plugin'
   apply plugin: 'robust'
       
   ```
2. 在整个项目的build.gradle加入classpath

   ```java
    buildscript {
       repositories {
                 maven { url 'https://jitpack.io' }
       }
       dependencies {
            classpath 'com.github.ccchenhao.RobustFix:auto-patch-plugin:1.0.0'
            classpath 'com.github.ccchenhao.RobustFix:gradle-plugin:1.0.0'
      }
   }
   ```
3. 使用

   1、拷贝：demo中的PatchManipulateImp，RobustCallBackSample和patch module到自己目录下
   将demo中app下的robust.xml配置文件和robust整个文件夹拷贝一份到自己的app目录下

   1.1如果想debug环境热修复，forceInsert=true

   1.2 hotfixPackage中修改需要插桩的包名      
   2、打开apply plugin: 'robust'，安装apk成功即插桩完成。

   3、保存第二步运行结果中app/build/outputs/robust/methodsMap.robust和app/build/outputs/mapping/release/mapping.txt到robust目录下。

   如果没有开启混淆(对应proguard得设为false)，发现没有混淆时不需要mapping文件也可以，但官网说需要。

   如果开启了混淆，gradle 3.6及以上版本默认启用R8，会将插入的ChangeQuickRedirect变量优化掉，需要在混淆文件proguard-rules.pro中加入以下代码。
   -keepclassmembers class **{
   public static com.meituan.robust.ChangeQuickRedirect *;
   }

   4、打开apply plugin: 'auto-patch-plugin' ，使用修复看下面，再运行
   ![补丁制作成功图片](https://cdn.nlark.com/yuque/0/2022/png/642203/1652066391214-46bfd6f2-b790-41a8-b065-6289ac4e7a53.png)
   就说明patch.dex成功生成，在/build/outputs/robust/patch.jar。

   5、测试：将patch包保存到项目的外部存储目录中，自己项目注意命令修改
   ```java
   adb push /Users/chenhmr/Documents/Robust/app/build/outputs/robust/patch.jar /storage/emulated/0/Android/data/com.meituan.robust.sample/cache/robust/patch.jar    
   ```

   6、调用new PatchExecutor(getApplicationContext(), new PatchManipulateImp(), new RobustCallBackSample()).start();进行热修复
   将robust.xml的patchLog设为true，robust查看日志

# 使用修复

1、对于新添加的方法，使用@Add注解新方法即可，所在类的某个方法必须有modify注解(即使不用随便注解个)，否则无法生成patch类。

2、对于修改方法，使用@Modify注解方法或方法内部加RobustModify.modify，要注意修复的到底是方法还是lambda中的代码

2.1对于修复java lambda中的，可以直接lambda中RobustModify.modify()

2.2对于修复kotlin lambda中的，因javassist问题无法编译，所以只能通过@modify lambda所在方法+@Add新类代替lambda。

2.3对于modify方法中的，@modify所在方法，但要注意的是如果该方法中有java lambda和kotlin lambda，打patch包有问题的，目前临时解决方案是也是用2.2方式代替。

# 注意事项和碰到的问题

问题:robust插件发现插桩失败

1、robust.xml的turnOnRobust，forceInsert有没有开启

2、robust.xml的hotfixPackage是否有需要插桩的包名开头

3、只有一行代码的方法没插桩是正常的


问题:@modify打patch包失败
![图片1](https://cdn.nlark.com/yuque/0/2022/png/642203/1651913466600-bcac8301-c5ab-4648-97e8-c37189043602.png)

methodsMap.robust没包含这个方法，可能这个方法是没被插桩或者重新运行了但methodsMap.robust没重新拷贝到robust下。

没插桩这个方法的原因看上面。

问题：d8打patch包失败
![](https://cdn.nlark.com/yuque/0/2022/png/642203/1654087455768-ef208b69-390a-412b-932b-ca5a32255f33.png)

![](https://cdn.nlark.com/yuque/0/2022/png/642203/1654087391638-1ea98490-80c9-4931-80cd-1da5639fdece.png)

说明modify方法中有java lambda或kotlin lambda，解决是modify+add新类代替

其他注意事项可以看官网[Wiki](https://github.com/Meituan-Dianping/Robust/wiki)


 
