# simple-video-streaming
Simple video streaming application

Running
---
To successfully start and test application, please follow the steps outlined below.

Prerequisites:
- Java Development Kit (JDK) 17: Ensure that Java 17 is installed on your system. You can verify the installation by 
running `java -version` in your terminal or command prompt. If Java 17 is not installed, download it from the official
Oracle website or consider using a version manager like SDKMAN! for installation.
- Docker: Confirm that Docker is installed and properly configured on your machine. To verify, execute 
`docker --version`. If Docker is not installed, download it from the official Docker website and follow the installation
instructions for your operating system.

Starting the Application:
- Open a Terminal or Command Prompt: Navigate to the root directory of project where the mvnw script is located.
- Run the Application with the Development Profile: Execute the following command to start the application with the dev 
profile:
- 
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

This command utilizes the Maven Wrapper (`mvnw`) to run Spring Boot application with the specified profile.

Wait for the Application to Initialize: Allow the application to fully start. You should see log messages indicating 
that the application is running and listening on port 8080.

Accessing the API Documentation:

Once the application is running, you can access the API documentation and test the endpoints using Swagger UI:
- Open a Web Browser: Launch your preferred web browser.
- Navigate to Swagger UI: Enter the following URL in the address bar:

```url
http://localhost:8080/api/swagger-ui/index.html
```

This interface allows you to explore and interact with the API endpoints provided by your application.

Troubleshooting Tips:
- Port Conflicts: If port 8080 is in use by another application, you may encounter issues starting application. 
Ensure that the port is available or modify the application's configuration to use a different port.
- Docker Services: Application depends on services running in Docker containers, ensure that Docker is running 
and the necessary containers are up and accessible.

---

Design and Development Decisions
---

**Video Metadata and Storage**

- Current Implementation:
Videos metadata, along with links to their respective files, are stored in a single database table.

- Future Considerations:
To improve performance for `/play` requests, it may be beneficial to create a dedicated table specifically for streamable file links.
  - This table could be normalized, with data like actor information stored in separate tables.
  - These changes, however, require a deeper understanding of the business requirements to ensure alignment with needs.

**Search Functionality**

- Current State:
The current database schema does not yet support advanced search capabilities.

- Potential Enhancements:
  - Explore integrating Elasticsearch for full-text search and more complex querying capabilities.
  - Alternatively, consider adding plugin-based indexes to PostgreSQL, enabling efficient searches on `VARCHAR` columns. Further research is necessary to determine the best approach.

**Engagement Tracking**

- Implementation Details:
User interactions with `/play` and `/load` endpoints are recorded as engagements.
  - Engagements are processed asynchronously using a pub/sub event mechanism, which operates in a separate thread.

- Future Scalability:
Engagement tracking could be offloaded to specialized analytics services, such as Google Analytics or Mixpanel, to leverage their infrastructure for better insights and scalability.
