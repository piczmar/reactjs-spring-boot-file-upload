# File upload application

It consists of two parts: 
- client - ReactJS application
- server - Spring Boot application

They can be built and deployed independently.

For more instructions navigate to [client](client/README.md) and [server](server/README.md) readme.

## Future enhancements

1. Add progress bar to client UI. It would give better user experience when uploading large files.
2. Enhance uploaded files table (client search, scrollbars, headers show/hide, etc.)
3. Write more tests (client and server side)
4. Add test coverage reports generation in Maven build (e.g. Jacoco plugin)
5. Provide docker images for client and server for easy deployments (e.g. on Kubernetes)
6. Provide production configurations (e.g. using non-embedded database)
7. Provide Jenkinsfile for Jenkins build pipeline configuration
