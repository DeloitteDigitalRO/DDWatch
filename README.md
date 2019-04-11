# DDWatch

**Prerequisites**
- Install Docker Desktop(there are also alternative docker installers)
- Make sure you don't have Postgres installed locally

**Docker - Postgres integration and application lifecycle**
- First run 'mvn docker:run' to have the postgres image up and running.
- You can start the spring boot app with 'mvn spring-boot:run'.
- The 'mvn docker:stop' command deletes the image and stops and removes the container
