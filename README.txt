Morcego 3D Network Browser - v0.5.0
-----------------------------------

Morcego is a java applet for visualizing and browsing graphs (networks)
in 3D. It's goal is to be easily embedded in web (by now) applications and,
for future releases, to become a framework. This documentation is divided
in two parts: first one is about using applet, second on how to embbed
Morcego in other applications and how to upgrade to v0.5.0.

This document is divided in the following sections:

1- Using the applet
2- Embedding applet in application 
3- Configuration
4- Upgrading

1- USING THE APPLET

Once Morcego applet window opens, you'll see (after loading, that
may take some moments) an animation of little balls connected to each
other in a network getting arranged in space - a cool effect by the way ;-).

The possible actions:

  * ROTATE    - Drag mouse on empty space and you'll be able to see the graph
                from different angles. If you drag fast and relase, like throwing,
                the graph will keep spinning.

  * GET INFO  - Put the mouse cursor over a node, and it will become a hand. If
                there's more info about that node, a description box will appear.
               
  * NAVIGATE  - Click on a node and it will go to center. When you do that, the
                new neighbourhood will appear in a few moments (wait a bit for
                the info to come from server). Clicking on center node will
                open the node URL in browser window.
               
  * MOVE NODE - Click on a node and drag to move it. The graph will balance
                itself as you move. This is useful to see how a node influences
                the whole network.

2- EMBEDDING APPLET IN APPLICATION

For integrating Morcego applet in your web application, you need an special Morcego server
to feed the applet with graph data. This is usually done with XMLRPC, the only transport
way available until now (check section 2.4 on how to implement other ways).

Implementing this used to be a tough task, but since v0.6 Morcego comes with a demo application
and a set of PHP classes for integration with other softwares. You have several options, depending
on your technical skills and time available. You can:

* Make a static mindmap website by editing some XML data files and templates
* Implement your own server in PHP to make a dynamic map, by extending a PHP class
* Develop a XMLRPC server that implements Morcego's protocol in any language you wish
* Make a new transport layer in java, by extending a java class and creating a server to feed it.

2.1 - Making a static mindmap

If you don't have much technical skills, but would like to make an online mind map, you can do one
by editing some XMl files and putting a simple application in a webserver with PHP:

* Copy demo/ to a directory in your web server
* Open a browser and check the results
* Take a look at the XML files under templates/data directory. Edit them to customize the mindmap.
* Check also the .tpl files at templates/ dir. Modify those to customize the layout. For help on that, check http://smarty.php.net

2.2 - Integrating to your PHP application

