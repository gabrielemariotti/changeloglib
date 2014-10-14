# ChangeLog Library

ChangeLog Library provides an easy way to display a change log in your Android app.

Travis master: [![Build Status](https://travis-ci.org/gabrielemariotti/changeloglib.svg?branch=master)](https://travis-ci.org/gabrielemariotti/changeloglib)
Travis dev: [![Build Status](https://travis-ci.org/gabrielemariotti/changeloglib.svg?branch=dev)](https://travis-ci.org/gabrielemariotti/changeloglib)

![Screen](/ChangeLogDemo/images/screen.png)


---
## Examples

* **Sample** application: The demo is a showcase of the functionality of the library.

	 [![Get it on Google Play](http://www.android.com/images/brand/get_it_on_play_logo_small.png)](https://play.google.com/store/apps/details?id=it.gmariotti.changelog.demo)

* Browse the [source code of the sample application](/ChangeLogDemo) for a complete example of use.

**If you would like, you can support my work, donating through the demo app.**


For more examples and screenshots you can read this [document:](/ChangeLogDemo/README.md)


## Feature

ChangeLog Library provides a custom `ListView` to display a change log through a xml file.

* you can use it in Activities, Fragments, Dialogs
* it supports html text markup as bold and italics
* you can customize layout and behaviour
* it supports multi language
* it supports API 7+


## Usage

Implementing this library in your own apps is pretty simple.<br/>
First, you need an XML layout that will contain the `ChangeLogListView` that displays your changelog.

``` xml
<?xml version="1.0" encoding="utf-8"?>
 <view xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:chg="http://schemas.android.com/apk/res-auto"
       class="it.gmariotti.changelibs.library.view.ChangeLogListView"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       android:id="@+id/view"
       />
```

Then, you need a XML file with change log in `res/raw` folder.
It automatically searches for [`res/raw/changelog.xml`](/ChangeLogLibrary/src/main/res/raw/changelog.xml) but you can customize filename.

``` xml
<?xml version="1.0" encoding="utf-8"?>
<changelog bulletedList="true">

    <changelogversion versionName="1.0" changeDate="Aug 26,2013">
            <changelogtext>Initial release.</changelogtext>
    </changelogversion>

    <changelogversion versionName="0.9" changeDate="Aug 11,2013">
        <changelogtext>[b]New![/b] Add new attrs to customize header and row layout</changelogtext>
        <changelogtext>Fixed log while parsing </changelogtext>
        <changelogtext>Add support for html markup</changelogtext>
        <changelogtext>Add bullet point in </changelogtext>
        <changelogtext>Support for customized xml filename</changelogtext>
    </changelogversion>

</changelog>

```

Last, if you would like a multi language changelog, you just have to put the translated files `changelog.xml` in the appropriate folders `res/raw-xx/`.


## Doc

See the customisation [page:](/doc/CUSTOMIZATION.md) for more information.

---
## Quick Start

ChangeLog Library is pushed to Maven Central as a AAR, so you just need to add the following dependency to your `build.gradle`.

    dependencies {
        compile 'com.github.gabrielemariotti.changeloglib:library:1.5.2'
    }

[To build the library and demo locally you can see this page for more info](/doc/BUILD.md).


## ChangeLog

* [Changelog:](CHANGELOG.md) A complete changelog


Credits
-------

Author: Gabriele Mariotti (gabri.mariotti@gmail.com)

<a href="https://plus.google.com/u/0/114432517923423045208">
  <img alt="Follow me on Google+"
       src="https://github.com/gabrielemariotti/changeloglib/raw/master/ChangeLogDemo/images/g+64.png" />
</a>
<a href="https://twitter.com/GabMarioPower">
  <img alt="Follow me on Twitter"
       src="https://github.com/gabrielemariotti/changeloglib/raw/master/ChangeLogDemo/images/twitter64.png" />
</a>
<a href="http://it.linkedin.com/in/gabrielemariotti">
  <img alt="Follow me on LinkedIn"
       src="https://github.com/gabrielemariotti/changeloglib/raw/master/ChangeLogDemo/images/linkedin.png" />
</a>

License
-------

    Copyright 2013-2014 Gabriele Mariotti

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
