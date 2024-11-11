# {{ artifact-id }}-bom

A [Bill of Materials](https://www.baeldung.com/spring-maven-bom) for the {{ project-title }}.

## Use

To use any of the modules from this project, you should include it's Bill of Materials in the parent pom of your service:

```xml
    <properties>
        <{{ group-id }}.{{ artifact-id }}.version>VERSION</{{ group-id }}.{{ artifact-id }}.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>{{ group-id }}</groupId>
                <artifactId>{{ artifact-id}}-bom</artifactId>
                <version>${{'{'}}{{ group-id }}.{{ artifact-id }}.version.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    <dependencyManagement>
```