Morcego also comes with a set of PHP classes to make your own server. You'll need two files: your server (let's call it server.php),
a php file available through HTTP, and a subclass of Morcego_Graph (let's call it MyGraph)

* server.php

Very simple, check demo/server.php on how to do that. All you need to do is create a Morcego_Graph object (note that Morcego_Graph is an
abstract class and must be subclassed) and use it to instantiate a Morcego_Server object.

* MyGraph

This is the core of your server. You must implement the method getNode($nodeId), that takes the nodeId and returns a data structure
containing all relevant data about node with id $nodeId and the links it has to/from other nodes.

The data structure has the exact same format of the getSubGraph() method (explained below at 2.3), but you usually want to return
only one node in "nodes" section. I say usually, because it might be useful to return a bigger set of nodes and links, for example
in raw XMl server used by the demo: you might want to defined many nodes in one XML files to make your job easier (note that in the
demo you can't start your navigation in some of the nodes that doesn't have it's own XMl file). 

2.3 - Developing your own XMLRPC server

The XMLRPC server should implement two methods:

 * integer getVersion()

This method returns the XMLRPC protocol version. Current version is 2, so if you're
implementing the server according to instructions below, this method should return "2".
In case this method is not present, version 1 is assumed, and so data structure for 
older versions of Morcego (0.4 and below) will be expected.

 * struct getSubGraph(string nodeId, int depth)

This method returns a part of the graph containing the node with id
nodeId and all nodes with distance lesser or equal than depth.
The returned struct must be as follow, {} indicates structs and []
arrays, fields marked with * are optional

{
  nodes => {
                    nodeId => {
                                            * type => "Round",
                                            * color => "#FF0000",
                                            * actionUrl => "http://...",
                                            * description => "node description, shown in box",
                                },
                    node1Id => {},
                    node2Id => {...}
                  }
  links => [
                {
                    from => "nodeId",
                    to => "node2Id",
                  * type => "Solid"
                },
                
                {
                    from => "nodeId",
                    to => "node1Id",
 				  * type => "Dashed"
 				}                 
                
           ]
}

PROPERTIES FOR NODES AND LINKS

  Each node can have the following properties:
  
   * type: "Round", "Text" or "Image".
           Round nodes are represented by balls with a title above (that can be empty).
           Text nodes have just the title.
           Image nodes are represented by image defined by "image" property, an url.

   * actionUrl: The URL that will be loaded by this node.
   
   * description: Text that will show when mouse passes over the node
   
   * color: Overrides nodeDefaultColor and defines the color of node if it's Round.
   
   * bodyCharge: Overrides nodeCharge configuration and sets the eletrostatical charge of
                 the node. Bigger values will put this node far from others.
                 
   * bodyMass: Overrides nodeMass configuration and sets the mass of the node. Bigger
               values will give more inerce to the node and make it move slower.
               
  Each link can have the following ones:
  
   * type: "Solid", "Dashed", "Directional", "Bidirectional"
           Solid links are solid lines from one node to another.
           Dashed is a dashed line.
           Directional draws an arrow from one to other
           Bidirectional draws a line with arrow in both ends.
           
   * from (mandatory): The id of the node at one end.
   
   * to (mandatory): The id of the node at other end.
   
     ** Note: the order of "from" and "to" only matters for "Directional" type of link.
     
   * description: Text that will show when mouse passes over the link
   
   * color: Overrides linkDefaultColor and defines color of link
   
   * springSize: Overrides springSize configuration option for this link and sets its size.
                 Bigger values will put nodes far from each other.
                 
   * springElasticConstant: Overrides elasticConstant configuration option for this link.
                            Bigger values will lower variation between springSize and final size.
                            Smaller values will make animation smoother.
                            
NAVIGATION WITH JAVASCRIPT

Using javascript, you can call the method navigateTo(nodeId) of applet to navigate to a node.
                            
3- CONFIGURATION

There are configuration variables on Config class, every var can be overrided
by an applet param with same name. The only ones that you must override
are serverUrl and startNode. All variables:

     - serverUrl (string): Full URL of XMLRPC server. MANDATORY
     - startNode (string): ID of the starting node. MANDATORY

  Colors and layout settings

     - showMorcegoLogo (boolean): default true
                                  Show software's logo on position below
     - logoX (integer): default 10
     - logoY (integer): default 10

     - showArcaLogo (boolean): default true
                               Show deverlopers' logo on position below
     - arcaX (integer): default 480
     - arcaY (integer): default 460

     - backgroundColor (color): default #FFFFFF

     - linkDefaultType (string): default "Solid"
                                 Can also be "Dashed" or "Directional".
                                 Overrided by property "type" of each link.

     - linkDefaultColor (color): default #787878
     

     - nodeDefaultType (string): default "Round"
                                 Can also be "Text" or "Image".
                                 Overrided by property "type" of each node.

     - nodeDefaultColor (color): default #FF0000
     
     - nodeDefaultImage (string or url): default "default.gif"
                                         This is used for nodes of type "Image".
                                         default.gif is packaged with applet, you should put an url here.
                                         Be careful, images take a lot of cpu, use very simple ones.

     - nodeBorderColor (color): default #000000

     - textSize (integer): default 40
                           Size of text in node
     - nodeSize (integer): default 30
     					   Size of the node
     - minNodeSize (integer): default 0
                              Minimum size of node, 0 will make far nodes disappear
     - centerNodeScale (float): default 2
                                Proportion of center node, to put it in more evidence than others.

     - descriptionColor (color): default #282828
								 Color of text in node's description box.

     - descriptionBackground (color): default #c8c8c8

     - descriptionBorder (color): default #000000

     - descriptionMargin (integer): default 4
                                    Margin around text in description box.

     - windowWidth (integer): default 600
     - windowHeight (integer): default 500

     - viewStartX (integer): default 0
     - viewStartY (integer): default 0
     - viewHeight (integer): default same as windowHeight
     - viewWidth (integer): default same as windowWidth


  Camera configuration

     - cameraDistance (integer): default 500
                                 Distance from camera to center node.
                                 
     - minCameraDistance (integer): default 150
                                    Minimum distance from camera to nearest node.
                                    In case a node get too near, cameraDistance is increased.

     - fieldOfView (integer): default 200
                              The greater fieldOfView is, bigger is everything.


  Physical constants used to balance the graph. For best position, Morcego uses a physical
  simulation in which each node is an eletrical charge with mass and no dimension, each
  connection is a spring and whole system is in a viscose environment.

     - frictionConstant (float): default 0.4
     							 Small values increases the time for graph to get
                                 balanced, while very big values will make the whole
                                 process very slow. 

     - elasticConstant (float): default 0.5
                                This is the default elastic constant for links, smaller values will
                                make movements smoother but graph will take more time to balance.
                                Overrided by property with same name for each link.

     - punctualElasticConstant (float): default 0.8
                                        Elastic constant of spring that will move some node to center
                                        when it's clicked. The smaller this value, the smoother is the
                                        navigation.
                                        
     - eletrostaticConstant (float): default 1000
     								 Bigger values will put nodes far from each other.

     - springSize (float): default 100
                           Bigger values increase the distance of nodes

     - nodeMass (float): default 5
                         Bigger values increase the inerce of system

     - nodeCharge (float): default 1
                         Bigger values increase the strengh of repulsion of nodes


  Rotation angle limits. These configure mouse sensitiveness for rotating the graph.

     - maxTheta (float): default 20.0f

     - minTheta (float): default 1.0f

   Performance X Quality options

     - renderingFrameInterval (integer): Delay between each frame of animation, in miliseconds.
                                         default 50.
     
     - balancingStepInterval (integer): Interval between each calculation of forces to
                                        balance the graph, in miliseconds. Increase will 
					raise performance and lower animation quality.
					default 50.

     - fontSizeInterval (integer): Looks like font rendering is cached, so that if a lower
                                   number of font sizes are rendered the performance is
								   better. Increasing this number will increase performance
								   but lower text depth impression. default 5.

   General configuration

     - transport (string): default "Xmlrpc"
     					   Kind of transport layer used, xmlrpc is the only one at moment.

     - feedAnimationInterval (integer): default 100
                                        Time between appearance of each node, in milisecs.

     - loadPageOnCenter (boolean): default true.
								   If set to true, target page will be loaded on browser
                                   when user navigates to node.

     - navigationDepth (integer): default 2
                                  The distance from farest node to center. The bigger it
                                  is, more nodes will be fetched around center.
     
     - controlWindowName (string): default "morcegoController"
     							   Name of window in which URLs should be loaded. 
                                   This only has any effect if the nodes have an actionUrl
								   set by server.

		
4- UPGRADING

Upgrading 0.4 -> 0.5

To upgrade from v0.4 to v0.5, put morcego-0.5.0.jar in place of morcego-0.4.jar and check below
the changes made in applet that may affect your application. You can remove xmlrpc jar file, it's
now packaged in morcego's jar. If you want to benefit from new features like link properties,
dashed and directional links, you have to implement in your server the new version of XMLRPC
transport protocol. For that, check "EMBEDDING APPLET IN APPLICATION" section above.

  - Configuration variables renamed

     * linkColor: renamed to linkDefaultColor
     * transportClass: renamed to transport.
                       The suffix "Transport" in this configuration is not used anymore.
     
  - New default values
       
     * windowWidth: 600 instead of 500
     * elasticConstant: 0.3 instead of 0.5
     * punctualElasticConstant: 0.8 instead of 1
     * feedAnimationInterval: 100 instead of 500
     
  - Removed options
  
     * adjustCameraPosition: this got obsolete with new minCameraDistance, that makes camera adjustment
                             more flexible. If you want behaviour of adjustCameraPosition, just set
                             minCameraDistance to same value as cameraDistance.
  
     * viewHeight, viewWidth: these are not really gone, but they now default to windowHeight and windowWidth, so
                              you don't have to configure them anymore. Right now it's useless to define a view
                              smaller than window.
                              
  - New configuration options
  
     * minCameraDistance: defines the minimum distance of the camera from any node. Use this instead of
                          adjustCameraPosition.
     * centerNodeScale: see above

Upgrading 0.3 -> 0.4

To upgrade from v0.3 to v0.4.0 you basically have to:

  1- put morcego-0.4.0.jar in place of morcego-0.3.jar

  2- Check if you have configured any of these variables:

     * ballBorderColor: renamed to nodeBorderColor

     * minBallSize: renamed to minNodeSize

     * cameraX, cameraY, cameraZ: all three substituted by cameraDistance. Note the new
                                  variable adjustCameraPosition.

  3- Check if any of these new behaviour bothers you and change them as desired:

     * Having software logo and developer's logo in screen. You might have to configure
       arcaX and arcaY if your window is not default size.

     * Now camera distance is relative to plane of nearest node instead of fixed origin.
       Disable adjustCameraPosition to fix camera.

     * Now node's target page is loaded every time user navigates on graph, instead of
       having to click center node. Disable that by setting loadPageOnCenter to false.

  4- Remove any of the following configuration vars that are not used anymore:

     - originX, originY: now it's always the center os screen
     - originZ: no sense being different from zero.

     - universeRadius, maxNodeSpeed: the new physical model don't need limits to avoid
                                     weird behaviour on high values.

     - linkedNodesDistance: also obsoleted by physical model, check now springSize
