## droidblutooth

*[app build.gradle]
*dependencies 추가
<pre>
<code>
implementation 'com.skyware:droid-bluetooth:1.0.1-RELEASE'
</code>
</pre>

* [project build.gradle]
* 스크립트 추가 Maven
<pre>
<code>
buildscript {
    repositories {
        ...
        maven {url "http://1.221.205.250:9495/nexus/content/repositories/releases"}	//<-- 추가
    }
    ...
}

allprojects {
    repositories {
        ...
        maven {url "http://1.221.205.250:9495/nexus/content/repositories/releases"}	//<-- 추가
    }
}
</code>
</pre>
