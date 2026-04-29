# Standard Module

The standard module is:

```java
org.densy.scriptify.common.script.module.StandardScriptModule
```

Module name:

```text
standard
```

You can add it directly:

```java
script.getModuleManager().addModule(new StandardScriptModule());
```

```js
import * as standard from "standard";
```

Or copy it to another name:

```java
SimpleScriptInternalModule std = new SimpleScriptInternalModule("std");
std.copy(new StandardScriptModule());
script.getModuleManager().addModule(std);
```

```js
import * as std from "std";
```

## Constants

| Name | Description |
| --- | --- |
| `baseDir` | Current JVM working directory. |
| `osName` | Operating system name. |

## Utility Functions

| Function | Signature | Description |
| --- | --- | --- |
| `print` | `print(...args)` | Prints arguments joined by spaces to `System.out`. |
| `arrayOf` | `arrayOf(...args)` | Returns a Java array. |
| `listOf` | `listOf(...args)` | Returns a Java `List`. |
| `setOf` | `setOf(...args)` | Returns a Java `Set`. |
| `shuffleArray` | `shuffleArray(array)` | Returns a shuffled copy of a list. |
| `regex_pattern` | `regex_pattern(pattern)` | Compiles a Java regex `Pattern`. |
| `regex_match` | `regex_match(regex, value)` or `regex_match(pattern, value)` | Matches a full string. |

## Crypto Functions

| Function | Signature | Description |
| --- | --- | --- |
| `base64encode` | `base64encode(string)` | Encodes a string to Base64. |
| `base64decode` | `base64decode(string)` | Decodes Base64 as UTF-8. |
| `md5` | `md5(input)` | Returns MD5 hex digest. |
| `sha256` | `sha256(input)` | Returns SHA-256 hex digest. |

## Random Functions

| Function | Signature | Description |
| --- | --- | --- |
| `randomUUID` | `randomUUID()` | Random UUID string. |
| `randomBoolean` | `randomBoolean()` | Random boolean. |
| `randomInt` | `randomInt(max)` or `randomInt(min, max)` | Random int. |
| `randomLong` | `randomLong(max)` or `randomLong(min, max)` | Random long. |
| `randomFloat` | `randomFloat(max)` or `randomFloat(min, max)` | Random float. |
| `randomDouble` | `randomDouble(max)` or `randomDouble(min, max)` | Random double. |

Each call creates a new `java.util.Random`.

## File Functions

| Function | Signature | Description |
| --- | --- | --- |
| `existsFile` | `existsFile(filePath)` | Checks path existence. |
| `readFile` | `readFile(filePath)` | Reads a file as string. |
| `writeFile` | `writeFile(filePath, fileContent)` | Writes a string; returns written path. |
| `deleteFile` | `deleteFile(filePath, recursive?)` | Deletes file or directory. |
| `moveFile` | `moveFile(original, target)` | Moves/renames a file. |
| `listFiles` | `listFiles(filePath)` | Lists absolute paths in a directory. |
| `downloadFromUrl` | `downloadFromUrl(url, filePath)` | Downloads URL content into a file. |
| `joinPath` | `joinPath(...args)` | Joins path parts with `/`. |
| `normalizePath` | `normalizePath(path)` | Replaces `\` with `/`. |

File functions use Scriptify `SecurityFileSystem`.

## OS Functions

| Function | Signature | Description |
| --- | --- | --- |
| `env` | `env(name)` | Returns an environment variable. |
| `execCommand` | `execCommand(input)` | Runs an OS command and returns stdout/stderr. |

`execCommand` is a privileged capability. Do not expose it to untrusted scripts.

## Zip Functions

| Function | Signature | Description |
| --- | --- | --- |
| `zipFile` | `zipFile(filePath, compressedFilePath)` | Zips a file or directory. |
| `unzipFile` | `unzipFile(compressedFilePath, decompressedPath)` | Extracts an archive. |
| `smartZipFile` | `smartZipFile(filesPath, compressedFilePath, patterns)` | Zips entries whose names match regex patterns. |
| `smartUnzipFile` | `smartUnzipFile(compressedFilePath, decompressedPath, patterns)` | Extracts archive entries whose names match regex patterns. |

Unzip functions validate extracted paths to prevent archive entries from escaping the destination directory.
