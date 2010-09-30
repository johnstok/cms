# Developing the browser CC client
Developer notes for the CC client library.

## Communicating with the server
CC clients talk to a CC server by sending JSON documents over HTTP.


### Implementing a remote method
The client-core module provides a simplified HTTP library for talking to the server. A library is also provided to serialise standard CC types to and from JSON.

The `ccc.client.core.Command` interface encapsulates a single CC API method. A client command accepts API method parameters via its constructor. To invoke the command you call the `invoke` method specifying the subject of the command and the callback that will handle the command's outcome. A command implementation encapsulates all JSON serialisation and HTTP communications.

_The legacy `RemotingAction` class should no longer be used for communicating with a CC server._


### Invoking a remote method.
Since commands are invoked asynchronously you must provide a callback to handle the result. Either the `onSuccess` or the `onFailure` method will be called depending on the outcome. The `DefaultCallback` class is provided to provide standard exception handling on the remoting event bus.

A number of standard callbacks are provided. Typically these will push an appropriate event onto the remoting bus to indicate the success of the remote method's invocation.


### Hypermedia implementation of the server remoting.
CC aims to provide a [RESTful][rest] remoting API. As such the implementation runs over JSON/HTTP and adheres to the following principles:

* _Identification of resources._

  Each resource (user, page, etc') can be identified by a unique URL.

* _Manipulation of resources through these representations._
* _Self-descriptive messages._
* _Hypermedia as the engine of application state._

  Each representation received from the CC server carries with it a collection of links that specify further methods that may be invoked. This differs from other implementations of RMI over HTTP where the binding between a method and a URL are explicitly bound. The CC client only requires knowledge of the root 'API' URL to connect to a server. It is for this reason that an existing object must be passed along to a `Command` via its `invoke` method.

  _Caveat_ - the HTTP methods for each API method are still hard-coded in the command class.


## Implementing a dialog
The CC browser client makes use of the Model View Presenter (MVP) pattern for UI classes. MVP maximises separation of general UI logic from UI toolkit specifics (GXT, Swing, etc').


### Presenter implementation
The MVP presenter should implement the `AbstractPresenter` class. An `AbstractPresenter` is parameterised by the type of view and the type of model that the presenter supports. Presenter classes will automatically be registered to listen for events on the _remoting_ event bus. When a presenter is no longer required `dispose` must be called to unregister the listener.


### View implementation
The MVP view must implement the `View` interface, and if data is being edited will typically implement `Validatable`. You should aim to make the view implementation as '[passive][passive]' as possible. References to UI toolkit classes should remain within the view implementation.


### Communicating between view & presenter
A presenter typically receives the view it should use via its constructor. When the presenter calls the `show` method on the `View` interface it may pass a reference to itself to allow bi-directional communication. For a typical dialog box editing data the presenter would implement the `Editable` interface. This would allow the view to notify the presenter when a user clicks the 'save' or 'cancel' buttons.





[passive]: http://martinfowler.com/eaaDev/PassiveScreen.html "Passive View Pattern"