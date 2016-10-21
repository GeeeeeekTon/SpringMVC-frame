1.从svn下载下来，应该只需要修改下数据库链接（spring_jdbc.properties）就行：
	上面用的是我本机数据库：用户名是root 密码是V1YGs87ezBBN+Nr0ivTre9tqte/5QVeygWmmseN0rlb4ntkS23IrNCZ3Ien2TdjdW1RbeTwglQWtCgr6fKqALQ==    明文是【password】
	192.168.0.131数据库：用户名是root 密码是bNVOqb7WKLX5Bjnw+LMv92taj25KOxDimXxILPQjw42wgv+1lHzOH8kr97xDwWdhpY67QuYCS7sWN4W46YbkFA==    明文是【root】
	上面都已经建立了rbc数据库，并且建立了一个表da_retrain_area作为演示。
	其他配置都是连接的131、132虚拟机上提供的服务，可以自己随便看看！
2.页面驱动customPageAjax演示使用：更多支持的操作符查看MySearchFilter
http://localhost/rbc/area/customPageAjax?order_status=asc&order_name=desc&iDisplayStart=6&pageSize=3&search_IN_name=zz
3.机构首页模板管理：dongao.properties的template.path：
/**模板管理的存储路径:支持自定义存储到硬盘上任何位置，要书写绝对路径，集群情况，网络文件系统（NFS，Network File System）
nfs是一种将远程主机上的分区（目录）经网络挂载到本地系统的一种机制，通过对网络文件系统的支持，
用户可以在本地系统上像操作本地分区一样来对远程主机的共享分区（目录）进行操作。*/

补充：
修改tomcat server.xml如下可自动加载类似r500：
<Context docBase="C:\Users\zhaominglei\Workspaces\MyEclipse 8.5\rbc\web" path="/rbc" reloadable="true"/>

服务治理
dubbo 					http://alibaba.github.io/dubbo-doc-static/User+Guide-zh.htm

jquery插件：

插件名称  					参考地址
datatable 				http://datatables.net/release-datatables/examples/
jbox					http://jjsp.jinjiang.gov.cn:8888/js/jbox-v2.3/jbox-demo.html
validform				http://validform.rjboy.cn/
ztree					http://www.ztree.me/v3/demo.php
chosen					http://harvesthq.github.io/chosen/
My97DatePicker			http://www.my97.net/dp/demo/index.htm
linkageSel 				http://linkagesel.xiaozhong.biz/

注意事项：
1、访问 http://localhost/rbc/dubbo/test  可以看到通过dubbo方式调用的远程数据分页列表【演示用的是课程商品】  这个就是例子 已经提交到svn了  谁有远程调用的类似需求  参考下这个就行了  都加注释了。
2、mapper如果需要自定义方法 方法名称不要包含Page 因为在PageInterceptor里 会拦截根据命名拦截这类方法自动构造select count(0)和 limit语句，mapper.xml中 selectByPage和commonBySqlPage 
就是为了分页使用的，其他情况不要复用此配置完成其他的功能哦。
3、dubbo因为没有按照文档中推荐的方式使用，所以配置实用复杂麻烦了，涉及到系统间调用的方法，简单策略就是都放到这个DubboService里集中管理，如果要提供服务给对方，也尽量都放到一个DubboServiceSpi类里，
集中管理，再把注释加的全点了。
4、熟悉Firebug的同学，可以在我们的项目里记录日志了:  $.log('调试信息1').log('调试信息2').log('调试信息3'); 或者  $('#someDiv').hide().log('div hidden').addClass('someClass');
5、标准的crud要参考地区或者课程类别管理，不要把简单需求功能复杂化，书写的代码除了完成功能外，要可以复用和扩展，至少要偏于排查锁定问题，基本的质量要保证。
6、对于陌生的东西，要增加自己学习、理解、快速掌握的能力,做之前一定先要把思路理清晰，知道在做什么。
7、http://localhost/rbc/dubbo/testMap?map['one']=one&map['two']=one&map['two']=one1  这个例子，演示springmvc如何直接绑定map，从而简化dubbo入参构造。

