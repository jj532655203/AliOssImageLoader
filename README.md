# AliOssImageLoader

本library使用与图片资源保存在阿里云oss服务器上,获取图片时存在身份认证,或者以后可能要做身份的。

相反，如果贵公司的图片资源可公开访问，或者保存在阿里云oss上但是老板说了永远不做身份认证，那您可直接使用其它优秀图片加载器（如Glide等）。

## 用法：
###1.项目根目录的gradle文件 

buildscript.repositories{
	maven { url "https://jitpack.io" }
}

allprojects.repositories{
	maven { url "https://jitpack.io" }
}



### 2.您要使用本图片加载器的module(比如app)的gradle文件

implementation  'com.github.jj532655203:AliOssImageLoader:1.1.2'



### 3.Application.onCreate方法中

//AliOssImageLoader初始化
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
		
		
##tips

建议对图片加载器原理不够了解的开发者,将项目下载下来研究清楚,里面的图片缓存机制/线程池reject策略的用法/防止bitmap引起的内存泄漏等知识都是非常重要又基础的。