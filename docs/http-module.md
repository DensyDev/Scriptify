# HTTP Module

The HTTP module is:

```java
org.densy.scriptify.http.script.module.HttpScriptModule
```

Module name:

```text
http
```

## Setup

```java
script.getModuleManager().addModule(new HttpScriptModule());
```

With restricted Java member access:

```java
script.getModuleManager().setScriptAccess(ScriptAccess.EXPLICIT);
```

`HttpRequest`, `HttpMethod`, and `OutputType` are annotated for explicit access.

## Exports

| Export | Type | Description |
| --- | --- | --- |
| `HttpRequest` | Java class | Request object. |
| `HttpMethod` | Java enum | HTTP method enum. |
| `OutputType` | Java enum | Response output type enum. |
| `createHttpRequest` | Scriptify function | Factory function for `HttpRequest`. |

## HttpMethod

Supported values:

- `GET`
- `PUT`
- `POST`
- `DELETE`
- `PATCH`
- `HEAD`
- `OPTIONS`
- `TRACE`

## OutputType

Supported values:

- `STRING`
- `BYTES`

## HttpRequest

Constructor:

```java
new HttpRequest(String url, HttpMethod method)
```

Exported methods:

| Method | Description |
| --- | --- |
| `setBody(body, mediaType)` | Sets request body and media type. |
| `addHeader(key, value)` | Adds a header. |
| `send(outputType)` | Sends the request. Accepts `OutputType` or string. |

## JavaScript Usage

```js
import * as http from "http";

const request = new http.HttpRequest("https://example.com", http.HttpMethod.GET);
request.addHeader("Accept", "text/plain");

const response = request.send(http.OutputType.STRING);
```

Factory function:

```js
import { createHttpRequest } from "http";

const request = createHttpRequest("https://example.com", "GET");
```

## Behavior

The module uses OkHttp. Each `send` creates a new `OkHttpClient`, builds an OkHttp request, sends it synchronously, and returns either response text or response bytes.

For `POST` and `PUT`, an empty request body is created when no body was configured.

## Security Notes

The HTTP module performs network requests. Treat it as a privileged capability. `ScriptAccess.EXPLICIT` controls Java members on exported classes, but it does not block the exported `createHttpRequest` function or network access itself.
