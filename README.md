OPS-Knime
=========

OPS-Knime is a project devoted to ease the process of including OpenPHACTS services (http://dev.openphacts.org) into the Knime workflow engine (http://www.knime.org/)
where:<br/>
<ul><li>OpenPHACTS reduces the barriers to drug discovery in industry, academia and for small businesses. </li>
<li>
Knime offers a workflow system by implementing a low-barrier visual programming environment for researchers dependent on data and statistical data-crunching methods/algorithms.
</li> 
</ul>
In other words: <b>OpenPHACTS</b> gives pharmacologists easy access to a wide range of data relevant to their research and <b>Knime</b> allows scientists to partially automate their research workflows which
are part of their research process.   



KNIME-UTILS: SWAGGER & JSON
----------
The <a href="http://dev.openphacts.org">OpenPHACTS services</a> are accessible via <a href="http://www.w3schools.com/tags/ref_httpmethods.asp">HTTP-GET/a> .
These services are described in SWAGGER, and rendered by 3scale into convenient, human-readible html format. SWAGGER is a quite new and promising way to describe REST services. 
It contains all the necessary information needed to be able
to understand the purpose and how to technically invoke  services that are provided. For example the address of the server, the path to the services and the parameters
that the services require or which are optional to fine-tune the desired functionality of the service.


The OPS-Knime project is mainly concerned to give the Knime community easy access to the OpenPHACTS services. Given that the SWAGGER file already contains all information
needed to invoke any service provided by OpenPHACTS and is in described in a standard, unambiguous format, JSON, it was quite straightforward to create two utility
components for the KNIME community: 1) a SWAGGER node and a JSON serializer.


JSON_to_KnimeTable
------------------
This node parses the JSON from a given url to a KNIME table by a recursive traversal through the JSON objects and JSON arrays. The column names
are a concatenation of the keys found when walking through the hierarchical path. The resulting tables looks quite similar to an online JSON-to-CSV tool
found <a href="http://json-csv.com"> here </a>


Installing the nodes
------------
* Download "org.openphacts_1.0.0.zip" and unpack it in the plugins folder of your KNIME installation


Development - Source
----------
The source code of the plugins are in the folder "OPS-Knime" folder which can be checked out as
a java project in Knime-Eclipse.

TODO
-------------
* more nodes
* better nodes
