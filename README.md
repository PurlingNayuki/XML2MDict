# XML2MDict: A Java Implement of XML to MDict Source Converter
---

## Commandline Utility Usage
To convert a single xml file, just run

` java -jar xml2mdict.jar file.xml`

If a bunch of files are to be converted, palce them in a directory and run

` java -jar xml2mdict.jar file1.xml file2.xml ...`

or

` java -jar xml2mdict.jar directory`

If you need to assign css file, use `-c` or `--css` option:

`java -jar --css style.css xml2mdict.jar file.xml`

## Java Library Usage
A Java library is provided to read xml files and convert them to MDict source. The XMLSource class is extendable and may need to be modified to fit situations.
### Conversion API
With the library added, you may create a XMLSource instance and use `toMDictSource()` method to convert:
```java
import com.purlingnayuki.util.xml2mdict.XMLSource;
// ....
File xml = new File("path/to/file.xml");
XMLSource xmlsource = new XMLSource(xml);
System.out.print(xmlsource.toMDictSource(cssPath));
```
If no css file needed or assigned, pass `null` to `toMDictSource()`:
```java
toMDictSource(null);
```

Note that the construct of XMLSource accepts *ONLY* a file, not a directory. If files are to be converted, use a loop:
```java
String[] xmls = indir.list();
for (String fn: xmls) {
    XMLSource xmlsource = new XMLSource(new File(indir.getAbsolutePath() + File.separator + fn));
    System.out.print(xmlsource.toMDictSource(null));
```

### Headword Setup in `getHeadwordByNode()`
The `String getHeadwordByNode(Element)` method is used to set up headword for each item. If other rules must be applied to get headword from xml files, derive XMLSource class and override this method.

### Images, audios and other elements in `handleElement()`
Elements are handled in `String handleElement(Element, StringBuilder)` method. If any tags that need to be handled exceptionally, derive the XMLSource class and override this method.
