# Paper Trader

## Goals

- To give the user a good stock simuation  to practice for the real stock market
- Accurate simulation data
- Stocks change every day true to what could be seen in the real stock market
- A friendly user interface to make it easy to use for the end user

## Simulation

### How Stocks are Stored:

Each stock has a few values that define them:

- Average Growth per day
- Standard Deviation
- Share Value
- Volume of Shares

Average growth per day and standard deviation are two values that are calculated beforehand by historical stock data.  They represent how much the stock should grow per day.  Using these two values combined can create a semi-realistic simulation  of how the stock will grow.

Share Value is how much money a single share is worth. This is based off recent data as of October 2025.

Volume of Shares is how many shares the stock has in total, this multiplied by the Share Value is how much the company is worth overall.  This is based off recent data as of October 2025.

### Stock Incrementing Per Day:

1. Each stock currently stores its own standard deviation and average growth per day
2. Every stock uses a random gaussian and its own standard deviation to create a realistic change in the stock's value.
3. Adding the average growth per day creates a change that should be true to life in a way that feels realistic and yet fair.
4. To make the game easier, it's possible to add a small bias to the starting average growth to make it easier to grow your portfolio.
5. A random value determines whether or not the average growth per day changes, to add another element of randomness to the simulation.

### How Portfolio is Stored:

The portfolio has a few values that defines it:

- Money
- Owned Stocks
- Trades

Money is the liquid cash that is not in the stock market.  It can be used to buy stocks and -- if a stock goes up in value -- money will increase.

Owned stocks is the number of shares per stock that the player owns.  It is stored in shares instead of money to allow an easier conversion back to money via share value.

Trades is the historical trades the player has made.  After a trade is made it is added to the list and never removed.  Currently these values are not used, but will be used later when UI is implemented.

## Running

The main way to run the code is via an IDE.

### IDE

- In your chosen IDE, clone the GitHub repository.
- Java will run its dependencies, after it is finished, make sure to sync the gradle project.
- After the project is synced, in gradle tasks, select application, then 'run' to run the program.