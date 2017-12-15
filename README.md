# Distributed File System
Sorcha Nolan 13317836  
NFS File System in Java   

This implementation is an NFS-style system implementing file servers, a directory service, a client text editor supported by a client proxy library, a locking system and caching system. Sockets are used as a transport medium.   

## Set up   
This system is written in Java, using Maven for project management. It also incorporates two MySQL databases. A run.sh file is included which will run the system. This script takes some parameters - the first being the directory service port number, the next is the number of file servers to be run, the next parameter is the file server port, next is the IP address of the directory service (and all services as this script runs the system on the same machine), and finally a port number for the client proxy.   

## Client Editor   
The front-end for the system is a simple text editor with some basic functions. Initially the menu bar allows the user to either open an existing file or create a new file. A list of accessible files is requested from the directory service. When a new file is created, it is stored in one of the file servers. Opening or creating a new file automatically present the user with the read-only version of the file. The options of 'Edit' and 'Finish Editing' then appear. If the user wants to edit the document, they can click on 'Edit' and receive a writable version of the file (unless it is currently locked by another user). When the user is finished editing, they can click 'Finish Editing', which will update the servers on the new version of the file (and release the lock on the file).

## Client Proxy
The client proxy lies behind the text editor, and processes the interactions the user has with the system, communicating with both the directory service and the relevant file servers. When an action is performed on the text editor, the client proxy processes it and sends the relevant request to the servers. 
A 'New File' request will be sent to the directory service, which will respond with an assigned file server where that new file can be stored. The client proxy then connects to the file server, and sends the new file information to the file server for storage. 
Before an 'Open File' request can be made, the client proxy requests all files hosted on running servers from the directory. This is presented as a dropdown menu in which the user can click on the file name of the file they want to open. A request is sent to the directory service about that file, and it responds with the file server it is hosted on. The client connects to this file server, and requests the file as readonly. 

## Directory Service  
This service is connected to a MySQL database, where file-server mappings are stored, along with other relevant system information. When a file server starts up, it registers itself with the directory, so the directory knows which servers are running at any time, and only make files available that are stored in running servers. Apart from this, the directory service deals with requests from clients and keeps the database data consistent with system state.  

## File Servers
File servers register themselves initially with the directory service, and use file storage in a local folder to persist their files. A new connection spins up a new thread, in which read and write requests are processed accordingly.

## Locking Service
The locking service is implemented alongside the directory service, using the same database to store information about locks and lock queues. When the user clicks 'Edit', the locking service checks the database for any current locks on the file. Locks expire after 10 minutes. If there are no locks, the locking service secures a lock for the user and alerts the user, allowing a writeable file to be accessed. If the file to be accessed already has a lock on it, the user will be sent a pop-up notification, telling them that they currently only have readonly access, but will be notified when they can write to the file. They are then added to a locking queue for that file (stored in the database). A runnable regularly polls the database for locks that have been removed or expired. When this occurs, the locking service sends an update to the client at the top of the queue, alerting them through a pop-up that they now have access to the file, and giving them that access on the file. 

## Caching
Client-side caching is implemented, and caching information is stored in a database connected to each file server. This stores information about which users have accessed which files, so that the system is aware of which caches to invalidate on file update. On the client side, a cache folder is created, and added to whenever the user requests to read a new file. When a file is updated on the file server through a write request, all users who currently have that file in their cache are sent an invalidate request, which removes the file and replaces it with the updated version. 
