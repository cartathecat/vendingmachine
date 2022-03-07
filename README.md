# Vending Machine

## Dependencies
The following dependencies are required to build the Vending Machine microservice and the Vending Machine test harness.

* Java version 1.8 (Sorry, I dont have v11 on my laptop)

#
##### Table of Contents  
[Vending Machine microservice](#Vending-Machine-microservice)  
[Vending Machine test harness](#Vending-Machine-test-harness)  
[Running the Vending Machine microservice](#Running-the-Vending-Machine-microservice)  
[Running the Vending Machine test harness](#Running-the-Vending-Machine-test-harness) 
[Vending Machine test harness operations](#Vending-Machine-test-harness-operations)

##### Vending machine end-points
[Initialise the vending machine](#Initialise-the-vending-machine)  
[Listing products](#Listing-products)  
[Vend a product](#Vend-a-product)  
[Refund deposited coins](#Refund-deposited-coins)  
[Coin Bucket](#Coin-Bucket)  
[Float and deposited values](#Float-and-deposited-values)  
[Vending Machine status](#Vending-Machine-status)  
[Swagger](#Swagger)  

<br/>

#
## Vending Machine microservice
The Vending Machine microservice is written in Java Spring Boot, to process vending mchine actions.

The Vending Machine microservice is a Java Maven project with Junit dependency. 

The project source code can be found in the following GitHub repository:

&nbsp;&nbsp;&nbsp;&nbsp;github.com/cartathecat/vendingmachine

## Build instructions
The Vending Machine microservice can be built with Maven as;

```
mvn clean install
```

This will build and run test of the microservice, the result will be place in the .\target folder as VendingMachine-1.0.0.0.jar 

[Top](#Vending-Machine)

#

## Vending Machine test harness
The Vending Machine test harness is a simple Java console applicaiton to simulate a Vending Machine, that uses the vending machine microservice.

The Vending Machine test harness is a Java Maven project.

The projects source code can be found in the following GitHub repository:

&nbsp;&nbsp;&nbsp;&nbsp;github.com/cartathecat/vendingtestharness

## Build instructions
The Vending Machine test harness can be built with Maven as;

```
mvn clean install
```

This will build and run test of the test hardness, the result will be place in the .\target folder as VendingTestHarness-1.0.0.0.jar 

[Top](#Vending-Machine)

#

## Running the Vending Machine microservice
The Vending Machine microservice can be run as:

```
java -jar .\{file location}\VendingMachine-1.0.0.0.jar
```

This will run the vending machine microservice with the default configuration application.yaml file:

```
logging:
  level:
    org:
      springframework: OFF
server:
  port: 8091
  error:
    include-message: ALWAYS

```

To override the default application.yaml file, such as application-test.yaml file, create a file called application-test.yaml and point the Spring Boot microservice to use the new configuration file.

```
java -jar .\{file location}\VendingMachine-1.0.0.0.jar --spring.profiles.active=test
```
NOTE : dont add the application prefix or the .yaml, yml sufix.

For further details on how to [Spring YAML Configuration](https://docs.spring.io/spring-boot/docs/1.0.0.RC5/reference/html/boot-features-external-config.html)

[Top](#Vending-Machine)

#

## Running the Vending Machine test harness
The Vending Machine test harness can be run as:

```
java -jar .\target\VendingTestHarness-1.0.0.0.jar [-p {port} -t {timeout}]
```

Valid arguments are:
Argument | Required | Meaning
-------- | -------- | ------------------------------------------------------------------
--port | |
-p | Yes | Port number on which the Vending Machine microservice is listening on
--timeout | |
-t | No | Timeout value for invoking an end-point (in seconds) - the default value is 5 seconds

<br/>
The testharness can be running before Vending Machine microservice is running.  In this case, timeouts will occur when invoking the endpoints.
<br/>
<br/>

[Top](#Vending-Machine)

#

# Vending Machine end-points

## Initialise the vending machine
To initialise the vending machine with coins:

Request
```
GET
http://{HOST}:{PORT}/vendingmachine/v1/init/TWOPOUND:5,ONEPOUND:10,FIFTY:10,TWENTY:10,TEN:20,FIVE:20,TWO:20,ONE:20
```

Response
ResponseStatus: 200
```
{
    "message": "Vending initialised"
}
```

Coins are passed on the URI as a GET request, in an array, together with the number of coins, separated by a colon (:) as FIFTY:10, rather than as properties on the header or as a POST with a JSON payload.

Valid coins that can be passed are;

```
TWOPOUND
ONEPOUND
FIFTY
TWENTY
TEN
FIVE
TWO
ONE
```

When coins are invalid, entered on /init or /deposit, the following error is returned:

Response
ResponseStatus: 400
```
{
    "timestamp": "2022-03-05T18:21:25.077+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Coin XXXX is not valid",
    "path": "/vendingmachine/v1/deposit/ONEPOUND:1,FIFTY:2,XXXX"
}
```


## Errors responses

### Vending machine has already been initialized
If the Vending Machine is initialized more than once, the following error response is returned:

ResponseStatus: 500
```
{
    "timestamp": "2022-03-05T15:22:07.654+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Vending machine has already been initialized",
    "path": "/vendingmachine/v1/init/TWOPOUND:5,ONEPOUND:10,FIFTY:10,TWENTY:10,TEN:20,FIVE:20,TWO:20,ONE:20"
}
```

### Vending machine is in vending status
If the Vending Machine is initialized and coins have been deposited to vend products, the following error response is returned:

ResponseStatus: 500
```
{
    "timestamp": "2022-03-05T15:26:13.738+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Vending machine is in vending status",
    "path": "/vendingmachine/v1/init/TWOPOUND:5,ONEPOUND:10,FIFTY:10,TWENTY:10,TEN:20,FIVE:20,TWO:20,ONE:20"
}
```

[Top](#Vending-Machine)

#

## Listing products
To return a list of products: 

NOTE: The list of products are hard coded.

Request
```
GET
http://{HOST}:{PORT}/vendingmachine/v1/products
```

Response
ResponseStatus: 200
```
[
    {
        "id": 1,
        "description": "Cheese and Onion Crisps",
        "price": 150,
        "quantityCount": 10
    },
    {
        "id": 2,
        "description": "Salt and Vinegar Crisps",
        "price": 150,
        "quantityCount": 10
    },
    {
        "id": 3,
        "description": "Prawn Cocktail Crisps",
        "price": 150,
        "quantityCount": 10
    },
    {
        "id": 4,
        "description": "Twix Chocolate Bar",
        "price": 100,
        "quantityCount": 10
    },
    {
        "id": 5,
        "description": "Mars Chocolate Bar",
        "price": 110,
        "quantityCount": 5
    },
    {
        "id": 6,
        "description": "Snickers Chocolate Bar",
        "price": 120,
        "quantityCount": 5
    },
    {
        "id": 7,
        "description": "Coka Cola",
        "price": 140,
        "quantityCount": 5
    },
    {
        "id": 8,
        "description": "Diet Coka Cola",
        "price": 140,
        "quantityCount": 5
    },
    {
        "id": 9,
        "description": "Fanta",
        "price": 125,
        "quantityCount": 6
    },
    {
        "id": 10,
        "description": "Diet Fanta",
        "price": 125,
        "quantityCount": 6
    }
]
```

[Top](#Vending-Machine)

#

## Deposit coins to vend
To deposit coins to vend products:

Request
```
GET
http://{HOST}:{PORT}/vendingmachine/v1/deposit/ONEPOUND:1,FIFTY:2
```

Response
ResponseStatus: 200
```
{
    "deposit": 200,
    "message": "Deposit"
}
```

Multiple vend requests can be made to increase the deposited amount.

## Errors responses

### Vending machine is not yet initialised
If the Vending Machine is not initialized, the following error response is returned:

ResponseStatus: 500
```
{
    "timestamp": "2022-03-05T15:31:09.451+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Vending machine is not yet initialised",
    "path": "/vendingmachine/v1/deposit/ONEPOUND:1,FIFTY:2"
}
```

[Top](#Vending-Machine)

#

## Vend a product
To vend a product:

Request
```
GET
http://{HOST}:{PORT}/vendingmachine/v1/vend/1
```

Response
ResponseStatus: 200
```
{
    "product": {
        "id": 9,
        "description": "Fanta",
        "price": 125,
        "quantityCount": 5
    },
    "change": 75,
    "vendtype": "PRODUCT",
    "changeReturn": [
        {
            "coinQuantity": 1,
            "coin": "FIFTY"
        },
        {
            "coinQuantity": 1,
            "coin": "TWENTY"
        },
        {
            "coinQuantity": 1,
            "coin": "FIVE"
        }
    ]
}
```

The response shows which product has been selected with the change given ( if any) and the number of coins returned.

The property ``vendtype`` can be either ``PRODUCT`` or ``REFUND``.  For a successfully vended item, the value will be ``PRODUCT``.

To simulate a real-life vending machine - at least the one I last used - change is returned after each product has been successfully vended.  If multiple prodcts are required, then coins have to be deposited again.

## Errors responses

### Vending machine is not yet initialised
When the vending machine is not yet initialised

ResponseStatus: 500
```
{
    "timestamp": "2022-03-05T15:49:57.627+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Vending machine is not yet initialised",
    "path": "/vendingmachine/v1/vend/22"
}
```

### Product code XX not found
If the product can't be foud, the follow error is returned:

ResponseStatus: 404
```
{
    "timestamp": "2022-03-05T15:44:09.701+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Product code 22 not found",
    "path": "/vendingmachine/v1/vend/22"
}
```

[Top](#Vending-Machine)

#

## Refund deposited coins
To refund any money once coins have been deposited:

Request
```
GET
http://{HOST}:{PORT}/vendingmachine/v1/refund
```

Response
ResponseStatus: 200
```
{
    "change": 200,
    "vendType": "REFUND",
    "changeReturn": [
        {
            "coinQuantity": 1,
            "coin": "TWOPOUND"
        }
    ]
}
```

The response shows the refunded amount, together with the vendtype and the coins.

The property ``vendtype`` can be either ``PRODUCT`` or ``REFUND``.  For a refund, the value will be ``REFUND``.

## Error responses

### Vending machine is not yet initialised
When the vending machine is not yet initialised

ResponseStatus: 500
```
{
    "timestamp": "2022-03-05T15:49:57.627+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Vending machine is not yet initialised",
    "path": "/vendingmachine/v1/vend/22"
}
```

### Not enough coins to return change deposit
If there is not enough money deposited when the refund is requested, the following error is returned:

ResponseStatus: 500
```
{
    "timestamp": "2022-03-05T15:45:19.865+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Not enough coins to return change deposit",
    "path": "/vendingmachine/v1/refund"
}
```

[Top](#Vending-Machine)

#

## Coin Bucket
To return a list of coins in the vending machine:

Request
```
GET
http://{HOST}:{PORT}/vendingmachine/v1/coinbucket
```

Response
ResponseStatus: 200
```
{
    "ONE": 20,
    "TWO": 20,
    "FIVE": 20,
    "TEN": 20,
    "TWENTY": 10,
    "FIFTY": 10,
    "ONEPOUND": 10,
    "TWOPOUND": 5
}
```

## Error responses

### Vending machine is not yet initialised
When the vending machine is not yet initialised

ResponseStatus: 500
```
{
    "timestamp": "2022-03-05T15:49:57.627+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Vending machine is not yet initialised",
    "path": "/vendingmachine/v1/vend/22"
}
```

[Top](#Vending-Machine)

#

## Float and Deposited values
To return the float and deposited amounts:

Request
```
GET
http://{HOST}:{PORT}/vendingmachine/v1/floatvalue
```

Response
ResponseStatus: 200
```
{
    "floatValue": 3060,
    "depositValue": 0
}    
```

## Error responses

### Vending machine is not yet initialised
When the vending machine is not yet initialised

ResponseStatus: 500
```
{
    "timestamp": "2022-03-05T15:49:57.627+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Vending machine is not yet initialised",
    "path": "/vendingmachine/v1/vend/22"
}
```

[Top](#Vending-Machine)

#

## Vending Machine status
To return the status of the vending machine:

Request
```
GET
http://{HOST}:{PORT}/vendingmachine/v1/status
```

Response
ResponseStatus: 200
```
{
    "status": "READY_TO_VEND"
}    
```
Valid status can be;

Status                    | Meaning
-------------------       | --------------------------------------------------------------------------------------------------------------
```INACTIVE```            | When the vending machine has not been initialised.
```READ_TO_VEND```        | When the vending machine has been initialised, but no money has been deposited.
```VENDING_IN_PROGRESS``` | When the vending machine has been initialied, and money has been deposited, but no product has been selected.

<br />

## Error responses

### Vending machine is not yet initialised
When the vending machine is not yet initialised

ResponseStatus: 500
```
{
    "timestamp": "2022-03-05T15:49:57.627+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Vending machine is not yet initialised",
    "path": "/vendingmachine/v1/vend/22"
}
```

[Top](#Vending-Machine)

#

## Vending Machine test harness operations
The Vending Machine test harness is a simple Java console application.

When ran, the following screen is displayed:



[Top](#Vending-Machine)

</br>

## Swagger
Swagger endpoint is included

http:[HOST]:[PORT]/swagger-ui.html

[Top](#Vending-Machine)

</br>

 
## Impprovements

There can be many improvements to the Vending Machine microservice, includimg:

* Amemd /init endpoint to be a POST and pass a payload body
* Amend /deposit endpoint to be a POST and pass a payload body
* Amend /vend endpoint to be a PUT
* Unique errors, rather than using Spring Boot ``ResponseStatusException`` 
* Authenticate when coins are deposited, to stop other users continuing with the vend
* Timeout and issue a refund when no action operation is performed after coins are deposited.


