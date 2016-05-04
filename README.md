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
### Convert Using Existing Rules
With the library added, you may create a XMLSource instance and use `toMDictSource()` method to convert:
```java
import com.purlingnayuki.util.xml2mdict.Converter.MDictConverter;
import com.purlingnayuki.util.xml2mdict.Provider.OxfordCD.OxfordXMLSource;
import com.purlingnayuki.util.xml2mdict.datatype.Converter;
// ....
File[] files = new File[size];
// ....
Provider provider = new OxfordXMLSource(files, true);
Converter converter = new MDictConverter(provider);
converter.setParameter("css", youCssString).convert(); // if you need to assign css file;
converter.convert(); // if no need to assign css file
```

Note that the Provider class _won't_ handle directory, at least for now. If need to handle directories, add the files in them to the ArrayList:
```java
// ....
if (in.isDirectory())
    for (String fn: in.list()) {
        File file = new File(fn);
        if (!file.isDirectory())
            arrayList.add(file);
    }
// ....
```
### Convert by Original Rules
To create your own rules, you need to extends or implement one of or both `Provider` class and `Converter` interface. You need to implement method below:
```java
public abstract class Provider {
    public abstract ArrayList<Element> getHeadwords(); // to get all headwords from ArrayList<File>
    public abstract String getContent(Element elem); // to get content body corresponds to input Element
}
```
```java
public interface Converter {
    ArrayList<String> convert() throws DocumentException; // to generate formatted content from all input files
    Converter setParameter(String name, String value); // to specify parameter of this converter, for example to control output format
}
```

Once you complete the job, you can call your Provider and Converter:
```java
File[] files;
// ....
Provider provider = new myProvider(files);
Converter converter = new myConverter(provider);
converter.setParameter("setSomeParameters", "ifNeeded").converter;
```
**If you create a new Provider or Converter, a pull request is always welcome.**
