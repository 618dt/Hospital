## 一、项目简介与特点
### 1.项目简介
​	◇网上预约挂号系统参考北京市114预约挂号系统实现，网上预约挂号是近年来开展的一项便民就医服务，旨在缓解看病难、挂号难的就医难题。网上预约挂号全面提供的预约挂号业务从根本上解决了这一就医难题，患者可以在平台上的任意一家医院随时随地轻松挂号！不用排长队！
### 2.项目特点

- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，请求接口封装，错误码统一处理
- 前端采用Element-ui框架，极大的提高了开发效率
- 引入swagger文档支持，方便编写API接口文档
- 项目采用分布式架构，每个子项目可独立部署，扩展灵活，易于维护
- 使用NoSQL型数据库，将医院排班等信息存储在MongoDB中，提高数据的查询速度
- 使用SpringCloudGateway进行统一网关校验和请求转发，并解决跨域问题
### 3.项目业务流程
◇项目业务流程如图：
![业务流程.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1646032002195-72f9eee8-5c59-459a-8a96-f42ce5d4e009.png#clientId=u69f49bdf-8458-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=673&id=ufcc13ae2&margin=%5Bobject%20Object%5D&name=%E5%B0%9A%E5%8C%BB%E9%80%9A%E4%B8%9A%E5%8A%A1%E6%B5%81%E7%A8%8B.png&originHeight=841&originWidth=1189&originalType=binary&ratio=1&rotation=0&showTitle=false&size=90685&status=done&style=none&taskId=u7fed5bd6-3bb0-467a-8474-949215b3d46&title=&width=951.2)

## 二、项目模块介绍
### 1.项目模块组成
◇如下图所示，整个项目（appointment-parent）由多个子项目（service,common等）聚合而成，每个子项目各是一个独立可部署的JavaWeb项目（可以放到Tomcat中运行），各项目通过SpringCloud实现项目间服务调用，使得整个项目可以实现分布式部署。这种架构是分布式Java项目常采用的架构模型。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645796126400-bcfa17df-bfea-49c0-a3e3-e8fd342ebdc8.png#clientId=u827ca338-226f-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=638&id=u4a4484ed&margin=%5Bobject%20Object%5D&name=image.png&originHeight=797&originWidth=1173&originalType=binary&ratio=1&rotation=0&showTitle=false&size=49606&status=done&style=none&taskId=u03ec0510-54b7-489f-85ff-fee87e552f1&title=&width=938.4)
◇其中common模块包括了项目所需的基础工具类，如全局异常类，统一返回结果状态信息类，Redis配置类，service模块所需的服务类等。hospital-manage是医院系统模块。service是各个微服务的父模块，包含了cmn数据字典服务，医院服务，短信服务，订单服务等。service_client模块包含了远程服务提供者提供者的服务接口。service_gateway是网关模块，配置有请求的统一转发，请求过滤等信息。

### 2.项目服务架构
◇项目微服务架构图如下
![架构图.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1646033456298-faa05956-b620-48d1-9758-dba5e702dea8.png#clientId=u69f49bdf-8458-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=1421&id=u7315d8f6&margin=%5Bobject%20Object%5D&name=%E5%B0%9A%E5%8C%BB%E9%80%9A%E6%9E%B6%E6%9E%84%E5%9B%BE.png&originHeight=1776&originWidth=2293&originalType=binary&ratio=1&rotation=0&showTitle=false&size=659511&status=done&style=none&taskId=u0cf3b1db-2764-4d02-89fd-182467fcd96&title=&width=1834.4)

## 三、项目主要技术
### 1.前端技术
◇[Vue.js](https://cn.vuejs.org/index.html)：是一套用于构建用户界面的渐进式JavaScript框架。 与其它大型框架不同的是，Vue 被设计为可以自底向上逐层应用。Vue 的核心库只关注视图层，不仅易于上手，还便于与第三方库或既有项目整合。另一方面，当与现代化的工具链以及各种支持类库结合使用时，Vue 也完全能够为复杂的单页应用（[SPA](https://baike.baidu.com/item/SPA/17536313)）提供驱动。
◇[Element-ui](https://element.eleme.cn/#/zh-CN)：网站快速成型工具，一套为开发者、设计师和产品经理准备的基于 Vue 2.0 的桌面端组件库。
◇[Nuxt.js](https://www.nuxtjs.cn/)：基于 Vue.js 的轻量级应用框架,可用来创建服务端渲染 (SSR) 应用,也可充当静态站点引擎生成静态站点应用,具有优雅的代码结构分层和热加载等特性。
### 2.后端技术
#### 2.1 开发框架
◇[SpringBoot](https://start.spring.io/)：简化spring的搭建和开发过程的全新框架。
◇[MyBatis-Plus](https://baomidou.com/)：一个[MyBatis](https://mybatis.org/mybatis-3/)的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。
#### 2.2 数据库
◇[MongoDB](https://www.mongodb.com/)：一个跨平台的，面向文档的数据库，是当前 NoSQL 数据库产品中最热门的一种。
◇[MySQL](https://www.mysql.com/)：关系型数据库。
◇[Redis](https://redis.io/)：一个基于内存的高性能key-value数据库。
#### 2.3 SpringCloud微服务
◇[Nacos](https://nacos.io/zh-cn/docs/quick-start.html)：使用 Nacos 简化服务发现、配置管理、服务治理及管理的解决方案，让微服务的发现、管理、共享、组合更加容易。
◇[Gateway](https://spring.io/projects/spring-cloud-gateway)：为微服务架构提供一种简单而有效的统一的API路由管理方式。
◇[Feign](https://spring.io/projects/spring-cloud-openfeign)：一个声明式的Web服务客户端，使用Feign可使得Web服务客户端的写入更加方便。
#### 2.4 其他
◇消息中间件[RabbitMQ](https://www.rabbitmq.com/)：一个开源的AMQP实现，服务器端用Erlang语言编写，支持多种客户端，如：Python、Ruby、.NET、Java、JMS、C、PHP、ActionScript、XMPP、STOMP等，支持AJAX。用于在分布式系统中存储转发消息，在易用性、扩展性、高可用性等方面表现不俗。
◇Api接口文档工具[Swagger](https://swagger.io/)：一款RESTFUL接口的文档在线自动生成+功能测试功能软件。
## 四、项目主要功能
### 1.用户模块
#### 1.1 登录注册以及认证
◇用户首页，可以根据医院名称进行模糊查询，输入文字后可根据文字模糊查询出医院列表显示在下拉框中，点击可跳转至医院页面进行预约挂号。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645848378064-376da2a5-bb38-47e1-b66f-817e09887a76.png#clientId=u2f212fff-ad2a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=950&id=u935216d5&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1187&originWidth=1918&originalType=binary&ratio=1&rotation=0&showTitle=false&size=413149&status=done&style=none&taskId=u0b189a30-f8b4-49ef-b3a0-d60ba8b9a55&title=&width=1534.4)
◇用户注册功能，可以使用邮箱和微信登录。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645848554735-d1d00e1e-5593-4f4d-8de2-cc15c850f0b9.png#clientId=u2f212fff-ad2a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=567&id=uacb861bd&margin=%5Bobject%20Object%5D&name=image.png&originHeight=709&originWidth=1196&originalType=binary&ratio=1&rotation=0&showTitle=false&size=139820&status=done&style=none&taskId=uce34c309-b30f-472d-bfd4-b7d8742cb5f&title=&width=956.8)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645848532112-11c06114-0ef0-4ba4-9d5c-088cf1a0bf3c.png#clientId=u2f212fff-ad2a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=566&id=ud0355bcf&margin=%5Bobject%20Object%5D&name=image.png&originHeight=708&originWidth=1197&originalType=binary&ratio=1&rotation=0&showTitle=false&size=174247&status=done&style=none&taskId=u5323006b-1422-49da-a363-52f1e790527&title=&width=957.6)
◇登录成功后默认用户名为邮箱号，需要进行实名认证才能挂号。![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645849579723-0e8294d2-51bd-4820-81f1-1a3822898ea5.png#clientId=u9c405413-9261-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=330&id=uca3ce679&margin=%5Bobject%20Object%5D&name=image.png&originHeight=412&originWidth=1917&originalType=binary&ratio=1&rotation=0&showTitle=false&size=248197&status=done&style=none&taskId=u5e2f9dc2-66b6-4c02-bc67-24f70efc568&title=&width=1533.6)
◇用户实名认证功能，通过填写姓名，证件信息以及证件照方式进行实名认证，由平台管理员进行认证审批。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645850174586-36b13918-81b2-40dd-84af-cc5af2067d47.png#clientId=u9c405413-9261-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=776&id=u77472733&margin=%5Bobject%20Object%5D&name=image.png&originHeight=970&originWidth=1381&originalType=binary&ratio=1&rotation=0&showTitle=false&size=83587&status=done&style=none&taskId=u927af88b-c86d-466c-a5c1-9d57fd8ac41&title=&width=1104.8)
#### 1.2 用户就诊人管理
◇用户就诊人管理，用户能够对就诊人进行基本的增删改查操作。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645851170039-13512702-62f8-4507-8913-5c1f19ddca23.png#clientId=u9c405413-9261-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=573&id=ue35e8988&margin=%5Bobject%20Object%5D&name=image.png&originHeight=716&originWidth=1621&originalType=binary&ratio=1&rotation=0&showTitle=false&size=47375&status=done&style=none&taskId=u4d40a550-c25d-46e3-adcf-51fefcb90fb&title=&width=1296.8)
◇添加就诊人的相关信息。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645851321787-bfb725df-9a46-4b87-b59b-35a84649a4af.png#clientId=u9c405413-9261-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=1330&id=u270e8589&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1662&originWidth=1229&originalType=binary&ratio=1&rotation=0&showTitle=false&size=74771&status=done&style=none&taskId=u03d812d4-79a5-4153-8148-a8c3f0421bf&title=&width=983.2)
◇实现省市区三级联动下拉框以便就诊人选择住址![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645851481528-59663a73-f4b7-4b57-b25f-1fbb31ed2e70.png#clientId=u9c405413-9261-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=351&id=uba5dce13&margin=%5Bobject%20Object%5D&name=image.png&originHeight=439&originWidth=1161&originalType=binary&ratio=1&rotation=0&showTitle=false&size=30174&status=done&style=none&taskId=ucfd72d0c-60f8-4cd2-96b4-d2c542945a5&title=&width=928.8)
#### 1.3 预约挂号功能
◇预约挂号功能，能够根据自身情况选择对应的医院科室进行预约挂号。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645851076761-33ed8ac3-282e-4766-a21c-939356597052.png#clientId=u9c405413-9261-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=550&id=dMlt8&margin=%5Bobject%20Object%5D&name=image.png&originHeight=687&originWidth=1615&originalType=binary&ratio=1&rotation=0&showTitle=false&size=52762&status=done&style=none&taskId=u369adfa1-dc8e-4bdf-a962-752776d0b9e&title=&width=1292)
◇点击剩余按钮后进入确认挂号页面，选择就诊人进行预约挂号。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645851700157-b3f19c99-600b-43e7-ad63-b09a9470a54a.png#clientId=u9c405413-9261-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=1322&id=u0f8d1b90&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1652&originWidth=1415&originalType=binary&ratio=1&rotation=0&showTitle=false&size=92938&status=done&style=none&taskId=ufcdc7106-62ba-467e-a1cb-d0d4e9bfc85&title=&width=1132)
◇当点击确认挂号后能够收到邮件提醒，预约成功后不进行支付可以直接取消。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645861133423-78d4bcb1-3a74-4c03-bad1-5fb7b3e1d445.png#clientId=u23d5cfb8-9a7d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=430&id=u4e4fec63&margin=%5Bobject%20Object%5D&name=image.png&originHeight=538&originWidth=1499&originalType=binary&ratio=1&rotation=0&showTitle=false&size=100468&status=done&style=none&taskId=u3d51a407-ac70-4696-9e75-ca698f6994b&title=&width=1199.2)
◇预约成功跳转到待支付页面。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645852676324-b396e243-17d8-407d-abcf-5d25dd8742aa.png#clientId=u64daafa0-70ea-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=641&id=ude422d32&margin=%5Bobject%20Object%5D&name=image.png&originHeight=801&originWidth=1341&originalType=binary&ratio=1&rotation=0&showTitle=false&size=59439&status=done&style=none&taskId=u6b448d26-99a0-4424-a692-66da85230cb&title=&width=1072.8)
◇使用微信支付![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645852702905-5e0507a6-a190-4348-a7f4-c4e98c44c6b9.png#clientId=u64daafa0-70ea-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=583&id=u9ae06b06&margin=%5Bobject%20Object%5D&name=image.png&originHeight=729&originWidth=1307&originalType=binary&ratio=1&rotation=0&showTitle=false&size=60228&status=done&style=none&taskId=ubfe0c53a-0986-4a8b-a388-44c15da6ca8&title=&width=1045.6)
◇支付成功后订单状态变为已支付。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645852803503-4d2d84c4-475b-4377-86ad-06261ec0e2e6.png#clientId=u64daafa0-70ea-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=334&id=uba2e91fa&margin=%5Bobject%20Object%5D&name=image.png&originHeight=417&originWidth=1331&originalType=binary&ratio=1&rotation=0&showTitle=false&size=28218&status=done&style=none&taskId=u68ddd2a8-0a53-4a11-8461-6c6e469b326&title=&width=1064.8)
◇支付成功后取消预约能够进行全额退款，取消预约后也会有邮件提醒。
![Screenshot_20220226_141911_com.tencent.mm.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645857008975-26865b8c-2324-48b8-b741-484c9d542dec.png#clientId=u23d5cfb8-9a7d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=380&id=u9e4a0530&margin=%5Bobject%20Object%5D&name=Screenshot_20220226_141911_com.tencent.mm.png&originHeight=475&originWidth=1079&originalType=binary&ratio=1&rotation=0&showTitle=false&size=79744&status=done&style=none&taskId=u56f970fd-68c9-4832-a4cf-6f710ca8c5c&title=&width=863.2)
### 2.平台管理模块
#### 2.1 医院设置管理功能
◇查看医院设置列表；能够进行分页显示，以及按医院名称，医院编号条件查询；医院设置即对平台上的医院进行相关信息的设置，如医院的api基础路径，用于和医院系统交互；医院方的联系人等；以及对医院的锁定删除等功能。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645800514957-52204880-53d8-484b-a323-79067e7fc769.png#clientId=u56e55247-ea2d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=449&id=u85367c88&margin=%5Bobject%20Object%5D&name=image.png&originHeight=561&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=89553&status=done&style=none&taskId=u9cf9aec6-ced7-48a8-957e-28ba549f4cf&title=&width=1536)
◇医院设置添加![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645800626630-99025846-2d27-4fcb-b7e7-6ad6a2fb0fa2.png#clientId=u56e55247-ea2d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=440&id=ueac65eae&margin=%5Bobject%20Object%5D&name=image.png&originHeight=550&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=48242&status=done&style=none&taskId=u992fc128-6649-4b06-89bd-493cb4e24fe&title=&width=1536)
◇医院设置修改，点击医院设置列表的修改操作就会跳转到编辑页面，并将数据回填。![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645800658163-9e50629d-8a7f-4d12-9f20-29255f65eae2.png#clientId=u56e55247-ea2d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=449&id=u52f6ff84&margin=%5Bobject%20Object%5D&name=image.png&originHeight=561&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=52613&status=done&style=none&taskId=u30e54649-80dd-49d3-919b-1aeda0488f8&title=&width=1536)
◇医院列表
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645846066005-25f3ea6f-4701-4b44-9fc6-12949cfd5dcb.png#clientId=uba8c650a-871a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=452&id=uabaafe61&margin=%5Bobject%20Object%5D&name=image.png&originHeight=565&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=70689&status=done&style=none&taskId=u78315e08-e261-4049-a8df-6bf73bcf87e&title=&width=1536)
◇查看医院详情
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645846228516-f88570a6-88b6-4221-bf7d-022f996cb126.png#clientId=uba8c650a-871a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=691&id=u6f702682&margin=%5Bobject%20Object%5D&name=image.png&originHeight=864&originWidth=1919&originalType=binary&ratio=1&rotation=0&showTitle=false&size=237726&status=done&style=none&taskId=uf71eacf8-97e1-4099-860d-fd44c79ceeb&title=&width=1535.2)
◇点击排班可以按日期、科室显示相关排班信息。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645846159147-0985aab6-f80b-455b-a0c7-67d922121766.png#clientId=uba8c650a-871a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=511&id=u2d0684da&margin=%5Bobject%20Object%5D&name=image.png&originHeight=639&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=109884&status=done&style=none&taskId=uc9a76b0d-5787-4597-9af7-90b21fe5a41&title=&width=1536)
#### 2.2 数据字典
◇数据字典树形懒加载
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645846326135-aa5adeea-d77d-40cd-b23c-c808a241089f.png#clientId=uba8c650a-871a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=518&id=u25b13a9c&margin=%5Bobject%20Object%5D&name=image.png&originHeight=647&originWidth=1696&originalType=binary&ratio=1&rotation=0&showTitle=false&size=60544&status=done&style=none&taskId=u6790c4f9-3988-4d05-a615-d425aeb6dfe&title=&width=1356.8)
#### 2.2 用户管理功能
◇用户列表
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645846715543-c24e6ed8-f597-46b8-864d-fb49b2ff1144.png#clientId=uba8c650a-871a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=407&id=u049a261b&margin=%5Bobject%20Object%5D&name=image.png&originHeight=509&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=58588&status=done&style=none&taskId=ued2c63fc-290f-4ebe-8c47-6fc51a5dd43&title=&width=1536)
◇用户审批列表
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645850460179-24293546-ab4b-4d36-a62d-a711542881c6.png#clientId=u9c405413-9261-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=417&id=u75e2750c&margin=%5Bobject%20Object%5D&name=image.png&originHeight=521&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=55506&status=done&style=none&taskId=u1321b3cc-3f64-4c74-92fb-d0d0e1bb961&title=&width=1536)
◇用户信息查看
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645846971306-247df992-7478-4962-a481-535730228710.png#clientId=uba8c650a-871a-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=722&id=u50350c42&margin=%5Bobject%20Object%5D&name=image.png&originHeight=902&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=118458&status=done&style=none&taskId=ueb13436f-cd0c-4058-aedb-f183785eff6&title=&width=1536)
#### 2.3 订单查看及统计功能
◇平台能够查看所有的订单信息并且对订单数据进行统计。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645959395125-c2fab051-87a4-4f55-97e3-754e43cd26b2.png#clientId=u32243033-0061-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=426&id=u4e6ba57d&margin=%5Bobject%20Object%5D&name=image.png&originHeight=532&originWidth=1845&originalType=binary&ratio=1&rotation=0&showTitle=false&size=64312&status=done&style=none&taskId=uead0482e-eb53-4e75-b8f4-425360d5a2b&title=&width=1476)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645941809992-a033e8bc-8fe1-4d74-9751-8d7de2ccac5b.png#clientId=u87948bc2-2618-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=638&id=u455b1366&margin=%5Bobject%20Object%5D&name=image.png&originHeight=797&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=67241&status=done&style=none&taskId=u5203ff1f-730b-497d-bca4-d16f9be2a9d&title=&width=1536)
### 3.医院系统模块
◇医院设置功能;设置医院的编码、医院签名和统一预约挂号平台的基础路径；医院签名用于和挂号平台进行校验，基础路径即调用平台相关的接口路径。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645799932699-12c569b8-3d34-4137-a63e-065b1c9d0a2c.png#clientId=u56e55247-ea2d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=546&id=ud5ca2005&margin=%5Bobject%20Object%5D&name=image.png&originHeight=682&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=40957&status=done&style=none&taskId=ue7e2ddb0-aa10-46fd-8874-54c99173045&title=&width=1536)
◇医院信息及上传医院信息功能，通过JSON数据的方式上传医院信息；若要修改医院信息，在医院编号不变的情况下重新添加数据即可。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645800177789-2ca6bdfc-1b20-44ce-9d3b-687190928669.png#clientId=u56e55247-ea2d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=638&id=u4a5534c8&margin=%5Bobject%20Object%5D&name=image.png&originHeight=798&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=202691&status=done&style=none&taskId=u8c78047d-d41a-4cde-b013-50387b46e83&title=&width=1536)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645800316594-c4540778-12cc-48aa-bedf-5e6f625f2eb3.png#clientId=u56e55247-ea2d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=603&id=u87343001&margin=%5Bobject%20Object%5D&name=image.png&originHeight=754&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=94961&status=done&style=none&taskId=ufaf71b5d-2c8d-4bc9-b430-d72b6c077c7&title=&width=1536)
◇科室列表与上传科室功能；上传科室的方式也是通过JSON数据上传。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21429457/1645800351792-d8b7cfa4-658c-49b3-a2b2-90013520e9ea.png#clientId=u56e55247-ea2d-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=614&id=KRfx3&margin=%5Bobject%20Object%5D&name=image.png&originHeight=767&originWidth=1920&originalType=binary&ratio=1&rotation=0&showTitle=false&size=148550&status=done&style=none&taskId=u0a183f16-9824-4a96-90f3-6a462833814&title=&width=1536)
◇医院排班列表以及上传排班功能和前面类似。
​

