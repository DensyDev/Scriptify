# File System
Example of working with the file system using scripts

```js
// Downloading a file from url
const url = "https://repo.densy.org/snapshots/org/densy/scriptify/script-js/1.0.3-SNAPSHOT/maven-metadata.xml";
const fileName = "maven-metadata.xml";

if (!existsFile(fileName)) {
    print(`File ${fileName} not found. Downloading...`)
    downloadFromUrl(url, fileName)
}

const foundFiles = [];

// Search all files in the current folder
listFiles("").forEach(file => {
    print(`Found file in folder ${file}.`)
    // Get the file name and delete the file maven-metadata.xml
    const foundFileName = normalizePath(file).replace(`${normalizePath(baseDir)}/`, "");
    if (foundFileName === fileName) {
        print(`File ${file} found. Delete it.`);
        deleteFile(file);
    }
    foundFiles.push(file);
});

// Write all saved current actions to a file
writeFile("summary.json", JSON.stringify(foundFiles));
```