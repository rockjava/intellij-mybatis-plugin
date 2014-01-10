#Download

* http://plugins.jetbrains.com/plugin/7293

# Features

* Useful navigation between xml and mapper
* Support generating of statement, @Param annotation and mapper of xml
* Support some useful rename of mapper in xml
* Support correct result type for select statement in mapper xml
* Support correct unresolved attribute value of mapper xml
* Support refactor the name of mapper xml file when rename the mapper interface
* Support refactor for id based tag in mapper xml
* Support find usage of mapper interface and mapper xml element
* Highlight conflicting element of mapper xml as errors
* Auto register mapper as spring bean</li>
* Support completion of jdbc type in mapper xml
* Support completion of referencing to select statement in mapper xml
* Support switching between java type and alias in mapper xml using intention keystroke
* Generate columns of single table with specified alias using intention keystroke
    * To insert a ',' at the beginning of the generated columns, set the alias like ',yourAlias' (Quote is not required)
    * To insert a ',' at the end of the generated columns, set the alias like 'yourAlias,' (Quote is not required)
    * To insert a ',' at both start and end of the generated columns, set the alias like ',yourAlias,' (Quote is not required)
* Mapper parameter auto completion in xml while editing sql
    * \#{yourParameter}
    * @Param annotation based
    * Association is supported
* Support generating properties for elements of result map using intention keystroke
    * DataSource of intellij is required

#Generate

* Place your caret on your target
    * Declaration of mapper interface、method or parameter
* Shortcut: Option + Enter(Mac) | Alt + Enter(Windows)
* To generate statement quickly, you can provide some method generating models with "Idea Setting" -> "Mybatis"
