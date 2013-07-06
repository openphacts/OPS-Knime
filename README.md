OPS-Knime
=========

OPS-Knime is a project devoted to ease the process of including OpenPHACTS services (http://dev.openphacts.org) into the Knime workflow engine (http://www.knime.org/)
The goal of OpenPHACTS is to reduce the barriers to drug discovery in industry, academia and for small businesses. 
Knime is a workflow system offering a low-barrier visual programming environment for researchers dependent on data and statistical data-crunching methods/algorithms.

OpenPHACTS gives pharmacologists easy access to a wide range of data relevant to their research. Knime allows scientists to partially automate their research workflows which
are part of their research process.   
OPS-Knime connects two domains: 1)the domain of available pharmalogical data and 2) the domain of automating workflows performed by researchers in the quest of finding answers
to their research questions. 


KNIME-UTILS: SWAGGER & JSON
----------
The OpenPHACTS services are accessible via a REST web interface (http://dev.openphacts.org).
The services are described in SWAGGER, and rendered by 3scale into convenient, human-readible html format. SWAGGER is a quite new and promising way to describe REST services. 
It contains all the necessary information needed to be able
to understand the purpose and how to technically invoke  services that are provided. For example the address of the server, the path to the services and the parameters
that the services require or which are optional to fine-tune the desired functionality of the service.


The OPS-Knime project is mainly concerned to give the Knime community easy access to the OpenPHACTS services. Given that the SWAGGER file already contains all information
needed to invoke any service provided by OpenPHACTS and is in described in a standard, unambiguous format, JSON, it was quite straightforward to create a utility for KNIME that
understands SWAGGER. and JSON.



*  Enzyme Classification API
* Target info
* Compound info
* Pathways info 
* Text to conceptwiki


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
