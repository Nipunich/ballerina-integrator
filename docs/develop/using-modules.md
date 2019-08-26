# Using Modules

A *module* is a directory that contains Ballerina source code files and is part of a namespace. Modules facilitate collaboration, sharing, and reuse. Modules can include functions, connectors, constants, annotations, services, and objects. To share a module among programs, projects, and users you need to push the module into a repository.

Modules:

<ol>
<li>May or may not have a version</li>
<li>However, modules cannot be pushed into a registry for sharing without a version</li>
<li>Are referenced by `<org-name>/<module-name>` where `<org-name>` is a namespace from within a repository.</li>
</ol>

Module names can contain alphanumeric characters including dots `.`. Dots in a module name have no meaning other than the last segment after the final dot being used as a default alias within your source code.

## Setting up

Before you push your module, you must enter your Ballerina Central access token in `Settings.toml` in your home repository (`<USER_HOME>/.ballerina/`).

To get your token, register on Ballerina Central and visit the user dashboard at [https://central.ballerina.io/dashboard](https://central.ballerina.io/dashboard).

If you are connected to the internet via an HTTP proxy, add the following section to `Settings.toml` and change accordingly.

```
[proxy]
host = "localhost"
port = "3128"
username = ""
password = ""
```

## Pushing a Module

### CLI Command

Pushing a module uploads it to [Ballerina Central](https://central.ballerina.io/).

```
ballerina push <module-name>
```

### Organizations

When you push a module to Ballerina Central, the runtime validates organizations for the user against the org-name defined in your module’s `Ballerina.toml` file. Therefore, when you have more than one organization in Ballerina Central, be sure to pick the organization name that you intend to push the module into and set that as the `org-name` in the `Ballerina.toml` file inside the project directory.



## Importing Modules

Your Ballerina source files can import modules:

```ballerina
import [<org-name>]/<module-name> [as <identifier>];
```

When you import a module, you can use its functions, annotations, and other objects in your code. You can also reference the objects with a qualified identifier, followed by a colon `:`. For example, `<identifier>:<module-object>`.

Identifiers are either derived or explicit. The default identifier is either the module name, or if the module name has dots `.` included, then the last word after the last dot. For example, `import ballerina/http;` will have `http:` be the derived identifer. The module `import tyler/net.http.exception` would have `exception:` as the default identifier.

You can have an explicit identifier by using the `as <identifier>` syntax.

```ballerina
import ballerina/http;

// The 'Service' object comes from the imported module.
service hello on new http:Listener(9090) {

    // The 'Request' object comes from the imported module.
    resource function sayHello (http:Caller caller, http:Request req) {
        ...
    }
}
```

Or you can override the default identifier:
```ballerina
import ballerina/http as network;

service hello on new network:Listener(9090) {

    // The 'Request' object comes from the imported module.
    resource function sayHello (network:Caller caller, network:Request req) {
        ...
    }
}
```

## Module Version Dependency
If your source file or module is a part of a project, then you can explicitly manage version dependencies of imported modules within the project by defining it in `Ballerina.toml`:

```toml
[dependencies."tyler/http"]
version = "3.0.1"
```

If an import version is not specified in `Ballerina.toml`, the compiler will use the `latest` module version from a repository, if one exists.

```ballerina
import tyler/http;

public function main() {
  http:Person x = http:getPerson();
}
```

## Compiled Modules
A compiled module is the compiled representation of a single module of Ballerina code, which includes transitive dependencies into the compiled unit.

Modules can only be created, versioned, and pushed into a repository as part of a *project*.

## Running Compiled Modules
An entrypoint such as a `main()` or a `service` that is compiled as part of a named module is automatically linked into a `.balx`. You can run the compiled module `.balx`:

```bash
ballerina run module.balx
```


