# ChangeLog Library Customization and Tips

There are many ways you can customize the changelog view.

### Custom Header Layout

Library uses `res/layout/changelogrowheader_layout.xml` XML layout for each header.<br/>
You can use your own xml layout with `chg:rowHeaderLayoutId` attribute in `ChangeLogListView` element.

``` xml
    <!-- Custom xml file Example and custom header layout -->
    <view xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:chg="http://schemas.android.com/apk/res-auto"
          class="it.gmariotti.changelibs.library.view.ChangeLogListView"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          chg:rowHeaderLayoutId="@layout/demo_changelogrowheader_layout"
    />
```

The quickest way to start with this would be to copy the [`changelogrowheader_layout.xml`](https://github.com/gabrielemariotti/changeloglib/tree/master/ChangeLogLibrary/src/main/res/layout/changelogrowheader_layout.xml) layout resource file from this project into your own and modify its contents.<br/>
There's a specific set of views you need to have in your layout:

1. A `TextView` with the ID `chg_headerVersion` that display the Version Number
2. A `TextView` with the ID `chg_headerDate` that display the Version Date

You can find an example in [`demo_changelogrowheader_layout.xml`](https://github.com/gabrielemariotti/changeloglib/tree/master/ChangeLogDemo/src/main/res/layout/demo_changelogrowheader_layout.xml)

### Custom Row Layout

Library uses `res/layout/changelogrow_layout.xml` XML layout for each row.<br/>
You can use your own xml layout with `chg:rowLayoutId` attribute in `ChangeLogListView` element.

``` xml

    <!-- Custom xml file Example and custom header layout -->
    <view xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:chg="http://schemas.android.com/apk/res-auto"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          class="it.gmariotti.changelibs.library.view.ChangeLogListView"
          chg:rowLayoutId="@layout/demo_changelogrow_layout"
    />
```
The quickest way to start with this would be to copy the [`changelogrow_layout.xml`](https://github.com/gabrielemariotti/changeloglib/tree/master/ChangeLogLibrary/src/main/res/layout/changelogrow_layout.xml) layout resource file from this project into your own and modify its contents.<br/>
There's a specific set of views you need to have in your layout:

1. A `TextView` with the ID `chg_textbullet` that display the bullet point
2. A `TextView` with the ID `chg_text` that display the actual text that will be displayed as a change in your List

You can find an example in [`demo_changelogrow_layout.xml`](https://github.com/gabrielemariotti/changeloglib/tree/master/ChangeLogDemo/src/main/res/layout/demo_changelogrow_layout.xml)

### Custom ChangeLog XML

Library uses `res/raw/changelog.xml`.
You can use your own file with `chg:changeLogFileResourceId` attribute in `ChangeLogListView` element.

``` xml

    <!-- Custom xml file Example and custom header layout -->
    <view xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:chg="http://schemas.android.com/apk/res-auto"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          class="it.gmariotti.changelibs.library.view.ChangeLogListView"
          chg:changeLogFileResourceId="@raw/custom_changelog"
    />
```

You can find an example in [`demo_changelogrow_fragment_customlayout.xml`](https://github.com/gabrielemariotti/changeloglib/tree/master/ChangeLogDemo/src/main/res/layout/demo_changelog_fragment_customlayout.xml)

### Bullet Point
The quickest way to customize this character is to specify this resource in your `strings.xml`.

``` xml
   <string name="changelog_row_bulletpoint">\u2022"</string>
```

If you don't want a bullet list you can specify it in `changelog.xml` file with `bulletedList="false"` attribute.

``` xml
    <?xml version="1.0" encoding="utf-8"?>
    <changelog bulletedList="false">
        .....
    </changelog>

```

### Html Text Markup

You can use some html text markup as bold and italic in your changelog.xml

``` xml
   <changelogversion versionName="0.9" changeDate="Aug 11,2013">
        <changelogtext>[b]New![/b] Add new attrs to customize header and row layout</changelogtext>
        <changelogtext>Fixed log while [i]parsing[/i] </changelogtext>
        <changelogtext>performance &lt;b&gt;improvement&lt;/b&gt;</changelogtext>
        ....
```
You can use:

1. `[b] text [/b]` for a <b>bold style</b>, or `&lt;b&gt; text &lt;/b&gt;`
2. `[i] text [/i]` for an <i>italic style</i>, or `&lt;b&gt; text &lt;/i&gt;`


### Customize `Version` String

You can customize `Version` String in header.<br/>
The quickest way to customize this character is to specify this resource in your `strings.xml`.

``` xml
<string name="changelog_header_version">"Revision "</string>
```

Use string with quotes if you want a space.

For more detailed information and examples you can read this [document:](https://github.com/gabrielemariotti/changeloglib/tree/master/ChangeLogDemo/README.md)