业务问答：
一个年度 可以有多个商品？yes    24课时  48课时
1个课表可以是多个type吗？ no  
但是同一个年度 周期 同一个类型下  是不是只能有一个课表 yes。

激活学习卡主要逻辑：
1、校验卡、密码，是否过期，是否已使用。
2、展示年度时，需要根据card里边的area_business_rule_id（是da_retrain_partner_period_area_year_rule表的主键）字段去da_retrain_partner_period_area_year_rule表里边找合作周期partner_period_id,再去da_retrain_partner_period_year找到合作年度，但需要排除da_retrain_account中对应用户有效的年度课程。
3、开课，根据合作周期、用户选的年度和用户所属地区去da_retrain_partner_period_area_year_rule找唯一一条数据，往da_retrain_account插入相应数据。

正确配置dongao.properties

default 16
生成静态页
http://rbc.dongaoacc.com/rbc/template/generateIndex?id=16
合作机构id为16的静态页面已经生成了，为了方便测试，上传到svn了，所以这步可以省略

访问静态页
http://rbc.dongaoacc.com/rbc/static/heb/index.html
访问用户中心，此时partnerId已经在cookie里了
http://rbc.dongaoacc.com/rbc/u/index

开发环境：ide

php ucenter
http://192.168.0.132  
密码是123456

mq
http://192.168.0.132:8161/admin
用户名：admin
密码：admin


新框架 
虚拟机 ssh登录账号
192.168.0.132
user:root
pass:DongAo@test2

192.168.0.131
user:root
pass:DongAo@test1
后改为
liuzhiyi
1q2w!Q@W

数据库在131上
账号:root
密码:root
后改为
liuzhiyi
aaaaaa

为了方便大家更好的找寻页面，现将新继教相关页面整合到一个统一的文件夹内，以后技术人员在新路径中查看相关页面。这个目录将会成为之后的提交目录，大家记得更新哦。
svn路径：https://svn.test.com/svn/ued/新继教静态页

测试
http://192.168.0.144/ucenter/sysUser/toLogin
admin
admin

192.168.0.141 对应ip 114.255.153.233
ssh 
liuzhiyi
yanglin@dongao


支付宝：dongao_test@163.com
密码：dongao123

dubbo-admin 管理控制台 
http://192.168.0.132:8089
用户:root,密码:root 或 用户:guest,密码:guest
http://192.168.0.131:8089
用户:root,密码:root 或 用户:guest,密码:guest
http://192.168.0.144:8090
用户:root,密码:root 或 用户:guest,密码:guest

线上环境dubbo管理
http://dubbomonitor.dongaoacc.com/
root
1q2w!Q@W

查看主从状态信息;
mysql -h"192.168.0.131"  -uliuzhiyi -paaaaaa;
show master status \G;

mysql -h"192.168.0.132"  -uliuzhiyi -paaaaaa;
show slave status \G;

spring版本: 3.2.3.RELEASE

******Cache 注解使用说明*************

@Cachable 在类上（一般是*ServiceImpl）标注，说明该类的至少一个方法用到了缓存。

@CachePut 
	entity 要缓存的实体类
	isPojo 要缓存的内容是否是单纯的pojo，一般就看方法的返回值即可，不是pojo的情况：返回值为基本数据类型或者基本数据类型对应的包装类，String或者自定义对象等，这时要定义isPojo为false。默认为true，代表纯的领域对象pojo。
	relEntitys 一般无需定义，如果对应获取要缓存内容的途径来源于多个pojo，需要定义该属性，如dao的实现用到了多表关联
或者整体上对一个大的业务方法结果进行缓存，但是这个结果来源于多个dao的组合，这时需要手动定义该属性。

@CacheClear
	entitys 要清空的目标实体类数组，一般情况只有一个，但是一个类的方的涉及到多个实体的修改、增加、删除，就要定义对
应多个实体类。

eg：

class A{} 
class B{}

