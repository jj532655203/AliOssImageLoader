# AliOssImageLoader

apk下载地址:

https://phone-love-piano-public-ro.oss-cn-shenzhen.aliyuncs.com/demo-apk/ali_oss_image_loader_debug_0403.apk

或手机扫码下载

![Image scan qrcode download apk](https://phone-love-piano-public-ro.oss-cn-shenzhen.aliyuncs.com/demo-apk/ali_oss_image_loader_debug_apk_qrcode.png)

本library使用与图片资源保存在阿里云oss服务器上,获取图片时存在身份认证,或者以后可能要做身份的。

相反，如果贵公司的图片资源可公开访问，或者保存在阿里云oss上但是老板说了永远不做身份认证，那您可直接使用其它优秀图片加载器（如Glide等）。

## 用法：
### 1.项目根目录的gradle文件 

buildscript.repositories{
	maven { url "https://jitpack.io" }
}

allprojects.repositories{
	maven { url "https://jitpack.io" }
}



### 2.您要使用本图片加载器的module(比如app)的gradle文件

implementation  'com.github.jj532655203:AliOssImageLoader:1.1.2'



### 3.Application.onCreate方法中

		
        AliOssImageLoader.init(new OSSFederationCredentialProvider() {

            /**
             * 注意!:ossClient会在token失效时(不固定啥时候失效哦),调用本方法重新获取token
             * 还有其它实例化OSSFederationToken的方法见:https://help.aliyun.com/document_detail/100624.html?spm=a2c4g.11186623.6.664.4d281c62Q8euRd
             */
            @Override
            public OSSFederationToken getFederationToken() throws ClientException {

                ...

                return new OSSFederationToken(ak, sk, token, expiration);
            }
        }, <BUCKET_NAME>);
		
		
###  4.proguard

-keep class com.github.jj532655203.** { *; }
-dontwarn com.github.jj532655203.**


		
		
## tips

建议对图片加载器原理不够了解的开发者,将项目下载下来研究清楚,里面的图片LRU缓存机制/线程池reject策略用于快速滑动列表的场景/防止bitmap引起的内存泄漏等知识都是非常重要又基础的。

觉得好请star,让更多人知道。谢谢。