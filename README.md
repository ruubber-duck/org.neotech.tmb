# org.neotech.tmb
Write timestamps to MySQL with in-queue buffering. 

### Prerequisites

You really need installed and working MySQL databse server.

### Installing

1. Create MySQL database using script inside {$TMB_HOME}/etc/MySQLCreate.sql or just type and run these strings:

```
CREATE SCHEMA `tmb` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `tmb`.`TmRcrd` (
  `tm` DATETIME NOT NULL,
  PRIMARY KEY (`tm`));
```

2. Specify connection properties in files 

```
{$TMB_HOME}/etc/tmb-app.properties
{$TMB_HOME}/etc/tmb-pswd.properties
```


## Running 

Use bash or cmd script

```
./tmb.sh
```

```
tmb.bat
```

## CLI Help

```
 -h,--help      Show help.
 -p,--present   Show database content.
Press <q + Enter> to stop.
```


