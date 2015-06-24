# ChangeLog Library Customization and Tips

There are many ways you can customize the changelog view.

* [Custom Header Layout](#custom-header-layout)
* [Custom Row Layout](#custom-row-layout)
* [Layouts with Material Specs](#layouts-with-material-specs)
* [Custom ChangeLog XML](#custom-changeLog-xml)
* [Bullet Point](#bullet-point)
* [Html Text Markup](#html-text-markup)
* [Customize Strings](#customize-strings)
* [Use an url to download the changelog.xml file](#use-an-url-to-download-the-changelog.xml-file)
* [Bug and improvement tags](#bug-and-improvement-tags)
* [Emtpy view](#emtpy-view)

The library uses a custom `RecyclerView` to display the changelog.

If you would like to use a `ListView`, you have the same features changing the `it.gmariotti.changelibs.library.view.ChangeLogRecyclerView` with `it.gmariotti.changelibs.library.view.ChangeLogListView`.


### Custom Header Layout

Library uses `res/layout/changelogrowheader_layout.xml` XML layout for each header.<br/>
You can use your own xml layout with `chg:rowHeaderLayoutId` attribute in `ChangeLogRecyclerView` element.

``` xml
    <!-- Custom xml file Example and custom header layout -->
    <it.gmariotti.changelibs.library.view.ChangeLogRecyclerView
          xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:chg="http://schemas.android.com/apk/res-auto"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          chg:rowHeaderLayoutId="@layout/demo_changelogrowheader_layout"
    />
```

The quickest way to start with this would be to copy the [`changelogrowheader_layout.xml`](/ChangeLogLibrary/src/main/res/layout-v14/changelogrowheader_layout.xml) layout resource file from this project into your own and modify its contents.<br/>
There's a specific set of views you need to have in your layout:

1. A `TextView` with the ID `chg_headerVersion` that display the Version Number
2. A `TextView` with the ID `chg_headerDate` that display the Version Date

You can find an example in [`demo_changelogrowheader_layout.xml`](/ChangeLogDemo/src/main/res/layout/demo_changelogrowheader_layout.xml)

### Custom Row Layout

Library uses `res/layout/changelogrow_layout.xml` XML layout for each row.<br/>
You can use your own xml layout with `chg:rowLayoutId` attribute in `ChangeLogRecyclerView` element.

``` xml

    <!-- Custom xml file Example and custom header layout -->
    <it.gmariotti.changelibs.library.view.ChangeLogRecyclerView
          xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:chg="http://schemas.android.com/apk/res-auto"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          chg:rowLayoutId="@layout/demo_changelogrow_layout"
    />
```
The quickest way to start with this would be to copy the [`changelogrow_layout.xml`](/ChangeLogLibrary/src/main/res/layout-v14/changelogrow_layout.xml) layout resource file from this project into your own and modify its contents.<br/>
There's a specific set of views you need to have in your layout:

1. A `TextView` with the ID `chg_textbullet` that display the bullet point
2. A `TextView` with the ID `chg_text` that display the actual text that will be displayed as a change in your List

You can find an example in [`demo_changelogrow_layout.xml`](/ChangeLogDemo/src/main/res/layout/demo_changelogrow_layout.xml)

### Layouts with Material Specs

Library contains also 2 layouts with **Material specs and metrics**:

- [`res/layout/changelogrow_material_layout.xml`](/ChangeLogLibrary/src/main/res/layout-v14/changelogrow_material_layout.xml) l XML layout for each row.
- [`res/layout/changelogrowheader_material_layout.xml`](/ChangeLogLibrary/src/main/res/layout-v14/changelogrowheader_material_layout.xml) l XML layout for each header.

You can use them with a simple:

``` xml

    <!-- Material layout -->
     <it.gmariotti.changelibs.library.view.ChangeLogRecyclerView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/view"
        chg:rowHeaderLayoutId="@layout/changelogrowheader_material_layout"
        chg:rowLayoutId="@layout/changelogrow_material_layout"
        android:layout_gravity="center"
        />
```

You can find an example in [`demo_changelogrow_fragment_material.xml`](/ChangeLogDemo/src/main/res/layout/demo_changelog_fragment_material.xml)
You can find a screenshot [here](/ChangeLogDemo/README.md#material-specs).


### Custom ChangeLog XML

Library uses `res/raw/changelog.xml`.
You can use your own file with `chg:changeLogFileResourceId` attribute in `ChangeLogRecyclerView` element.

``` xml

    <!-- Custom xml file Example and custom header layout -->
    <it.gmariotti.changelibs.library.view.ChangeLogRecyclerView
          xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:chg="http://schemas.android.com/apk/res-auto"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          chg:changeLogFileResourceId="@raw/custom_changelog"
    />
```

You can find an example in [`demo_changelogrow_fragment_customlayout.xml`](/ChangeLogDemo/src/main/res/layout/demo_changelog_fragment_customlayout.xml)

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

If you need a hiperlink you can obtain it with:

```xml
    <changelogbug>Fixed a [a href="http://www.google.it"]number[/a] of bugs with the
              Gmail and SMS extensions</changelogbug>
```

Of course you can use standard android notation:

```xml
     <changelogimprovement><![CDATA[Option to specify a <a href="http://www.google.it">manual</a>
                               location for weather]]></changelogimprovement>
     <changelogtext><![CDATA[<b><font color=red>New!</font></b> Translations for Greek,
                   Dutch and Brazilian Portuguese]]>
     </changelogtext>
```


### Customize Strings

You can customize `Version` String in header.<br/>
The quickest way to customize this character is to specify this resource in your `strings.xml`.

``` xml
<string name="changelog_header_version">"Revision "</string>
```

Use string with quotes if you want a space.

You can also customize other strings in the same way. See original `res/string.xml` in library.

### Use an url to download the changelog.xml file

Library uses `res/raw/changelog.xml`.

You can use a url link to download your xml file with `chg:changeLogFileResourceUrl` attribute in `ChangeLogRecyclerView` element.

``` xml

    <!-- Custom xml file Example and custom header layout -->
    <it.gmariotti.changelibs.library.view.ChangeLogRecyclerView
          xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:chg="http://schemas.android.com/apk/res-auto"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          chg:changeLogFileResourceUrl="http://mydomain.org/changelog.xml"
    />
```

Pay attention:
To use this feature you have to add these user-permissions to your app:

``` xml
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
```

### Bug and improvement tags

You can use 2 custom changelogtext tags that will pre-pend the change log text with **Bug:** and **New:** respectively.

``` xml
   <changelogversion versionName="0.9" changeDate="Aug 11,2013">
        <changelogtext>[b]New![/b] Add new attrs to customize header and row layout</changelogtext>
        <changelogimprovement>Option to specify a manual location for weather</changelogimprovement>
        <changelogtext>performance &lt;b&gt;improvement&lt;/b&gt;</changelogtext>
        <changelogbug>Fixed a number of bugs with the Gmail and SMS extensions</changelogbug>
   </changelogversion>
```

You can customize the prefix text in your project overriding these values in strings.xml

``` xml
    <!-- Prefix for Bug type change log -->
    <string name="changelog_row_prefix_bug">[b]Bug:[/b]</string>

    <!-- Prefix for Improvement type change log -->
    <string name="changelog_row_prefix_improvement">[b]New:[/b]</string>
```

You can use the same html text markup described [above](#Html-text-markup).



For more detailed information and examples you can read this [document:](/ChangeLogDemo/README.md)
