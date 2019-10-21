JxBase
======
* JxBase - Copyright (c) 2019 Julien Férard (https://github.com/jferard)
* JDBF - Copyright (c) 2012-2018 Ivan Ryndin (https://github.com/iryndin)


JxBase is a fork from [jdbf](https://github.com/iryndin/jdbf), a Java utility to read/write DBF files

## User Guide

### Read DBF file 

Piece of code that reads file from classpath. Single DBF record is represented here as a Map.

See [TestDbfReader.java](src/test/java/com/github/jferard/jxbase/TestDbfReader.java)

```java
    public void readDBF() throws IOException, ParseException {
        Charset stringCharset = Charset.forName("Cp866");

        InputStream dbf = getClass().getClassLoader().getResourceAsStream("data1/gds_im.dbf");

        DbfRecord rec;
        try (DbfReader reader = new DbfReader(dbf)) {
            DbfMetadata meta = reader.getMetadata();

            System.out.println("Read DBF Metadata: " + meta);
            while ((rec = reader.read()) != null) {
                rec.setStringCharset(stringCharset);
                System.out.println("Record #" + rec.getRecordNumber() + ": " + rec.toMap());
            }
        }
    }
```

### Read DBF file with MEMO fields

Piece of code that reads DBF and MEMO fields. 

See [TestMemo.java](src/test/java/com/github/jferard/jxbase/TestMemo.java)

```java
    public void test1() {
        Charset stringCharset = Charset.forName("cp1252");

        InputStream dbf = getClass().getClassLoader().getResourceAsStream("memo1/texto.dbf");
        InputStream memo = getClass().getClassLoader().getResourceAsStream("memo1/texto.fpt");

        try (DbfReader reader = new DbfReader(dbf, memo)) {
            DbfMetadata meta = reader.getMetadata();
            System.out.println("Read DBF Metadata: " + meta);

            DbfRecord rec;
            while ((rec = reader.read()) != null) {
                rec.setStringCharset(stringCharset);

                System.out.println("TEXVER: " + rec.getString("TEXVER"));
                // this reads MEMO field
                System.out.println("TEXTEX: " + rec.getMemoAsString("TEXTEX"));
                System.out.println("TEXDAT: " + rec.getDate("TEXDAT"));
                System.out.println("TEXSTA: " + rec.getString("TEXSTA"));
                System.out.println("TEXCAM: " + rec.getString("TEXCAM"));
                System.out.println("++++++++++++++++++++++++++++++++++");
            }

        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
```
