# Table2Json
Transform database tables into json via a POST call


## Usage

To use this application, there are two parts of parameters you have to provide.

### Payload with connection parameters
* url - connection string
* user - username for the database connection
* pass - password for the database connection (*will be send unencrypted*)
~~~~ .json
{
	"url": "jdbc:postgresql://localhost:32769/jay_database",
	"user": "postgres",
	"pass": ""
}
~~~~


### Queryparameters
* database - (*Optional*) define what database needs to be checked
* schema - (*Optional*) define what database needs to be checked
* table - (*Required*) define what table will be parsed
~~~~
http://localhost:8080/Table2Json/table2json?database=jay_database&schema=jay_schema&table=jay_table1
~~~~

