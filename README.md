[![Build Status](https://travis-ci.org/gam2046/httpd.svg?branch=master)](https://travis-ci.org/gam2046/httpd?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.gam2046/httpd/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.gam2046/httpd)
 
# httpd
Simple HTTP service based on NanoHTTPD.
Simple example, you can see the class `StaticResourcesHandler` and `SimplyDreamHttpd`

# How to use

Like Servlet, to implement the `RequestHandler` interface, and then call `DreamHttpd.registerHandler()` to register the handler.

## Create a HTTP server (Optional)

Create a class to extend `DreamHttpd`, like this:

```java
public class SimplyDreamHttpd extends DreamHttpd {
    public SimplyDreamHttpd(int port, String webRoot) {
        super(port, webRoot);
    }
}
```

The `webRoot` can be `null`, if you needn't to access local resources. You can override the method in accordance with the need, but usually it is not necessary. Note that there is no special case do not override the `serve` method, if you must override the `serve` method, you must pay attention to the processing of the handler, otherwise it will result in incorrect results. If you don't have special needs, you don't need this. Directly create an instance of `DreamHttpd`.

## Create a HTTP handler

the handler is like the servlet in J2EE, here is an example :

```java
public class StaticResourcesHandler extends AbstractRequestHandler {

    @Override
    public boolean doHandler(Map<String, String> args, String uri) {
        return true;
    }

    @Override
    protected NanoHTTPD.Response onGet(String root, Map<String, String> args, NanoHTTPD.IHTTPSession session) {
    }
}
```

A handler must implement `RequestHandler`. In the example, I inherited `AbstractRequestHandler`, usually, you can also do so, there is no need for the realization of `RequestHandler`. `AbstractRequestHandler` to achieve the basic operation, so it can be based on business needs, override the `onXXX` method (such as `onGet`).

The `doHandler` method needs to return a Boolean value to tell the server whether the handler can handle the request. Usually do not like this in the case of writing (direct return `true`), so that the result is in the handler after the registration of all handler will not be able to handle any requests.

## Create a cache handler (Optional)

The cache handlers are very likely normal handler, the only difference is that we need to inherit the `AbstractCache` class. 

## Register your HTTP handler

A Handler corresponds to a request, a HTTP server can have more than one handler. So the order in which the handler is registered is the order in which it is called.
For example, there are A and B two Handlers can handle the same request, but A first registered, then the request will only be processed by A.

`DreamHttpd.registerHandler(new RequestHandler())`

## Start your web server

Everything is ready, now only need to start the server.

`DreamHttpd.start()`
    
# Attention

The handler is in the order of a sequence, and it will only call the first handler that is qualified.