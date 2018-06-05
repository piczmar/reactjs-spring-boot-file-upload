# Upload Form - Server
Spring Boot application which provides API to upload files.

Files are stored on server filesystem.
Metadata of files is stored in embedded H2 database.

## Design considerations
### File storage
There are some restrictions regarding the number of files possible to store in single folder on disk, depending on OS used.
E.g. in unix systems this is usually around 20000 files. This was taken into account and the application will store 
no more than `files.folder.max` files in subfolder inside `files.folder` location.
### Easy switch to other storage
Application architecture allows easily to provide different `StorageProvider` implementation, 
e.g. to put files in AWS S3 instead of local disk. Then storage provider dependency can be switched in `FileService`. 
### Consistent state
Files upload consists of two phases: 

1) Save metadata in SQL database
2) Store binary content on disk

What would happen if SQL operation fails or vice-versa? The state of the application should be consistent in a way
that there are no dangling files without metadata in database.
For this purpose `FileService.storeData` is transactional.
### API versioning
REST API is versioned using URL path scheme, e.g.:
```
/api/v1/upload
/api/v1/list

```
New breaking changes should be released as a new API version, e.g.:
```
/api/v2/upload
```
### CORS headers
API allows Cross-Origin Resource Sharing.
This is needed for client application running in different domain or port to access the API.
It is currently open for any domains and should be restricted to particular domain in production or the
server and client apps should be deployed in the same domain (e.g. running behind a proxy)

### API self-documenting
API is self-documenting using Swagger-generated docs available at:
[http://localhost:9080/swagger-ui.html#](http://localhost:9080/swagger-ui.html#)

## Configuration
Configure server port, max uploaded file size, file storage location, log4j configuration 
and other properties in `src/main/resources/application.properties`

## Build
```
mvn clean package
```

## Run
```
java -jar target/uploadform-0.0.1-SNAPSHOT.jar
```
or
```
mvn spring-boot:run
```

The application will start by default on port 9080

[http://localhost:9080/swagger-ui.html#](http://localhost:9080/swagger-ui.html#)

## Usage with cURL
```
curl -F file=@path_to_file -F title=Title_param -F details=Details_param http://localhost:9080/api/v1/upload
```
In the above URL additional optional parameters (supported: title and details) can be used together with uploaded file.

Example response: 
```
{"message":"Successfully uploaded","title":"Title_param","details":"Details_param","fileName":"build.sh"}
```


The file will be stored on disk and metadata will be stored in embedded database.

The name of the uploaded file must be unique. If file with the same name was already uploaded the APi will respond with error:

```
{
   "status":"BAD_REQUEST",
   "timestamp":"2018-05-30T22:22:09.169",
   "message":"Duplicate file upload",
   "debugMessage":"File with name build.sh has been already uploaded"
}
```

Metadata for uploaded files can be queried with:

```
curl -H 'Content-Type:application/json` http://localhost:9080/api/v1/list
```

Example response:

```
{
  "content": [
    {
      "name": "file",
      "contentType": "application/octet-stream",
      "contentSize": 255,
      "title": "Title_param",
      "details": "Details_param",
      "createdAt": 1527705399722
    }
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true
    },
    "pageSize": 10,
    "pageNumber": 0,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "first": true,
  "numberOfElements": 1,
  "sort": {
    "sorted": false,
    "unsorted": true
  },
  "size": 10,
  "number": 0
}

```

The API allows paging, e.g.:

```
curl -H 'Content-Type:application/json` http://localhost:9080/api/v1/list?page=0&size=1
```

It will query first page of entries with 1 entry per page.
Default page size (when no `size` parameter is provided) is 10 entries.
