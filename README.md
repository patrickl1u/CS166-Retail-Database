# CS166 Project

## Online Shopping System for Retail Shops

### About

Class project for CS166 at UC Riverside.

### Setup
---

Requires `postgresql-12` for compatibility with JDBC. We will be using `conda` to install and manage all our packages and details to set up will follow.

#### Initialize Database

Scripts to create database located in `./sql/scripts/`. Scripts work best when executed in this directory.

First execute `start_db.sh` to initialize everything properly. Then, run `create_db.sh` to import data into the newly created database. 

- Note that for compatibility with Django, postgresql-12 needs to be installed. This can be done with `conda` as detailed below.

#### Setup Django

Recommended install method is by using `conda`. Install Miniconda from [here](https://docs.conda.io/en/latest/miniconda.html) or by running the commands below. 

##### Install miniconda

```
wget https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-x86_64.sh
bash Miniconda3-latest-Linux-x86-64.sh
```

When installing on UCR lab machines, `conda` should be installed to `/extra/$USER/miniconda` to avoid having to reinstall every time.

Once installed, run these commands:

```
cp ~/.bashrc /extra/$USER/.bashrc_conda
source /extra/$USER/.bashrc_conda
```

Run the second line every login to restore the `conda` environment.

Create a `conda` environment by running: 
```
conda create -n cs166env python=3.9
```

Activate this environment by running:
```
conda activate cs166env
```

Use `pip` to install dependencies by running `pip install -r requirements.txt`. Note that `psycopg2` requires `libpq-dev` to be installed. The package is available in Anaconda and can be installed as follows: 
```
conda install -c anaconda libpq 
```

In addition, the version of `postgres` installed on UCR lab machines is not compatible with `psycopg2`. Thus, we install `postgres` using `conda` as follows:
```
conda install -c conda-forge postgresql
```

##### Using miniconda

After everything is set up properly, the following commands can be run to initialize the environment:
```
source /extra/$USER/.bashrc_conda
conda activate cs166env
```

##### Database Configuration

Create `.env` in `djangofrontend/djangofrontend` with the following content:

```
SECRET_KEY=doesntreallymattersincewearejustrunningasdevelopment
DB_NAME=<insert DB name (probably username followed by '_DB')>
DB_USER=<insert user here (find out with 'echo $USER')>
DB_PASSWORD=
DB_HOST=localhost
DB_PORT=1024
```

Make sure to edit the above configuration with the proper information before saving the file.

#### `ngrok`

[ngrok](https://ngrok.com/) is a free program that simplifies access to web services, even if they are behind firewalls or other setups disallowing direct access.

A free account is required to use `ngrok`. I would recommend accessing the web interface directly but this is needed for accessing programs running on UCR's lab machines. `ngrok` is already installed (from `pip install -r requirements.txt`) if needed. 

After signing up, we must install the authentication token to the local machine. The authentication token can be found [here](https://dashboard.ngrok.com/get-started/setup). 

Install the token as follows (can't copy/paste directly from site):
```
ngrok authtoken <INSERT AUTH TOKEN HERE>
```

#### Setup Java Database Connectivity

Need to ensure that `javac` is available to compile the program (`default-jre` on Debian systems). To run Java software, ensure that `default-jre` or some equivalent is installed.

UCR lab machines already have this software available.

### Running Client (JDBC)
---

A script is provided to compile and run the client. 

Alternatively, run the client manually as follows:
    
```
java -cp ../classes:../lib/pg73jdbc3.jar Retail $USER"_DB" 1024 $USER
```

### Web Interface
---

Proof of Concept Retail E-Commerce platform

Run with the following from `djangofrontend`:
```
python manage.py runserver 0.0.0.0:5000
```

Requires that the `postgres` server is up and accesible.

#### `ngrok`

To access the frontend, run in a separate window: 
```
ngrok http 5000
```
The url displayed will be a revrse proxy that connects us to the frontend.


Database info accessible from here.
- `/`                    : home page (wip)
- `/store/`              : view all stores
- `/store/<int>`         : view store info
- `/store/<int>`/products: view store products (wip)
- `/user/<int>`          : view user info