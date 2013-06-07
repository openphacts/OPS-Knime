OPS-Knime
=========

Project for integration of OPS and the Knime workflow engine (http://www.knime.org/)

The committed code shows an example Knime plug-in that retrieves data based on the Enzyme Classification API using the Linked Data API. 

Installation
------------
* Copy the jar file in plugins to the "dropins" folder of your KNIME installation

Development
----------
* To do development on the plugin you should use the knime sdk (http://www.knime.org/node/81) which is a specialized version of eclipse. 
* The key classes are
** OpenPhactsDataNodeModel.java
** OpenPhactsDataNodeDialog.java 

TODO
-------------
* Better error handling
* Configuration options should present only available choices
* Configuration options should give range restrictions for the activity value
* Currently, only returns the first 10 resutls, this can be updated either to grab all results or walk through the pages
