##ONEScheduler
ONEScheduler is a custom cloud scheduler for OpenNebula and is currently still under developement. First release is expected in January 2017.
ONEScheduler is being developed as a replacement for current scheduler that OpenNebula provides.
Unlike the OpenNebula's scheduler, our scheduler has modular design and can be easily extended.

###ONEScheduler offers:
- fair-sharing algorithms
- better handling of the hosts and datastores suitable for the virtual machine
- simple interfaces for policies
- ease of incorporating new scheduling and fair-sharing policies
- its own configuration file (configuration.properties)

###ONEScheduler will introduce:
- possibility of testing new algorithms in a simulation mode
- possibility of running many algorithms in parallel and choosing the best solution

ONEScheduler is a maven project and for running it just download or clone the project and run it in your IDE.

ONEScheduler can be used in two modes:
(You can swith between these two modes in the configuration file under "useXml" field)
- connecting to OpenNebula and obtaining the xml files from OpenNebula (useXml=false)
- providing the xml files in form of "hostpool.xml", "vmpool.xml" etc. (useXml=true) (This mode will be used for the simulations)

For connecting to OpenNebula you need to fill the "secret" and "endpoint" field in configuration file.
"secret" - A string containing the ONE user:password tuple. Can be null.
"endpoint" - Where the rpc server is listening, must be something like "http://localhost:2633/RPC2". Can be null.

For running it as a replacement of OpenNebula, you should shut down the OpenNebula scheduler daemon.

This project is a part of a master's thesis at Faculty of Informatics at Masaryk University.