@Cachable
public class DemoServiceImpl implements XXXX{
	@CachePut(entity = A.class)
	public A load(Long id){};

	@CachePut(entity = A.class)
	public A getAbyDefine(A a){}

	@CachePut(entity = A.class,isPojo=false)
	public int  count(A a){}

	@CachePut(entity = A.class,isPojo=false)
	public Adto getAbyDefine(A a){}

	@CachePut(entity = A.class)
	public List<A> getlistbyDefine(A a){}

	@CachePut(entity = A.class,isPojo=false)
	public List<Adto> getlistbyDefine(A a){}

	@CachePut(entity = A.class,isPojo=false,relEntitys={A.class,B.class})
	public Map getMapByAadnB(A a,B b){
		map.put("a",a);// or  map.put("as",new ArrayList<A>());
		map.put("b",b);// or map.put("bs",new ArrayList<B>());
	}

	@CacheClear(entitys = { A.class })
	public void update(A a) {};

	@CacheClear(entitys = { A.class })
	public void delete(A a) {};

	@CacheClear(entitys = { A.class })
	public void save(A a) {};

	@CacheClear(entitys = { A.class,B.class })
	public void saveAandB(A a,B b) {};

	//TODO 待补充

}

注：
1.	A.class B.class 就是单纯的领域对象 pojo。

2.	有的时候一个大的业务方法，本身没有必要进行缓存【加的话也可以，就是粒度很粗，参考上面public Map getMapByAadnB()使用】，但是其中的大多数的每一个步骤是可以被缓存的。这时候，可以把这个方法 拆成几个小方法，在小方法是加上注解，
            但是引用的时候，要注意，涉及到类自身方法引用的话，比如 C类中有方法 a 和b ，b方法本身不管是否要用到缓存，但是其中的一步会调用a，这时候不能直接调用a方法，应该：
           C c = (C)AopContext.currentProxy(); 再c.a()方法。a()上的缓存才会起效。

3.	@CachePut  有一个CacheModuleType类型的属性，如果要缓存的一个结果是确定属于一个用户，或者一个周期的就应该定义此属性，可以提高性能。如用户选课，a用户选课    对用户选课表的操作   不会影响到 b用户。
                     暂时支持：两个维度【目标周期，目标用户】，针对前台而言，已经通过ThreadHandlerInterceptor统一把当前合作周期，当前合作用户放到了MyThreadVariable里， 
                     后台如果对指定用户或者指定合作周期操作，要手动维护下MyThreadVariable的目标周期和目标用户的值。

使用起来，其实很简单，什么时候加什么，就看方法的参数里是否包含周期id，如果包含就定义CacheModuleType.OPERATEPERIODID，如果包含用户id
，就定义CacheModuleType.OPERATEUSERID，如果既包含了周期id，又包含了用户id，则定义CacheModuleType.OPERATEUSERID，否则不用定义此值。

4.CacheMonitorController类对缓存【仅包含RbcDbCache这个区域】，进行了一些监控，和手动清除不同维度，实体缓存的方法。
    目前调用此方法仅仅需要登陆，并未有其他权限限制，打算先基于初始化的角色或者指定用户进行权限的限制，简化权限保护。


使用缓存注意的地方：

1.不要忘记同一个类之间的方法相互调用，注意加：CourseItemServiceImpl service=(CourseItemServiceImpl) AopContext.currentProxy();

2. 调用mapper load不会被缓存。 暂时问题,在想办法去解决。

3. service.method  返回值 为null的情况  不会被缓存。暂时问题,在想办法去解决。

4. 方法权限问题 ，不要写private，因为aop拦截不到，部分逻辑还会引发异常。这个要注意。

TODO ： 但是请求还是 有些方法有时间重构下  尤其dubbo远程调用 这里还是可以优化的

5. 注意 ThreadHandlerInterceptor 拦截不到的地方、以及后台、以及异步操作，要注意手动设置线程变量，以清空缓存，特别注意异步操作。

