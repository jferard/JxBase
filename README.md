JxBase
======
* JxBase - Copyright (c) 2019 Julien Férard (https://github.com/jferard)
* JDBF - Copyright (c) 2012-2018 Ivan Ryndin (https://github.com/iryndin)

[![Build Status](https://travis-ci.org/jferard/JxBase.svg?branch=master)](https://travis-ci.org/jferard/JxBase)
[![codecov](https://codecov.io/gh/jferard/JxBase/branch/master/graph/badge.svg)](https://codecov.io/gh/jferard/JxBase)

JxBase is a fork from [jdbf](https://github.com/iryndin/jdbf), a Java utility to read/write DBF files

## Convert a DBF file to SQLite from command line
Call, from command line, the class `com.github.jferard.jxbase.tool.DatabaseLoader`. Here's an example:

    ...$ java -cp "$HOME/.m2/repository/com/github/jferard/jxbase/0.0.1-SNAPSHOT/jxbase-0.0.1-SNAPSHOT.jar" com.github.jferard.jxbase.tool.DatabaseLoader
    Usage: java -cp 'path/to/jxbase/jar:path/to/jdbc/driver/jar' com.github.jferard.jxbase.tool.DatabaseLoader [option] [source] [connect_string]
    
       -h, --help          Print this message
       -c driver_class     Load the driver using Class.forName
       -d                  Drop tables if they exist
       -s N                Chunk size (default is one chunk of the size of the file)
                           Use this to avoid an out of memory for big files
       source              A directory or a single dbf file
       connection_string   A connection string to the database
    ...$java -cp "$HOME/.m2/repository/org/xerial/sqlite-jdbc/3.32.3.2/sqlite-jdbc-3.32.3.2.jar:$HOME/.m2/repository/com/github/jferard/jxbase/0.0.1-SNAPSHOT/jxbase-0.0.1-SNAPSHOT.jar" com.github.jferard.jxbase.tool.DatabaseLoader -d -s 10000 $HOME/prog/java/jxbase/src/test/resources/data1 "jdbc:sqlite:./test.sqlite"
    oct. 17, 2020 10:52:55 PM com.github.jferard.jxbase.tool.DatabaseLoader buildAndFillTables
    INFOS: Build tables
    oct. 17, 2020 10:52:55 PM com.github.jferard.jxbase.tool.DatabaseLoader buildAndFillTable
    INFOS:  > /home/jferard/prog/java/jxbase/src/test/resources/data1/gds_im.dbf
    oct. 17, 2020 10:52:55 PM com.github.jferard.jxbase.tool.DatabaseLoader buildAndFillTable
    INFOS:  > /home/jferard/prog/java/jxbase/src/test/resources/data1/tir_im.dbf
         

## User Guide

### Read a DBF file 

TODO

See [the integration test example](src/test/java/com/github/jferard/jxbase/it/ReaderIT.java)

### Read a DBF file with MEMO fields

TODO

See [the integration test example](src/test/java/com/github/jferard/jxbase/it/ReaderWithMemoIT.java)

### Write a DBF file 

TODO

See [the integration test example](src/test/java/com/github/jferard/jxbase/it/WriterIT.java)

### Write a DBF file with MEMO fields

TODO

See [the integration test example](src/test/java/com/github/jferard/jxbase/it/WriterWithMemoIT.java)

## Design of an xBase database
The database is a simple set of files.

### References
See:
* https://www.clicketyclick.dk/databases/xbase/format/
* http://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm
* http://devzone.advantagedatabase.com/dz/webhelp/Advantage12/index.html?master_advantage_isam_file_types.htm
* https://dbfread.readthedocs.io/en/latest/field_types.html

### The DBF file
The main file. Contains the header and the records.

    +------- Header ---------+
    |       Metadata         |   <- XBaseMetaDataReader/Writer    
    | Field Descriptor Array |   <- XBaseFieldDescriptorArrayReader/Writer
    |        Optional        |   <- XBaseOptionalReader/Writer
    +------------------------+
    |        Records         |   <- XBaseRecordReader/Writer
    +------------------------+

### DBT file (memo: optional)
This file contains block of data.

    +------------------------+  \
    |         Header         |   |
    +------------------------+   } <- XBaseMemoReader/Writer
    |        Records         |   |
    +------------------------+  /

### NDX file (index: optional)
TODO

## Design of JxBase
###
JxBase aims to build DBF format reader and writer by layers. Each dialect derives an existing format 
and adds fields, formats, files.  

### The covariance parameter problem
Whereas a `GenericRecordReader` has a `GenericDialect` parameter, a `FoxProRecordReader` needs 
a `FoxProDialect` parameter. A covariant parameter (the parameters type varies with the class type)
is a real problem because it violates the Liskov substitution principle: the subclass 
`FoxProRecordReader` won't work with a `GenericDialect` parameter (see https://en.wikipedia.org/wiki/Covariance_and_contravariance_(computer_science)#Covariant_method_parameter_type).
Usually, one solves this problem with generics:

```
    class GenericRecordReader<D extends GenericDialect> {
        ...
    }
    
    class FoxProRecordReader extends GenericRecordReader<FoxProDialect> {
        ...
    }
```

Currently, JxBase doesn't use generics for covariance because this would lead to a really complex situation. The fallaback used to achieve the covariance is 
a simple cast: we accept a `GenericDialect` and try to cast the instance into a `FoxProDialect` 

