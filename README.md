# Getting Started

## Run the application

The application was written in java 17 and is run locally on port 8000 using the command:
```
./gradlew bootRun
```
## Run the tests

To run tests, use:

```
./gradlew test
```
## Code style check

To run code style analysis (Checkstyle), use the command:
```
./gradlew style
```
## Run the traffic simulation

To run the traffic simulation, use the curl command below:
```
curl -X POST http://localhost:8000/api/v1/traffic-simulation/run \
     -H "Content-Type: application/json" \
     -d @input.json \
     -o output.json
```
