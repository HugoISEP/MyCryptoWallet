# MyCryptoWallet  

This application allows you to save your current cryptocurrency portfolio in order to have an overview of what you own and the total estimated price.
It is also possible to see the results of the trading bot connected to the application and to receive push notifications for each trade made.
  
## Set up

### credentials  
> The project use Binance API, so get your account (ask us for a referential link 😉) and setup your api credentials

> Ask us to get the credentials of the trading bot api (CRYPTO_API_KEY & CRYPTO_API_PASSWORD)

> Create a file apikey.properties in the root of the project and add these lines (don't forget the quotes):  
 ```
BINANCE_API_KEY="YOUR BINANCE API KEY" 
BINANCE_API_PASSWORD="YOUR BINANCE API PASSWORD"
CRYPTO_API_KEY="YOUR CRYPTO API KEY" 
CRYPTO_API_PASSWORD="YOUR CRYPTO API PASSWORD"
```

### FireBase
The application works with firebase in order to receive push notifications.   
Ask for the **google-services.json** file and place it in the **/app** directory

<br>

**Credits:**
PIONNIER Timothée (10574)
CHEMILLIER Hugo (10478)


