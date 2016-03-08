 [![Build Status](https://travis-ci.org/gam2046/httpd.svg?branch=master)](https://travis-ci.org/gam2046/httpd?branch=master)[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.gam2046/httpd/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.gam2046/httpd)
 
# httpd
Simple HTTP service based on NanoHTTPD.
Simple example, you can see the class `StaticResourcesHandler` and `SimplyDreamHttpd`

# How to use
Like Servlet, to implement the `RequestHandler` interface, and then call `DreamHttpd.registerHandler()` to register the handler.

# Attention
The handler is in the order of a sequence, and it will only call the first handler that is qualified.
