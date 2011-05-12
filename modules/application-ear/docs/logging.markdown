# Configuring logging for the server.
All logging for Content Control is managed with the log4j library. Logging is
configured using the log4j.properties file in the root of the server EAR
file.

In particular it is important to specify a unique file for each instance of
Content Control to log to.

The server installation scripts prompt the server administrator to specify an
appropriate path for the log file.