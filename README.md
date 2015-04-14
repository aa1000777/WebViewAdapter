#WebViewAdapter#
-----------
## 功能介绍 ##
### 方法一 ###
Android WebView 文本框获取焦点后自动放大的解决方案
参考[http://www.cppblog.com/guojingjia2006/archive/2012/12/18/196429.html](http://www.cppblog.com/guojingjia2006/archive/2012/12/18/196429.html)

### 方法二 ###
[TV端Android WebView 文本框获取焦点后自动放大的问题 ](http://www.snued.com/article/53ba4c57c5c866be43631b21)

```
mWebView.setWebChromeClient(new WebChromeClient(){

        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                view.requestFocus();

        }

 });
```

### 方法三 ###
以上二种方法都不能完全解决问题，在三星手机上总会出现放大效果
后在在网面中加了以下两行代码就好了

```
<!-- 下面两句代码是做手机适配用的 ， 加上之后手机网页就会自动适配-->
<meta name="viewport" content="width=device-width">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"> 
```

## ApplicationScreenshots ##
![](https://raw.githubusercontent.com/aa1000777/android-cjc-project/master/Image/WebViewAdapter1.png)