6. 缓存支持事物后：要注意好的编程习惯:如一个类有一个List类型的属性，如果确定要自己维护这个list的内容，不要tag.getCourseItems().add(ct);//不要这样用，习惯不好，反面教材，应该自己
    List<CourseItem> courseItems=new ArrayList<CourseItem>();然后按照逻辑填充这个courseItems，然后再tag.setCourseItems(courseItems);

7. 缓存是支持事物的，尤其是动作：save update delete 操作后， 直接查询 查的仍旧是旧的缓存哦，只有事物提交了，旧的缓存才会被刷新。所以考虑是否去掉对清除缓存的事物支持。



core分页   foreache 修改了 明雷 

记得输出 页面 或者程序日志 debug业务逻辑

add variable attributes to generated class files

===========================================
用户相关缓存：
da_retrain_account   show_type=0
da_retrain_student_elective
da_retrain_study_log
da_retrain_exam_log
da_retrain_user_base_info


da_retrain_student_elective_history
da_retrain_study_log_history
da_retrain_exam_log_history


da_retrain_student_info   明雷
da_retrain_user_year_info 建议加userId



变量放在主存区上，使用该变量的每个线程，都将从主存区拷贝一份到自己的工作区上进行操作。

volatile, 声明这个字段易变（可能被多个线程使用），Java内存模型负责各个线程的工作区与主存区的该字段的值保持同步，即一致性。

static, 声明这个字段是静态的（可能被多个实例共享），在主存区上该类的所有实例的该字段为同一个变量，即唯一性。

volatile, 声明变量值的一致性；static,声明变量的唯一性。

此外，volatile同步机制不同于synchronized, 前者是内存同步，后者不仅包含内存同步（一致性），且保证线程互斥（互斥性）。
static 只是声明变量在主存上的唯一性，不能保证工作区与主存区变量值的一致性；除非变量的值是不可变的，即再加上final的修饰符，否则static声明的变量，不是线程安全的。

快速本地开发调试步骤：（学员中心前台）
1. 设置host 如127.0.0.1    rbcmy.dongaoacc.com
2. 保证rbc项目中dongao.properties的配置developModeEnable=true
3. 请求这个地址进行当前用户和当前合作机构的一键设置，其中userId为数据库中对应的用户id，partnerId为对数据库对应的合作机构id， http://rbcmy.dongaoacc.com/rbc/develop/simulateUserAndPeriod?userId=9c2822d07b604a72afb5c332abbef851&partnerId=48&openKey=Te@cherMe
4.访问学员中心首页，http://rbcmy.dongaoacc.com/rbc/u/index   发现当前用户和当前合作机构已经被设置上。
5.进行学员中心的debug和需求开发。

快速本地开发调试步骤（各项目后台）：
1. 设置host 如127.0.0.1    my.dongaoacc.com
2. 保证各项目中dongao.properties的配置developModeEnable=true
3. 请求这个地址进行当前用户，其中userId为数据库中对应的用户id，演示用的userid，就是admin，所以大部分权限都有了 http://my.dongaoacc.com:84/ucenter/develop/simulateUser?openKey=Te@cherMe&userId=9c2822d07b604a72afb5c332abbef851
4. 成功后，自动调整到后台首页。
5. 调试其他项目，下一步就是想调试哪个就启动哪个，按照如下端口进行设置，然后点击就和线上后台操作一模一样了，发现也可以debug了。

注意1.要保证本地dongao.properties的配置developModeEnable=true
	2.要保证各个项目的端口设置和develop_menu.properties一致，不一致也可以，就是你就要对应修改
	3.入口在ucenter，所以想调试其他项目，也得启动ucenter先

document.domain的设置
1.单用户数据 未涉及到绝对安全的 简单安全 也可以加密   设置有效期  只读等安全策略 跨浏览器 跨窗口通信  应该优先给予客户端技术  如js flash  cookie 特定的浏览器支持
2.多用户数据  涉及到安全的 应该基于服务端存储  服务端推送


又有随堂练习，又有考试的地方暂时程序上是不支持的。
