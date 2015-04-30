# sendito
A messenger for the rest of us.

## Development platform
- A CouchDB instance
- NetBeans

## Requirements
- CouchDB

## Installation
1. Install CouchDB
2. Add an admin user
3. Add regular users
4. Add database named *sendito*
5. Configure replication to replicate *sendito*, *_users* and *_replicator*
6. Create new *servers.list* and add Servers to the database using the AdminApp (needs to be modified and launched from the IDE)
7. Distribute your *servers.list* along with the application