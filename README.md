# httpd
Simple HTTP service based on NanoHTTPD.
Simple example, you can see the class `StaticResourcesHandler` and `SimplyDreamHttpd`

# How to use
Like Servlet, to implement the `RequestHandler` interface, and then call `DreamHttpd.registerHandler()` to register the handler.

# Attention
The handler is in the order of a sequence, and it will only call the first handler that is qualified.
