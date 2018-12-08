使用方法：

主项目build.gradle中加入glide或者Picasso等图片加载第三方库；这里使用glide第三方库

~~~java
implementation 'com.github.bumptech.glide:glide:4.5.0'
~~~

在AndroidManifest.xml中添加权限

~~~java
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.CAMERA"/>
~~~

6.0以上系统注意申请权限。

注册图片选取activity

~~~java
<activity
	android:name="com.photo.select.ImageSelectorActivity"
    android:configChanges="orientation|screenSize"
    android:theme="@style/ImageSelectorTheme"/>
<activity
    android:name="com.photo.select.preview.MultiImgShowActivity"
    android:configChanges="orientation|screenSize"
    android:theme="@style/ImageShowTheme">
</activity>
~~~

适应android6.0以上版本，需要添加provider

~~~java
<!--authorities：app的包名.fileProvider （fileProvider可以随便写）,上面采用的是gradle的替换直接写成包名也可以，但是推荐这种方式，多渠道分包的时候不用关心了-->
<!--grantUriPermissions：必须是true，表示授予 URI 临时访问权限-->
<!--exported：必须是false-->
<provider
	android:name="android.support.v4.content.FileProvider"
    android:authorities="${applicationId}.fileProvider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths"/>
</provider>
~~~

同时注意com.photo.select.ImageSelectorFragment.java文件中的314行主要同android:authorities保持一致。

编写一个图片加载功能类，继承com.photo.select.ImageLoader，重写displayImage方法；这里采用glide为例

~~~java
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.photo.select.ImageLoader;
import com.ray.project.R;

public class GlideLoader implements ImageLoader {

	private static final long serialVersionUID = 1L;

	@Override
    public void displayImage(Context context, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .placeholder(R.drawable.global_img_default)
                .centerCrop();
        Glide.with(context)
                .load(path)
                .apply(options)
                .into(imageView);
    }

}
~~~

添加一个接收集合

```java
private ArrayList<String> selected = new ArrayList<>();
private final static int RESULT_PHOTO_CODE = 101;
```

重写onActivityResult

~~~java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && requestCode == RESULT_PHOTO_CODE) {
        selected = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
        pictureAdapter.setPictures(selected);
    }
}
~~~

最后调用

~~~java
ImageConfig imageConfig = new ImageConfig.Builder(
                new GlideLoader())
                .steepToolBarColor(getResources().getColor(R.color.actionbar))
                .titleBgColor(getResources().getColor(R.color.actionbar))
                .titleSubmitTextColor(getResources().getColor(R.color.material_white))
                .titleTextColor(getResources().getColor(R.color.material_white))
                // 开启多选（默认为多选） 
                .mutiSelect()
                // 单选
                //.singleSelect()
                // 多选时的最大数量（默认 9 张）
                .mutiSelectMaxSize(8)
                // 已选择的图片路径
                .pathList(selected)
                // 拍照后存放的图片路径（默认 /temp/picture）
			   //.filePath("/temp")
                // 开启拍照功能（默认关闭）
                .showCamera()
                .requestCode(101)
                .build();
ImageSelector.open(this, imageConfig);
~~~

